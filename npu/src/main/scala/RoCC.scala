package npu
import chisel3._
import chisel3.util._
import org.chipsalliance.cde.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tile._
import hardfloat._

class NPU(implicit p: Parameters) extends LazyRoCC(opcodes = OpcodeSet.custom3) {
  override lazy val module = new NPUModule(this)
}

class NPUModule(outer: NPU) extends LazyRoCCModuleImp(outer) with HasCoreParameters {
  val cmd = Queue(io.cmd)

  val funct = cmd.bits.inst.funct
  val data1 = cmd.bits.rs1
  val data2 = cmd.bits.rs2

  val doAdd = (funct === 0.U)
  val doSub = (funct === 1.U)
  val doMul = (funct === 2.U)
  val doDiv = (funct === 3.U)

  val rec1 = recFNFromFN(8, 8, data1(15, 0))
  val rec2 = recFNFromFN(8, 8, data2(15, 0))
  val neg_rec2 = recFNFromFN(8, 8, Cat(~data2(15), data2(14, 0)))
  val one_rec = recFNFromFN(8, 8, "h3F80".U)

  val muladd_rec = Module(new MulAddRecFN(8, 8))
  val div_rec = Module(new DivSqrtRecFN_small(8, 8, 0))

  muladd_rec.io.op := 0.U
  muladd_rec.io.a := rec1
  muladd_rec.io.b := Mux(doMul, rec2, one_rec)
  muladd_rec.io.c := Mux(doSub, neg_rec2, Mux(doAdd, rec2, 0.U))
  muladd_rec.io.roundingMode := consts.round_near_even
  muladd_rec.io.detectTininess := consts.tininess_afterRounding

  div_rec.io.inValid := cmd.valid && doDiv
  div_rec.io.sqrtOp := false.B
  div_rec.io.a := rec1
  div_rec.io.b := rec2
  div_rec.io.roundingMode := consts.round_near_even
  div_rec.io.detectTininess := consts.tininess_afterRounding

  val muladd_result = fNFromRecFN(8, 8, muladd_rec.io.out)
  val div_result = fNFromRecFN(8, 8, div_rec.io.out)

  cmd.ready := io.resp.ready
  io.resp.valid := (cmd.valid && !doDiv) || div_rec.io.outValid_div
  io.resp.bits.rd := cmd.bits.inst.rd
  io.resp.bits.data := Mux(div_rec.io.outValid_div, div_result, muladd_result)

  io.busy := cmd.valid
  io.interrupt := false.B
  io.mem.req.valid := false.B
  io.mem.req.bits.addr := 0.U
  io.mem.req.bits.tag := 0.U
  io.mem.req.bits.cmd := 0.U
  io.mem.req.bits.size := 0.U
  io.mem.req.bits.signed := false.B
  io.mem.req.bits.data := 0.U
  io.mem.req.bits.phys := false.B
  io.mem.req.bits.dprv := 0.U
  io.mem.req.bits.dv := false.B
}

class NPURoCCConfig extends Config((site, here, up) => {
  case BuildRoCC => up(BuildRoCC) ++ Seq(
    (p: Parameters) => {
      implicit val q = p
      val npu = LazyModule(new NPU)
      npu
    }
  )
})