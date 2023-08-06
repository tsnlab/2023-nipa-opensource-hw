package npu
import chisel3._
import chisel3.util._
import org.chipsalliance.cde.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tile._

class NPU(implicit p: Parameters) extends LazyRoCC(opcodes = OpcodeSet.custom3) {
  override lazy val module = new NPUModule(this)
}

class NPUModule(outer: NPU) extends LazyRoCCModuleImp(outer) with HasCoreParameters {
  val cmd = Queue(io.cmd)

  val funct = cmd.bits.inst.funct
  val data1 = cmd.bits.rs1
  val data2 = cmd.bits.rs2

  val doAdd = (funct === 0.U)

  cmd.ready := io.resp.ready
  io.resp.valid := cmd.valid
  io.resp.bits.rd := cmd.bits.inst.rd
  io.resp.bits.data := data1 + data2

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