// See LICENSE for license details.
package chipyard.fpga.nexys_video

import org.chipsalliance.cde.config._
import freechips.rocketchip.subsystem._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.system._
import freechips.rocketchip.tile._

import sifive.blocks.devices.uart._
import sifive.fpgashells.shell.{DesignKey}

import testchipip.{SerialTLKey}

import chipyard.{BuildSystem}

// don't use FPGAShell's DesignKey
class WithNoDesignKey extends Config((site, here, up) => {
  case DesignKey => (p: Parameters) => new SimpleLazyModule()(p)
})

class WithAdditions extends Config(
  new WithNexysVideoSystem)

class WithNexysVideoTweaks extends Config(
  new WithNexysVideoUARTTSI ++
  new WithNexysVideoDDRTL ++
  new WithNoDesignKey ++
  new chipyard.config.WithNoDebug ++ // no jtag
  new chipyard.config.WithNoUART ++ // use UART for the UART-TSI thing instad
  new chipyard.config.WithTLBackingMemory ++ // FPGA-shells converts the AXI to TL for us
  new freechips.rocketchip.subsystem.WithExtMemSize(BigInt(512) << 20) ++ // 512mb on NexysVideo
  new freechips.rocketchip.subsystem.WithoutTLMonitors)

class RocketNexysVideoConfig extends Config(
//   new WithAdditions ++
  new WithNexysVideoTweaks ++
  new chipyard.config.WithMemoryBusFrequency(50.0) ++
  new chipyard.config.WithPeripheryBusFrequency(50.0) ++  // Match the sbus and pbus frequency
  new chipyard.config.WithBroadcastManager ++ // no l2
  new chipyard.RocketConfig)

class MedRocketNexysVideoConfig extends Config(
//   new WithAdditions ++
  new WithNexysVideoTweaks ++
//   new chipyard.example.WithAxi4Dma(0x88000000L, 0x1000L) ++   //
//   new chipyard.example.WithAxi4Dma ++   //
  new chipyard.config.WithMemoryBusFrequency(50.0) ++
  new chipyard.config.WithPeripheryBusFrequency(50.0) ++  // Match the sbus and pbus frequency
  new chipyard.config.WithBroadcastManager ++ // no l2
//   new chipyard.example.WithStreamingFIR ++
//   new chipyard.example.WithStreamingPassthrough ++
  new chipyard.example.WithGCD(useAXI4=false, useBlackBox=false) ++    
  new chipyard.example.WithInitZero(0x88000000L, 0x1000L) ++   // add InitZero
  new chipyard.MedRocketConfig)


class SmallRocketNexysVideoConfig extends Config(
  new WithNexysVideoTweaks ++
  new chipyard.config.WithMemoryBusFrequency(50.0) ++
  new chipyard.config.WithPeripheryBusFrequency(50.0) ++  // Match the sbus and pbus frequency
  new chipyard.config.WithBroadcastManager ++ // no l2
  new chipyard.SmallRocketConfig)

class TinyRocketNexysVideoConfig extends Config(
  new WithNexysVideoTweaks ++
  new chipyard.config.WithMemoryBusFrequency(50.0) ++
  new chipyard.config.WithPeripheryBusFrequency(50.0) ++  // Match the sbus and pbus frequency
  new chipyard.config.WithBroadcastManager ++ // no l2
  new chipyard.TinyRocketConfig)

class UART230400RocketNexysVideoConfig extends Config(
  new WithNexysVideoUARTTSI(uartBaudRate = 230400) ++
  new RocketNexysVideoConfig)

class UART460800RocketNexysVideoConfig extends Config(
  new WithNexysVideoUARTTSI(uartBaudRate = 460800) ++
  new RocketNexysVideoConfig)

class UART921600RocketNexysVideoConfig extends Config(
  new WithNexysVideoUARTTSI(uartBaudRate = 921600) ++
  new RocketNexysVideoConfig)


class NoCoresNexysVideoConfig extends Config(
  new WithNexysVideoTweaks ++
  new chipyard.config.WithMemoryBusFrequency(50.0) ++
  new chipyard.config.WithPeripheryBusFrequency(50.0) ++  // Match the sbus and pbus frequency
  new chipyard.config.WithBroadcastManager ++ // no l2
  new chipyard.NoCoresConfig)

class WithNexysVideoSystem extends Config((site, here, up) => {
  case BuildSystem => (p: Parameters) => new NexysVideoDigitalTop()(p) // use the NexysVideo digital top
})