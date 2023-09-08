package sifive.fpgashells.shell.xilinx

import chisel3._
import org.chipsalliance.cde.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tilelink._
import freechips.rocketchip.util.SyncResetSynchronizerShiftReg
import sifive.fpgashells.clocks._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._
import sifive.fpgashells.devices.xilinx.xilinxnexysvideomig._

class SysClockNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: ClockInputDesignInput, val shellInput: ClockInputShellInput)
  extends SingleEndedClockInputXilinxPlacedOverlay(name, designInput, shellInput)
{
  val node = shell { ClockSourceNode(freqMHz = 100, jitterPS = 50) }

  shell { InModuleBody {
    val clk: Clock = io
    shell.xdc.addPackagePin(clk, "R4")
    shell.xdc.addIOStandard(clk, "LVCMOS33")
  } }
}
class SysClockNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: ClockInputShellInput)(implicit val valName: ValName)
  extends ClockInputShellPlacer[NexysVideoShellBasicOverlays] {
  def place(designInput: ClockInputDesignInput) = new SysClockNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
}

//PMOD JA used for SDIO
// class SDIONexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: SPIDesignInput, val shellInput: SPIShellInput)
//   extends SDIOXilinxPlacedOverlay(name, designInput, shellInput)
// {
//   shell { InModuleBody {
//     val packagePinsWithPackageIOs = Seq(("D12", IOPin(io.spi_clk)),
//       ("B11", IOPin(io.spi_cs)),
//       ("A11", IOPin(io.spi_dat(0))),
//       ("D13", IOPin(io.spi_dat(1))),
//       ("B18", IOPin(io.spi_dat(2))),
//       ("G13", IOPin(io.spi_dat(3))))

//     packagePinsWithPackageIOs foreach { case (pin, io) => {
//       shell.xdc.addPackagePin(io, pin)
//       shell.xdc.addIOStandard(io, "LVCMOS33")
//       shell.xdc.addIOB(io)
//     } }
//     packagePinsWithPackageIOs drop 1 foreach { case (pin, io) => {
//       shell.xdc.addPullup(io)
//     } }
//   } }
// }
// class SDIONexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: SPIShellInput)(implicit val valName: ValName)
//   extends SPIShellPlacer[NexysVideoShellBasicOverlays] {
//   def place(designInput: SPIDesignInput) = new SDIONexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
// }

class SPIFlashNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: SPIFlashDesignInput, val shellInput: SPIFlashShellInput)
  extends SPIFlashXilinxPlacedOverlay(name, designInput, shellInput)
{

  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(("L12", IOPin(io.qspi_sck)),
      ("T19", IOPin(io.qspi_cs)),
      ("P22", IOPin(io.qspi_dq(0))),
      ("R22", IOPin(io.qspi_dq(1))),
      ("P21", IOPin(io.qspi_dq(2))),
      ("R21", IOPin(io.qspi_dq(3))))

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
    } }
    packagePinsWithPackageIOs drop 1 foreach { case (pin, io) => {
      shell.xdc.addPullup(io)
    } }
  } }
}
class SPIFlashNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: SPIFlashShellInput)(implicit val valName: ValName)
  extends SPIFlashShellPlacer[NexysVideoShellBasicOverlays] {
  def place(designInput: SPIFlashDesignInput) = new SPIFlashNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
}

// class TracePMODNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: TracePMODDesignInput, val shellInput: TracePMODShellInput)
//   extends TracePMODXilinxPlacedOverlay(name, designInput, shellInput, packagePins = Seq("U12", "V12", "V10", "V11", "U14", "V14", "T13", "U13"))
// class TracePMODNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: TracePMODShellInput)(implicit val valName: ValName)
//   extends TracePMODShellPlacer[NexysVideoShellBasicOverlays] {
//   def place(designInput: TracePMODDesignInput) = new TracePMODNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
// }

// class GPIOPMODNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: GPIOPMODDesignInput, val shellInput: GPIOPMODShellInput)
//   extends GPIOPMODXilinxPlacedOverlay(name, designInput, shellInput)
// {
//   shell { InModuleBody {
//     val packagePinsWithPackageIOs = Seq(("E15", IOPin(io.gpio_pmod_0)), //These are PMOD B
//       ("E16", IOPin(io.gpio_pmod_1)),
//       ("D15", IOPin(io.gpio_pmod_2)),
//       ("C15", IOPin(io.gpio_pmod_3)),
//       ("J17", IOPin(io.gpio_pmod_4)),
//       ("J18", IOPin(io.gpio_pmod_5)),
//       ("K15", IOPin(io.gpio_pmod_6)),
//       ("J15", IOPin(io.gpio_pmod_7)))

//     packagePinsWithPackageIOs foreach { case (pin, io) => {
//       shell.xdc.addPackagePin(io, pin)
//       shell.xdc.addIOStandard(io, "LVCMOS33")
//     } }
//     packagePinsWithPackageIOs drop 7 foreach { case (pin, io) => {
//       shell.xdc.addPullup(io)
//     } }
//   } }
// }
// class GPIOPMODNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: GPIOPMODShellInput)(implicit val valName: ValName)
//   extends GPIOPMODShellPlacer[NexysVideoShellBasicOverlays] {
//   def place(designInput: GPIOPMODDesignInput) = new GPIOPMODNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
// }

class UARTNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: UARTDesignInput, val shellInput: UARTShellInput)
  extends UARTXilinxPlacedOverlay(name, designInput, shellInput, false)
{
  shell { InModuleBody {
    val packagePinsWithPackageIOs = Seq(("V18", IOPin(io.rxd)),
      ("AA19", IOPin(io.txd)))

    packagePinsWithPackageIOs foreach { case (pin, io) => {
      shell.xdc.addPackagePin(io, pin)
      shell.xdc.addIOStandard(io, "LVCMOS33")
      shell.xdc.addIOB(io)
    } }
  } }
}
class UARTNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: UARTShellInput)(implicit val valName: ValName)
  extends UARTShellPlacer[NexysVideoShellBasicOverlays] {
  def place(designInput: UARTDesignInput) = new UARTNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
}

//LEDS - 8 normal leds_
object LEDNexysVideoPinConstraints{
  val pins = Seq("T14", "T15", "T16", "U16", "V15", "W16", "W15", "Y13")
}
class LEDNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: LEDDesignInput, val shellInput: LEDShellInput)
  extends LEDXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(LEDNexysVideoPinConstraints.pins(shellInput.number)))
class LEDNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: LEDShellInput)(implicit val valName: ValName)
  extends LEDShellPlacer[NexysVideoShellBasicOverlays] {
  def place(designInput: LEDDesignInput) = new LEDNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
}

//SWs
// object SwitchNexysVideoPinConstraints{
//   val pins = Seq("A8", "C11", "C10", "A10")
// }
// class SwitchNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: SwitchDesignInput, val shellInput: SwitchShellInput)
//   extends SwitchXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(SwitchNexysVideoPinConstraints.pins(shellInput.number)))
// class SwitchNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: SwitchShellInput)(implicit val valName: ValName)
//   extends SwitchShellPlacer[NexysVideoShellBasicOverlays] {
//   def place(designInput: SwitchDesignInput) = new SwitchNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
// }

//Buttons
// object ButtonNexysVideoPinConstraints {
//   val pins = Seq("D9", "C9", "B9", "B8")
// }
// class ButtonNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: ButtonDesignInput, val shellInput: ButtonShellInput)
//   extends ButtonXilinxPlacedOverlay(name, designInput, shellInput, packagePin = Some(ButtonNexysVideoPinConstraints.pins(shellInput.number)))
// class ButtonNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: ButtonShellInput)(implicit val valName: ValName)
//   extends ButtonShellPlacer[NexysVideoShellBasicOverlays] {
//   def place(designInput: ButtonDesignInput) = new ButtonNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
// }

class JTAGDebugBScanNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: JTAGDebugBScanDesignInput, val shellInput: JTAGDebugBScanShellInput)
 extends JTAGDebugBScanXilinxPlacedOverlay(name, designInput, shellInput)
class JTAGDebugBScanNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: JTAGDebugBScanShellInput)(implicit val valName: ValName)
  extends JTAGDebugBScanShellPlacer[NexysVideoShellBasicOverlays] {
  def place(designInput: JTAGDebugBScanDesignInput) = new JTAGDebugBScanNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
}

// PMOD JD used for JTAG
// class JTAGDebugNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: JTAGDebugDesignInput, val shellInput: JTAGDebugShellInput)
//   extends JTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
// {
//   shell { InModuleBody {
//     shell.sdc.addClock("JTCK", IOPin(io.jtag_TCK), 10)
//     shell.sdc.addGroup(clocks = Seq("JTCK"))
//     shell.xdc.clockDedicatedRouteFalse(IOPin(io.jtag_TCK))
//     val packagePinsWithPackageIOs = Seq(("F4", IOPin(io.jtag_TCK)),  //pin JD-3
//       ("D2", IOPin(io.jtag_TMS)),  //pin JD-8
//       ("E2", IOPin(io.jtag_TDI)),  //pin JD-7
//       ("D4", IOPin(io.jtag_TDO)),  //pin JD-1
//       ("H2", IOPin(io.srst_n)))

//     packagePinsWithPackageIOs foreach { case (pin, io) => {
//       shell.xdc.addPackagePin(io, pin)
//       shell.xdc.addIOStandard(io, "LVCMOS33")
//       shell.xdc.addPullup(io)
//     } }
//   } }
// }
// class JTAGDebugNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: JTAGDebugShellInput)(implicit val valName: ValName)
//   extends JTAGDebugShellPlacer[NexysVideoShellBasicOverlays] {
//   def place(designInput: JTAGDebugDesignInput) = new JTAGDebugNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
// }

//cjtag
// class cJTAGDebugNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: cJTAGDebugDesignInput, val shellInput: cJTAGDebugShellInput)
//   extends cJTAGDebugXilinxPlacedOverlay(name, designInput, shellInput)
// {
//   shell { InModuleBody {
//     shell.sdc.addClock("JTCKC", IOPin(io.cjtag_TCKC), 10)
//     shell.sdc.addGroup(clocks = Seq("JTCKC"))
//     shell.xdc.clockDedicatedRouteFalse(IOPin(io.cjtag_TCKC))
//     val packagePinsWithPackageIOs = Seq(("F4", IOPin(io.cjtag_TCKC)),  //pin JD-3
//       ("D2", IOPin(io.cjtag_TMSC)),  //pin JD-8
//       ("H2", IOPin(io.srst_n)))

//     packagePinsWithPackageIOs foreach { case (pin, io) => {
//       shell.xdc.addPackagePin(io, pin)
//       shell.xdc.addIOStandard(io, "LVCMOS33")
//     } }
//       shell.xdc.addPullup(IOPin(io.cjtag_TCKC))
//       shell.xdc.addPullup(IOPin(io.srst_n))
//   } }
// }
// class cJTAGDebugNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: cJTAGDebugShellInput)(implicit val valName: ValName)
//   extends cJTAGDebugShellPlacer[NexysVideoShellBasicOverlays] {
//   def place(designInput: cJTAGDebugDesignInput) = new cJTAGDebugNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
// }

case object NexysVideoDDRSize extends Field[BigInt](0x20000000L * 1) // 512 MB
class DDRNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: DDRDesignInput, val shellInput: DDRShellInput)
  extends DDRPlacedOverlay[XilinxNexysVideoMIGPads](name, designInput, shellInput)
{
  val size = p(NexysVideoDDRSize)

  val ddrClk1 = shell { ClockSinkNode(freqMHz = 100)}
  val ddrClk2 = shell { ClockSinkNode(freqMHz = 200)}
  val ddrGroup = shell { ClockGroup() }
  ddrClk1 := di.wrangler := ddrGroup := di.corePLL
  ddrClk2 := di.wrangler := ddrGroup
  
  val migParams = XilinxNexysVideoMIGParams(address = AddressSet.misaligned(di.baseAddress, size))
  val mig = LazyModule(new XilinxNexysVideoMIG(migParams))
  val ioNode = BundleBridgeSource(() => mig.module.io.cloneType)
  val topIONode = shell { ioNode.makeSink() }
  val ddrUI     = shell { ClockSourceNode(freqMHz = 100) }
  val areset    = shell { ClockSinkNode(Seq(ClockSinkParameters())) }
  areset := di.wrangler := ddrUI

  def overlayOutput = DDROverlayOutput(ddr = mig.node)
  def ioFactory = new XilinxNexysVideoMIGPads(size)

  InModuleBody { ioNode.bundle <> mig.module.io }

  shell { InModuleBody {
    require (shell.sys_clock.get.isDefined, "Use of DDRNexysVideoPlacedOverlay depends on SysClockNexysVideoPlacedOverlay")
    val (sys, _) = shell.sys_clock.get.get.overlayOutput.node.out(0)
    val (ui, _) = ddrUI.out(0)
    val (dclk1, _) = ddrClk1.in(0)
    val (dclk2, _) = ddrClk2.in(0)
    val (ar, _) = areset.in(0)
    val port = topIONode.bundle.port
    
    io <> port
    ui.clock := port.ui_clk
    ui.reset := !port.mmcm_locked || port.ui_clk_sync_rst
    port.sys_clk_i := dclk1.clock.asUInt
    port.clk_ref_i := dclk2.clock.asUInt
    port.sys_rst := shell.pllReset
    port.aresetn := !ar.reset
  } }

  shell.sdc.addGroup(clocks = Seq("clk_pll_i"), pins = Seq(mig.island.module.blackbox.io.ui_clk))
}
class DDRNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: DDRShellInput)(implicit val valName: ValName)
  extends DDRShellPlacer[NexysVideoShellBasicOverlays] {
  def place(designInput: DDRDesignInput) = new DDRNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
}

//Core to shell external resets
class CTSResetNexysVideoPlacedOverlay(val shell: NexysVideoShellBasicOverlays, name: String, val designInput: CTSResetDesignInput, val shellInput: CTSResetShellInput)
  extends CTSResetPlacedOverlay(name, designInput, shellInput)
class CTSResetNexysVideoShellPlacer(val shell: NexysVideoShellBasicOverlays, val shellInput: CTSResetShellInput)(implicit val valName: ValName)
  extends CTSResetShellPlacer[NexysVideoShellBasicOverlays] {
  def place(designInput: CTSResetDesignInput) = new CTSResetNexysVideoPlacedOverlay(shell, valName.name, designInput, shellInput)
}


abstract class NexysVideoShellBasicOverlays()(implicit p: Parameters) extends Series7Shell {
  // Order matters; ddr depends on sys_clock
  val sys_clock = Overlay(ClockInputOverlayKey, new SysClockNexysVideoShellPlacer(this, ClockInputShellInput()))
  val led       = Seq.tabulate(8)(i => Overlay(LEDOverlayKey, new LEDNexysVideoShellPlacer(this, LEDMetas(i))(valName = ValName(s"led_$i"))))
//   val switch    = Seq.tabulate(4)(i => Overlay(SwitchOverlayKey, new SwitchNexysVideoShellPlacer(this, SwitchShellInput(number = i))(valName = ValName(s"switch_$i"))))
//   val button    = Seq.tabulate(4)(i => Overlay(ButtonOverlayKey, new ButtonNexysVideoShellPlacer(this, ButtonShellInput(number = i))(valName = ValName(s"button_$i"))))
  val ddr       = Overlay(DDROverlayKey, new DDRNexysVideoShellPlacer(this, DDRShellInput()))
  val uart      = Overlay(UARTOverlayKey, new UARTNexysVideoShellPlacer(this, UARTShellInput()))
//   val sdio      = Overlay(SPIOverlayKey, new SDIONexysVideoShellPlacer(this, SPIShellInput()))
//   val jtag      = Overlay(JTAGDebugOverlayKey, new JTAGDebugNexysVideoShellPlacer(this, JTAGDebugShellInput()))
//   val cjtag     = Overlay(cJTAGDebugOverlayKey, new cJTAGDebugNexysVideoShellPlacer(this, cJTAGDebugShellInput()))
  val spi_flash = Overlay(SPIFlashOverlayKey, new SPIFlashNexysVideoShellPlacer(this, SPIFlashShellInput()))
  val cts_reset = Overlay(CTSResetOverlayKey, new CTSResetNexysVideoShellPlacer(this, CTSResetShellInput()))
  val jtagBScan = Overlay(JTAGDebugBScanOverlayKey, new JTAGDebugBScanNexysVideoShellPlacer(this, JTAGDebugBScanShellInput()))

  def LEDMetas(i: Int): LEDShellInput =
    LEDShellInput(
      color = "red",
    //   rgb = (i < 12),
      number = i)
}

class NexysVideoShell()(implicit p: Parameters) extends NexysVideoShellBasicOverlays
{
  val resetPin = InModuleBody { Wire(Bool()) }
  // PLL reset causes
  val pllReset = InModuleBody { Wire(Bool()) }

  val topDesign = LazyModule(p(DesignKey)(designParameters))

  // Place the sys_clock at the Shell if the user didn't ask for it
  p(ClockInputOverlayKey).foreach(_.place(ClockInputDesignInput()))
  override lazy val module = new Impl
  class Impl extends LazyRawModuleImp(this) {

    val reset = IO(Input(Bool()))
    xdc.addBoardPin(reset, "reset")

    val reset_ibuf = Module(new IBUF)
    reset_ibuf.io.I := reset
    val sysclk: Clock = sys_clock.get() match {
      case Some(x: SysClockNexysVideoPlacedOverlay) => x.clock
    }
    val powerOnReset = PowerOnResetFPGAOnly(sysclk)
    sdc.addAsyncPath(Seq(powerOnReset))

    resetPin := reset_ibuf.io.O

    pllReset :=
      (!reset_ibuf.io.O) || powerOnReset //Arty100T is active low reset
  }
}

// class NexysVideoShellGPIOPMOD()(implicit p: Parameters) extends NexysVideoShellBasicOverlays
// //This is the Shell used for coreip arty builds, with GPIOS and trace signals on the pmods
// {
//   // PLL reset causes
//   val pllReset = InModuleBody { Wire(Bool()) }

//   val gpio_pmod = Overlay(GPIOPMODOverlayKey, new GPIOPMODNexysVideoShellPlacer(this, GPIOPMODShellInput()))
//   val trace_pmod = Overlay(TracePMODOverlayKey, new TracePMODNexysVideoShellPlacer(this, TracePMODShellInput()))

//   val topDesign = LazyModule(p(DesignKey)(designParameters))

//   // Place the sys_clock at the Shell if the user didn't ask for it
//   p(ClockInputOverlayKey).foreach(_.place(ClockInputDesignInput()))

//   override lazy val module = new LazyRawModuleImp(this) {
//     val reset = IO(Input(Bool()))
//     xdc.addBoardPin(reset, "reset")

//     val reset_ibuf = Module(new IBUF)
//     reset_ibuf.io.I := reset

//     val sysclk: Clock = sys_clock.get() match {
//       case Some(x: SysClockNexysVideoPlacedOverlay) => x.clock
//     }
//     val powerOnReset = PowerOnResetFPGAOnly(sysclk)
//     sdc.addAsyncPath(Seq(powerOnReset))
//     val ctsReset: Bool = cts_reset.get() match {
//       case Some(x: CTSResetNexysVideoPlacedOverlay) => x.designInput.rst
//       case None => false.B
//     }

//     pllReset :=
//       (!reset_ibuf.io.O) || powerOnReset || ctsReset //NexysVideo is active low reset
//   }
// }

/*
   Copyright 2016 SiFive, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
