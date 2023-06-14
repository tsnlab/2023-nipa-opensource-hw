package chipyard

import chisel3._
import chisel3.util._
import freechips.rocketchip.amba.axi4._
import freechips.rocketchip.subsystem.BaseSubsystem
import org.chipsalliance.cde.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import freechips.rocketchip.interrupts._
import freechips.rocketchip.util._

case class WrapperParams()

case object WrapperKey extends Field[Option[WrapperParams]](None)

class WrapperIO extends Bundle
{
  // clocks
  val clock_out = Output(Clock())

  // resets
  val reset_out = Output(Bool())

  // interrupts
  val interrupt = Input(Bool())

  // S.AXI.AR
  val s_axi_araddr = Output(UInt(32.W))
  val s_axi_arburst = Output(UInt(2.W))
  val s_axi_arcache = Output(UInt(4.W))
  val s_axi_arlen = Output(UInt(8.W))
  val s_axi_arprot = Output(UInt(3.W))
  val s_axi_arsize = Output(UInt(3.W))
  val s_axi_arvalid = Output(Bool())
  val s_axi_arready = Input(Bool())

  // S.AXI.R
  val s_axi_rdata = Input(UInt(32.W))
  val s_axi_rlast = Input(Bool())
  val s_axi_rresp = Input(UInt(2.W))
  val s_axi_rvalid = Input(Bool())
  val s_axi_rready = Output(Bool())

  // S.AXI.AW
  val s_axi_awaddr = Output(UInt(32.W))
  val s_axi_awburst = Output(UInt(2.W))
  val s_axi_awcache = Output(UInt(4.W))
  val s_axi_awlen = Output(UInt(8.W))
  val s_axi_awprot = Output(UInt(3.W))
  val s_axi_awsize = Output(UInt(3.W))
  val s_axi_awvalid = Output(Bool())
  val s_axi_awready = Input(Bool())

  // S.AXI.B
  val s_axi_bready = Output(Bool())
  val s_axi_bresp = Input(UInt(2.W))
  val s_axi_bvalid = Input(Bool())

  // S.AXI.W
  val s_axi_wdata = Output(UInt(32.W))
  val s_axi_wlast = Output(Bool())
  val s_axi_wstrb = Output(UInt(4.W))
  val s_axi_wvalid = Output(Bool())
  val s_axi_wready = Input(Bool()) 
}

class WrapperPeripheral(implicit p: Parameters) extends LazyModule 
{
  val device = new SimpleDevice("wrapper-tsn", Seq("xilinx,wrapper-tsn"))

  val slavenode = AXI4SlaveNode(Seq(AXI4SlavePortParameters(
    slaves = Seq(AXI4SlaveParameters(
      address       = List(AddressSet(0x1000, 0x3ff)),
      resources     = device.reg("slave"),
      regionType    = RegionType.UNCACHED,
      supportsWrite = TransferSizes(1, 4),
      supportsRead  = TransferSizes(1, 4),
      interleavedId = Some(0))), // AXI4-Lite never interleaves responses
    beatBytes = 4)))

  val intnode = IntSourceNode(IntSourcePortSimple(num = 1, resources = device.int))

  val tl_slave = TLIdentityNode()
    (slavenode :=
        AXI4Buffer() :=
        AXI4Fragmenter() :=
        AXI4UserYanker(Some(16)) :=
        AXI4Deinterleaver(64) :=
        TLToAXI4() :=
        TLFragmenter(4, 64, holdFirstDeny = true)
      := tl_slave)

  lazy val module = new Impl
  class Impl extends LazyModuleImp(this) {

    val io = IO(new WrapperIO)

    val (s, _) = slavenode.in(0)
    val (i, _) = intnode.out(0)

    // control
    io.clock_out      := clock 
    io.reset_out      := reset

    // interrupt
    i(0)              := io.interrupt

    // S.AXI.AR
    s.ar.ready        := io.s_axi_arready 
    io.s_axi_arvalid  := s.ar.valid
    io.s_axi_araddr   := s.ar.bits.addr
    io.s_axi_arburst  := s.ar.bits.burst
    io.s_axi_arcache  := s.ar.bits.cache
    io.s_axi_arlen    := s.ar.bits.len
    io.s_axi_arprot   := s.ar.bits.prot 
    io.s_axi_arsize   := s.ar.bits.size

    // S.AXI.R
    io.s_axi_rready   := s.r.ready
    s.r.valid         := io.s_axi_rvalid
    s.r.bits.data     := io.s_axi_rdata
    s.r.bits.last     := io.s_axi_rlast
    s.r.bits.resp     := io.s_axi_rresp

    // S.AXI.AW
    s.aw.ready        := io.s_axi_awready
    io.s_axi_awvalid  := s.aw.valid
    io.s_axi_awaddr   := s.aw.bits.addr
    io.s_axi_awburst  := s.aw.bits.burst
    io.s_axi_awcache  := s.aw.bits.cache
    io.s_axi_awlen    := s.aw.bits.len
    io.s_axi_awprot   := s.aw.bits.prot
    io.s_axi_awsize   := s.aw.bits.size

    // S.AXI.B
    io.s_axi_bready   := s.b.ready
    s.b.valid         := io.s_axi_bvalid
    s.b.bits.resp     := io.s_axi_bresp

    // S.AXI.W
    s.w.ready         := io.s_axi_wready
    io.s_axi_wvalid   := s.w.valid
    io.s_axi_wdata    := s.w.bits.data
    io.s_axi_wlast    := s.w.bits.last
    io.s_axi_wstrb    := s.w.bits.strb
  }
}

trait HasWrapper { this: BaseSubsystem =>
  implicit val p: Parameters
  val wrapper = LazyModule(new WrapperPeripheral()(p))

  ibus.fromSync := wrapper.intnode
  pbus.toFixedWidthSlave(Some("mmio-tsn")) {
      (wrapper.tl_slave)
  }
}

trait HasWrapperImp extends LazyModuleImp {
  val outer: HasWrapper
  val wrapperio = IO(new WrapperIO)
  wrapperio <> outer.wrapper.module.io
  dontTouch(wrapperio)
}
