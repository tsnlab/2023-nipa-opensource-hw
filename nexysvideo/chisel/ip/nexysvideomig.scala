package sifive.fpgashells.ip.xilinx.nexysvideomig

import Chisel._
import chisel3.experimental.{Analog,attach}
import freechips.rocketchip.util.{ElaborationArtefacts}
import freechips.rocketchip.util.GenericParameterizedBundle
import org.chipsalliance.cde.config._

// Black Box

class NexysVideoMIGIODDR(depth : BigInt) extends GenericParameterizedBundle(depth) {
  require((depth<=0x20000000L),"NexysVideoMIGIODDR supports upto 512 MB depth configuraton")
  val ddr3_addr             = Bits(OUTPUT,15)
  val ddr3_ba               = Bits(OUTPUT,3)
  val ddr3_ras_n            = Bool(OUTPUT)
  val ddr3_cas_n            = Bool(OUTPUT)
  val ddr3_we_n             = Bool(OUTPUT)
  val ddr3_reset_n          = Bool(OUTPUT)
  val ddr3_ck_p             = Bits(OUTPUT,1)
  val ddr3_ck_n             = Bits(OUTPUT,1)
  val ddr3_cke              = Bits(OUTPUT,1)
//   val ddr3_cs_n             = Bits(OUTPUT,1)
  val ddr3_dm               = Bits(OUTPUT,2)
  val ddr3_odt              = Bits(OUTPUT,1)

  val ddr3_dq               = Analog(16.W)
  val ddr3_dqs_n            = Analog(2.W)
  val ddr3_dqs_p            = Analog(2.W)
}

trait NexysVideoMIGIOClocksReset extends Bundle {
  //inputs
  //"NO_BUFFER" clock source (must be connected to IBUF outside of IP)
  val sys_clk_i             = Bool(INPUT)
  val clk_ref_i             = Bool(INPUT)
  //user interface signals
  val ui_clk                = Clock(OUTPUT)
  val ui_clk_sync_rst       = Bool(OUTPUT)
  val ui_addn_clk_0         = Bool(OUTPUT)
  val mmcm_locked           = Bool(OUTPUT)
  val aresetn               = Bool(INPUT)
  //misc
  val init_calib_complete   = Bool(OUTPUT)
  val sys_rst               = Bool(INPUT)
}

//scalastyle:off
//turn off linter: blackbox name must match verilog module
class nexysvideomig(depth : BigInt)(implicit val p:Parameters) extends BlackBox
{
  require((depth<=0x20000000L),"nexysvideomig supports upto 512 MB depth configuraton")

  val io = new NexysVideoMIGIODDR(depth) with NexysVideoMIGIOClocksReset {
    // User interface signals
    val app_sr_req            = Bool(INPUT)
    val app_ref_req           = Bool(INPUT)
    val app_zq_req            = Bool(INPUT)
    val app_sr_active         = Bool(OUTPUT)
    val app_ref_ack           = Bool(OUTPUT)
    val app_zq_ack            = Bool(OUTPUT)
    //axi_s
    //slave interface write address ports
    val s_axi_awid            = Bits(INPUT,4)
    val s_axi_awaddr          = Bits(INPUT,if(depth<=0x40000000) 30 else 32)
    val s_axi_awlen           = Bits(INPUT,8)
    val s_axi_awsize          = Bits(INPUT,3)
    val s_axi_awburst         = Bits(INPUT,2)
    val s_axi_awlock          = Bits(INPUT,1)
    val s_axi_awcache         = Bits(INPUT,4)
    val s_axi_awprot          = Bits(INPUT,3)
    val s_axi_awqos           = Bits(INPUT,4)
    val s_axi_awvalid         = Bool(INPUT)
    val s_axi_awready         = Bool(OUTPUT)
    //slave interface write data ports
    val s_axi_wdata           = Bits(INPUT,64)
    val s_axi_wstrb           = Bits(INPUT,8)
    val s_axi_wlast           = Bool(INPUT)
    val s_axi_wvalid          = Bool(INPUT)
    val s_axi_wready          = Bool(OUTPUT)
    //slave interface write response ports
    val s_axi_bready          = Bool(INPUT)
    val s_axi_bid             = Bits(OUTPUT,4)
    val s_axi_bresp           = Bits(OUTPUT,2)
    val s_axi_bvalid          = Bool(OUTPUT)
    //slave interface read address ports
    val s_axi_arid            = Bits(INPUT,4)
    val s_axi_araddr          = Bits(INPUT,if(depth<=0x40000000) 30 else 32)
    val s_axi_arlen           = Bits(INPUT,8)
    val s_axi_arsize          = Bits(INPUT,3)
    val s_axi_arburst         = Bits(INPUT,2)
    val s_axi_arlock          = Bits(INPUT,1)
    val s_axi_arcache         = Bits(INPUT,4)
    val s_axi_arprot          = Bits(INPUT,3)
    val s_axi_arqos           = Bits(INPUT,4)
    val s_axi_arvalid         = Bool(INPUT)
    val s_axi_arready         = Bool(OUTPUT)
    //slave interface read data ports
    val s_axi_rready          = Bool(INPUT)
    val s_axi_rid             = Bits(OUTPUT,4)
    val s_axi_rdata           = Bits(OUTPUT,64)
    val s_axi_rresp           = Bits(OUTPUT,2)
    val s_axi_rlast           = Bool(OUTPUT)
    val s_axi_rvalid          = Bool(OUTPUT)
    //misc
    val device_temp           = Bits(OUTPUT,12)
  }


  val migprj = """{<?xml version='1.0' encoding='UTF-8'?>
<!-- IMPORTANT: This is an internal file that has been generated by the MIG software. Any direct editing or changes made to this file may result in unpredictable behavior or data corruption. It is strongly advised that users do not edit the contents of this file. Re-run the MIG GUI with the required settings if any of the options provided below need to be altered. -->
<Project NoOfControllers="1" >
    <ModuleName>design_1_mig_7series_0_0</ModuleName>
    <dci_inouts_inputs>1</dci_inouts_inputs>
    <dci_inputs>1</dci_inputs>
    <Debug_En>OFF</Debug_En>
    <DataDepth_En>1024</DataDepth_En>
    <LowPower_En>ON</LowPower_En>
    <XADC_En>Enabled</XADC_En>
    <TargetFPGA>xc7a200t-sbg484/-1</TargetFPGA>
    <Version>4.1</Version>
    <SystemClock>No Buffer</SystemClock>
    <ReferenceClock>No Buffer</ReferenceClock>
    <SysResetPolarity>ACTIVE HIGH</SysResetPolarity>
    <BankSelectionFlag>FALSE</BankSelectionFlag>
    <InternalVref>1</InternalVref>
    <dci_hr_inouts_inputs>50 Ohms</dci_hr_inouts_inputs>
    <dci_cascade>0</dci_cascade>
    <Controller number="0" >
        <MemoryDevice>DDR3_SDRAM/Components/MT41K256M16XX-125</MemoryDevice>
        <TimePeriod>2500</TimePeriod>
        <VccAuxIO>1.8V</VccAuxIO>
        <PHYRatio>4:1</PHYRatio>
        <InputClkFreq>100</InputClkFreq>
        <UIExtraClocks>1</UIExtraClocks>
        <MMCM_VCO>800</MMCM_VCO>
        <MMCMClkOut0> 4.000</MMCMClkOut0>
        <MMCMClkOut1>1</MMCMClkOut1>
        <MMCMClkOut2>1</MMCMClkOut2>
        <MMCMClkOut3>1</MMCMClkOut3>
        <MMCMClkOut4>1</MMCMClkOut4>
        <DataWidth>16</DataWidth>
        <DeepMemory>1</DeepMemory>
        <DataMask>1</DataMask>
        <ECC>Disabled</ECC>
        <Ordering>Normal</Ordering>
        <CustomPart>FALSE</CustomPart>
        <NewPartName></NewPartName>
        <RowAddress>15</RowAddress>
        <ColAddress>10</ColAddress>
        <BankAddress>3</BankAddress>
        <MemoryVoltage>1.5V</MemoryVoltage>
        <C0_MEM_SIZE>536870912</C0_MEM_SIZE>
        <UserMemoryAddressMap>BANK_ROW_COLUMN</UserMemoryAddressMap>
        <PinSelection>
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="M2" SLEW="" name="ddr3_addr[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="L5" SLEW="" name="ddr3_addr[10]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="N5" SLEW="" name="ddr3_addr[11]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="N4" SLEW="" name="ddr3_addr[12]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="P2" SLEW="" name="ddr3_addr[13]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="P6" SLEW="" name="ddr3_addr[14]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="M5" SLEW="" name="ddr3_addr[1]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="M3" SLEW="" name="ddr3_addr[2]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="M1" SLEW="" name="ddr3_addr[3]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="L6" SLEW="" name="ddr3_addr[4]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="P1" SLEW="" name="ddr3_addr[5]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="N3" SLEW="" name="ddr3_addr[6]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="N2" SLEW="" name="ddr3_addr[7]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="M6" SLEW="" name="ddr3_addr[8]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="R1" SLEW="" name="ddr3_addr[9]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="L3" SLEW="" name="ddr3_ba[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="K6" SLEW="" name="ddr3_ba[1]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="L4" SLEW="" name="ddr3_ba[2]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="K3" SLEW="" name="ddr3_cas_n" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="DIFF_SSTL15" PADName="P4" SLEW="" name="ddr3_ck_n[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="DIFF_SSTL15" PADName="P5" SLEW="" name="ddr3_ck_p[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="J6" SLEW="" name="ddr3_cke[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="G3" SLEW="" name="ddr3_dm[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="F1" SLEW="" name="ddr3_dm[1]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="G2" SLEW="" name="ddr3_dq[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="F3" SLEW="" name="ddr3_dq[10]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="D2" SLEW="" name="ddr3_dq[11]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="C2" SLEW="" name="ddr3_dq[12]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="A1" SLEW="" name="ddr3_dq[13]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="E2" SLEW="" name="ddr3_dq[14]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="B1" SLEW="" name="ddr3_dq[15]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="H4" SLEW="" name="ddr3_dq[1]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="H5" SLEW="" name="ddr3_dq[2]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="J1" SLEW="" name="ddr3_dq[3]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="K1" SLEW="" name="ddr3_dq[4]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="H3" SLEW="" name="ddr3_dq[5]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="H2" SLEW="" name="ddr3_dq[6]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="J5" SLEW="" name="ddr3_dq[7]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="E3" SLEW="" name="ddr3_dq[8]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="B2" SLEW="" name="ddr3_dq[9]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="DIFF_SSTL15" PADName="J2" SLEW="" name="ddr3_dqs_n[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="DIFF_SSTL15" PADName="D1" SLEW="" name="ddr3_dqs_n[1]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="DIFF_SSTL15" PADName="K2" SLEW="" name="ddr3_dqs_p[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="DIFF_SSTL15" PADName="E1" SLEW="" name="ddr3_dqs_p[1]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="K4" SLEW="" name="ddr3_odt[0]" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="J4" SLEW="" name="ddr3_ras_n" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="LVCMOS15" PADName="G1" SLEW="" name="ddr3_reset_n" IN_TERM="" />
            <Pin VCCAUX_IO="" IOSTANDARD="SSTL15" PADName="L1" SLEW="" name="ddr3_we_n" IN_TERM="" />
        </PinSelection>
        <System_Control>
            <Pin PADName="No connect" Bank="Select Bank" name="sys_rst" />
            <Pin PADName="No connect" Bank="Select Bank" name="init_calib_complete" />
            <Pin PADName="No connect" Bank="Select Bank" name="tg_compare_error" />
        </System_Control>
        <TimingParameters>
            <Parameters twtr="7.5" trrd="7.5" trefi="7.8" tfaw="40" trtp="7.5" tcke="5" trfc="260" trp="13.75" tras="35" trcd="13.75" />
        </TimingParameters>
        <mrBurstLength name="Burst Length" >8 - Fixed</mrBurstLength>
        <mrBurstType name="Read Burst Type and Length" >Sequential</mrBurstType>
        <mrCasLatency name="CAS Latency" >6</mrCasLatency>
        <mrMode name="Mode" >Normal</mrMode>
        <mrDllReset name="DLL Reset" >No</mrDllReset>
        <mrPdMode name="DLL control for precharge PD" >Slow Exit</mrPdMode>
        <emrDllEnable name="DLL Enable" >Enable</emrDllEnable>
        <emrOutputDriveStrength name="Output Driver Impedance Control" >RZQ/7</emrOutputDriveStrength>
        <emrMirrorSelection name="Address Mirroring" >Disable</emrMirrorSelection>
        <emrCSSelection name="Controller Chip Select Pin" >Disable</emrCSSelection>
        <emrRTT name="RTT (nominal) - On Die Termination (ODT)" >RZQ/6</emrRTT>
        <emrPosted name="Additive Latency (AL)" >0</emrPosted>
        <emrOCD name="Write Leveling Enable" >Disabled</emrOCD>
        <emrDQS name="TDQS enable" >Enabled</emrDQS>
        <emrRDQS name="Qoff" >Output Buffer Enabled</emrRDQS>
        <mr2PartialArraySelfRefresh name="Partial-Array Self Refresh" >Full Array</mr2PartialArraySelfRefresh>
        <mr2CasWriteLatency name="CAS write latency" >5</mr2CasWriteLatency>
        <mr2AutoSelfRefresh name="Auto Self Refresh" >Enabled</mr2AutoSelfRefresh>
        <mr2SelfRefreshTempRange name="High Temparature Self Refresh Rate" >Normal</mr2SelfRefreshTempRange>
        <mr2RTTWR name="RTT_WR - Dynamic On Die Termination (ODT)" >Dynamic ODT off</mr2RTTWR>
        <PortInterface>AXI</PortInterface>
        <AXIParameters>
            <C0_C_RD_WR_ARB_ALGORITHM>RD_PRI_REG</C0_C_RD_WR_ARB_ALGORITHM>
            <C0_S_AXI_ADDR_WIDTH>29</C0_S_AXI_ADDR_WIDTH>
            <C0_S_AXI_DATA_WIDTH>64</C0_S_AXI_DATA_WIDTH>
            <C0_S_AXI_ID_WIDTH>4</C0_S_AXI_ID_WIDTH>
            <C0_S_AXI_SUPPORTS_NARROW_BURST>0</C0_S_AXI_SUPPORTS_NARROW_BURST>
        </AXIParameters>
    </Controller>
</Project> } """




  val migprjname = """{/nexysvideomig.prj}"""
  val modulename = """nexysvideomig"""

  ElaborationArtefacts.add(
    modulename++".vivado.tcl",
    """set migprj """++migprj++"""
   set migprjfile """++migprjname++"""
   set migprjfilepath $ipdir$migprjfile
   set fp [open $migprjfilepath w+]
   puts $fp $migprj
   close $fp
   create_ip -vendor xilinx.com -library ip -name mig_7series -module_name """ ++ modulename ++ """ -dir $ipdir -force
   set_property CONFIG.XML_INPUT_FILE $migprjfilepath [get_ips """ ++ modulename ++ """] """
  )


}

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
