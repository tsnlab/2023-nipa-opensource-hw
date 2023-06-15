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

  // M.AXI.AR
  val m_axi_araddr = Output(UInt(32.W))
  val m_axi_arburst = Output(UInt(2.W))
  val m_axi_arcache = Output(UInt(4.W))
  val m_axi_arlen = Output(UInt(8.W))
  val m_axi_arprot = Output(UInt(3.W))
  val m_axi_arsize = Output(UInt(3.W))
  val m_axi_arvalid = Output(Bool())
  val m_axi_arready = Input(Bool())

  // M.AXI.R
  val m_axi_rdata = Input(UInt(32.W))
  val m_axi_rlast = Input(Bool())
  val m_axi_rresp = Input(UInt(2.W))
  val m_axi_rvalid = Input(Bool())
  val m_axi_rready = Output(Bool())

  // M.AXI.AW
  val m_axi_awaddr = Output(UInt(32.W))
  val m_axi_awburst = Output(UInt(2.W))
  val m_axi_awcache = Output(UInt(4.W))
  val m_axi_awlen = Output(UInt(8.W))
  val m_axi_awprot = Output(UInt(3.W))
  val m_axi_awsize = Output(UInt(3.W))
  val m_axi_awvalid = Output(Bool())
  val m_axi_awready = Input(Bool())

  // M.AXI.B
  val m_axi_bready = Output(Bool())
  val m_axi_bresp = Input(UInt(2.W))
  val m_axi_bvalid = Input(Bool())

  // M.AXI.W
  val m_axi_wdata = Output(UInt(32.W))
  val m_axi_wlast = Output(Bool())
  val m_axi_wstrb = Output(UInt(4.W))
  val m_axi_wvalid = Output(Bool())
  val m_axi_wready = Input(Bool())

  // S.AXI.AR
  val s_axi_araddr = Input(UInt(32.W))
  val s_axi_arburst = Input(UInt(2.W))
  val s_axi_arcache = Input(UInt(4.W))
  val s_axi_arlen = Input(UInt(8.W))
  val s_axi_arprot = Input(UInt(3.W))
  val s_axi_arsize = Input(UInt(3.W))
  val s_axi_arvalid = Input(Bool())
  val s_axi_arready = Output(Bool())

  // S.AXI.R
  val s_axi_rdata = Output(UInt(32.W))
  val s_axi_rlast = Output(Bool())
  val s_axi_rresp = Output(UInt(2.W))
  val s_axi_rvalid = Output(Bool())
  val s_axi_rready = Input(Bool())

  // S.AXI.AW
  val s_axi_awaddr = Input(UInt(32.W))
  val s_axi_awburst = Input(UInt(2.W))
  val s_axi_awcache = Input(UInt(4.W))
  val s_axi_awlen = Input(UInt(8.W))
  val s_axi_awprot = Input(UInt(3.W))
  val s_axi_awsize = Input(UInt(3.W))
  val s_axi_awvalid = Input(Bool())
  val s_axi_awready = Output(Bool())

  // S.AXI.B
  val s_axi_bready = Input(Bool())
  val s_axi_bresp = Output(UInt(2.W))
  val s_axi_bvalid = Output(Bool())

  // S.AXI.W
  val s_axi_wdata = Input(UInt(32.W))
  val s_axi_wlast = Input(Bool())
  val s_axi_wstrb = Input(UInt(4.W))
  val s_axi_wvalid = Input(Bool())
  val s_axi_wready = Output(Bool())
}

class WrapperPeripheral(implicit p: Parameters) extends LazyModule 
{
  val device = new SimpleDevice("wrapper-tsn", Seq("xilinx,wrapper-tsn"))

  val slavenode = AXI4SlaveNode(Seq(AXI4SlavePortParameters(
    slaves = Seq(AXI4SlaveParameters(
      address       = List(AddressSet(0x1000, 0x3ff)),
      resources     = device.reg("mmio-tsn"),
      regionType    = RegionType.UNCACHED,
      supportsWrite = TransferSizes(1, 4),
      supportsRead  = TransferSizes(1, 4),
      interleavedId = Some(0))), // AXI4-Lite never interleaves responses
    beatBytes = 4)))

  val intnode = IntSourceNode(IntSourcePortSimple(num = 1, resources = device.int))

  val masternode = AXI4MasterNode(Seq(AXI4MasterPortParameters(
    masters  = Seq(AXI4MasterParameters(
      name = "dma-tsn"
    ))
  )))

  val tl_manager = TLIdentityNode()
    (slavenode :=
        AXI4Buffer() :=
        AXI4Fragmenter() :=
        AXI4UserYanker(Some(16)) :=
        AXI4Deinterleaver(64) :=
        TLToAXI4() :=
        TLFragmenter(4, 64, holdFirstDeny = true) :=
        TLBuffer() :=
      tl_manager)

  val tl_client =  TLIdentityNode()
    (tl_client :=
      TLBuffer() :=
      AXI4ToTL() :=
      AXI4UserYanker(Some(16)) :=
      AXI4Fragmenter() :=
      AXI4Buffer() :=
    masternode)

  
  lazy val module = new Impl
  class Impl extends LazyModuleImp(this) {

    val io = IO(new WrapperIO)

    val (s, _) = slavenode.in(0)
    val (i, _) = intnode.out(0)
    val (m, _) = masternode.out(0)

    // control
    io.clock_out      := clock 
    io.reset_out      := reset

    // interrupt
    i(0)              := io.interrupt

    // M.AXI.AR
    s.ar.ready        := io.m_axi_arready
    io.m_axi_arvalid  := s.ar.valid
    io.m_axi_araddr   := s.ar.bits.addr
    io.m_axi_arburst  := s.ar.bits.burst
    io.m_axi_arcache  := s.ar.bits.cache
    io.m_axi_arlen    := s.ar.bits.len
    io.m_axi_arprot   := s.ar.bits.prot 
    io.m_axi_arsize   := s.ar.bits.size

    // M.AXI.R
    io.m_axi_rready   := s.r.ready
    s.r.valid         := io.m_axi_rvalid
    s.r.bits.data     := io.m_axi_rdata
    s.r.bits.last     := io.m_axi_rlast
    s.r.bits.resp     := io.m_axi_rresp

    // M.AXI.AW
    s.aw.ready        := io.m_axi_awready
    io.m_axi_awvalid  := s.aw.valid
    io.m_axi_awaddr   := s.aw.bits.addr
    io.m_axi_awburst  := s.aw.bits.burst
    io.m_axi_awcache  := s.aw.bits.cache
    io.m_axi_awlen    := s.aw.bits.len
    io.m_axi_awprot   := s.aw.bits.prot
    io.m_axi_awsize   := s.aw.bits.size

    // M.AXI.B
    io.m_axi_bready   := s.b.ready
    s.b.valid         := io.m_axi_bvalid
    s.b.bits.resp     := io.m_axi_bresp

    // M.AXI.W
    s.w.ready         := io.m_axi_wready
    io.m_axi_wvalid   := s.w.valid
    io.m_axi_wdata    := s.w.bits.data
    io.m_axi_wlast    := s.w.bits.last
    io.m_axi_wstrb    := s.w.bits.strb

    // S.AXI.AR
    io.s_axi_arready  := m.ar.ready
    m.ar.valid        := io.s_axi_arvalid
    m.ar.bits.addr    := io.s_axi_araddr
    m.ar.bits.burst   := io.s_axi_arburst
    m.ar.bits.cache   := io.s_axi_arcache
    m.ar.bits.len     := io.s_axi_arlen
    m.ar.bits.prot    := io.s_axi_arprot
    m.ar.bits.size    := io.s_axi_arsize

    // S.AXI.R
    m.r.ready         := io.s_axi_rready
    io.s_axi_rvalid   := m.r.valid
    io.s_axi_rdata    := m.r.bits.data
    io.s_axi_rlast    := m.r.bits.last
    io.s_axi_rresp    := m.r.bits.resp
    
    // S.AXI.AW
    io.s_axi_awready  := m.aw.ready
    m.aw.valid        := io.s_axi_awvalid
    m.aw.bits.addr    := io.s_axi_awaddr
    m.aw.bits.burst   := io.s_axi_awburst
    m.aw.bits.cache   := io.s_axi_awcache
    m.aw.bits.len     := io.s_axi_awlen
    m.aw.bits.prot    := io.s_axi_awprot
    m.aw.bits.size    := io.s_axi_awsize
    
    // S.AXI.B
    m.b.ready         := io.s_axi_bready
    io.s_axi_bvalid   := m.b.valid
    io.s_axi_bresp    := m.b.bits.resp

    // S.AXI.W
    io.s_axi_wready   := m.w.ready
    m.w.valid         := io.s_axi_wvalid
    m.w.bits.data     := io.s_axi_wdata
    m.w.bits.last     := io.s_axi_wlast
    m.w.bits.strb     := io.s_axi_wstrb
  }

}

trait HasWrapper { this: BaseSubsystem =>
  implicit val p: Parameters
  val wrapper = LazyModule(new WrapperPeripheral()(p))

  ibus.fromSync := wrapper.intnode
  pbus.toFixedWidthSlave(Some("mmio-tsn")) {
      (wrapper.tl_manager)
  }
  fbus.fromMaster(Some("dma-tsn"), buffer = BufferParams.default) {
    TLBuffer.chainNode(0)
  } := (wrapper.tl_client)
}

trait HasWrapperImp extends LazyModuleImp {
  val outer: HasWrapper
  val wrapperio = IO(new WrapperIO)
  wrapperio <> outer.wrapper.module.io
  dontTouch(wrapperio)
}

