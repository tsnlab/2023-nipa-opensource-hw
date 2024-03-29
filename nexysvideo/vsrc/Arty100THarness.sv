// Generated by CIRCT unknown git version
// Standard header to adapt well known macros to our needs.
`ifndef RANDOMIZE
  `ifdef RANDOMIZE_REG_INIT
    `define RANDOMIZE
  `endif // RANDOMIZE_REG_INIT
`endif // not def RANDOMIZE
`ifndef RANDOMIZE
  `ifdef RANDOMIZE_MEM_INIT
    `define RANDOMIZE
  `endif // RANDOMIZE_MEM_INIT
`endif // not def RANDOMIZE

// RANDOM may be set to an expression that produces a 32-bit random unsigned value.
`ifndef RANDOM
  `define RANDOM $random
`endif // not def RANDOM

// Users can define 'PRINTF_COND' to add an extra gate to prints.
`ifndef PRINTF_COND_
  `ifdef PRINTF_COND
    `define PRINTF_COND_ (`PRINTF_COND)
  `else  // PRINTF_COND
    `define PRINTF_COND_ 1
  `endif // PRINTF_COND
`endif // not def PRINTF_COND_

// Users can define 'ASSERT_VERBOSE_COND' to add an extra gate to assert error printing.
`ifndef ASSERT_VERBOSE_COND_
  `ifdef ASSERT_VERBOSE_COND
    `define ASSERT_VERBOSE_COND_ (`ASSERT_VERBOSE_COND)
  `else  // ASSERT_VERBOSE_COND
    `define ASSERT_VERBOSE_COND_ 1
  `endif // ASSERT_VERBOSE_COND
`endif // not def ASSERT_VERBOSE_COND_

// Users can define 'STOP_COND' to add an extra gate to stop conditions.
`ifndef STOP_COND_
  `ifdef STOP_COND
    `define STOP_COND_ (`STOP_COND)
  `else  // STOP_COND
    `define STOP_COND_ 1
  `endif // STOP_COND
`endif // not def STOP_COND_

// Users can define INIT_RANDOM as general code that gets injected into the
// initializer block for modules with registers.
`ifndef INIT_RANDOM
  `define INIT_RANDOM
`endif // not def INIT_RANDOM

// If using random initialization, you can also define RANDOMIZE_DELAY to
// customize the delay used, otherwise 0.002 is used.
`ifndef RANDOMIZE_DELAY
  `define RANDOMIZE_DELAY 0.002
`endif // not def RANDOMIZE_DELAY

// Define INIT_RANDOM_PROLOG_ for use in our modules below.
`ifndef INIT_RANDOM_PROLOG_
  `ifdef RANDOMIZE
    `ifdef VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM
    `else  // VERILATOR
      `define INIT_RANDOM_PROLOG_ `INIT_RANDOM #`RANDOMIZE_DELAY begin end
    `endif // VERILATOR
  `else  // RANDOMIZE
    `define INIT_RANDOM_PROLOG_
  `endif // RANDOMIZE
`endif // not def INIT_RANDOM_PROLOG_

module Arty100THarness(
  input         sys_clock,
  inout         uart_txd,
                uart_rxd,
  inout  [15:0] ddr_ddr3_dq,
  inout  [1:0]  ddr_ddr3_dqs_n,
                ddr_ddr3_dqs_p,
  input         reset,
  output [13:0] ddr_ddr3_addr,
  output [2:0]  ddr_ddr3_ba,
  output        ddr_ddr3_ras_n,
                ddr_ddr3_cas_n,
                ddr_ddr3_we_n,
                ddr_ddr3_reset_n,
                ddr_ddr3_ck_p,
                ddr_ddr3_ck_n,
                ddr_ddr3_cke,
 //               ddr_ddr3_cs_n,
  output [1:0]  ddr_ddr3_dm,
  output        ddr_ddr3_odt,
                led_0,
                led_1,
                led_2,
                led_3,
                led_4,
                led_5,
                led_6,
                led_7/*,
                led_8,
                led_9,
                led_10,
                led_11,
                led_12,
                led_13,
                led_14,
                led_15*/
);

  wire        _powerOnReset_fpga_power_on_power_on_reset;	// @[Xilinx.scala:104:21]
  wire        _reset_ibuf_O;	// @[Arty100TShell.scala:323:28]
  wire        _plusarg_reader_out;	// @[PlusArg.scala:80:11]
  wire        _serial_width_adapter_io_narrow_in_ready;	// @[HarnessBinders.scala:33:42]
  wire        _serial_width_adapter_io_narrow_out_valid;	// @[HarnessBinders.scala:33:42]
  wire [7:0]  _serial_width_adapter_io_narrow_out_bits;	// @[HarnessBinders.scala:33:42]
  wire        _serial_width_adapter_io_wide_in_ready;	// @[HarnessBinders.scala:33:42]
  wire        _serial_width_adapter_io_wide_out_valid;	// @[HarnessBinders.scala:33:42]
  wire [31:0] _serial_width_adapter_io_wide_out_bits;	// @[HarnessBinders.scala:33:42]
  wire        _uart_to_serial_io_uart_txd;	// @[HarnessBinders.scala:31:36]
  wire        _uart_to_serial_io_serial_in_ready;	// @[HarnessBinders.scala:31:36]
  wire        _uart_to_serial_io_serial_out_valid;	// @[HarnessBinders.scala:31:36]
  wire [7:0]  _uart_to_serial_io_serial_out_bits;	// @[HarnessBinders.scala:31:36]
  wire        _ram_io_ser_in_valid;	// @[SerialAdapter.scala:58:24]
  wire [31:0] _ram_io_ser_in_bits;	// @[SerialAdapter.scala:58:24]
  wire        _ram_io_ser_out_ready;	// @[SerialAdapter.scala:58:24]
  wire        _ram_io_tsi_ser_in_ready;	// @[SerialAdapter.scala:58:24]
  wire        _ram_io_tsi_ser_out_valid;	// @[SerialAdapter.scala:58:24]
  wire [31:0] _ram_io_tsi_ser_out_bits;	// @[SerialAdapter.scala:58:24]
  wire [3:0]  _ram_io_adapter_state;	// @[SerialAdapter.scala:58:24]
  wire        _bits_in_queue_io_enq_ready;	// @[SerialAdapter.scala:34:28]
  wire        _bits_in_queue_io_deq_valid;	// @[SerialAdapter.scala:34:28]
  wire [31:0] _bits_in_queue_io_deq_bits;	// @[SerialAdapter.scala:34:28]
  wire        _bits_out_queue_io_enq_ready;	// @[SerialAdapter.scala:28:29]
  wire        _bits_out_queue_io_deq_valid;	// @[SerialAdapter.scala:28:29]
  wire [31:0] _bits_out_queue_io_deq_bits;	// @[SerialAdapter.scala:28:29]
  wire        _bundleIn_0_rxd_a2b_b;	// @[Util.scala:30:21]
  wire        _sys_clock_ibufg_O;	// @[ClockOverlay.scala:32:22]
  wire        _harnessSysPLLNode_clk_out1;	// @[XilinxShell.scala:76:55]
  wire        _harnessSysPLLNode_clk_out2;	// @[XilinxShell.scala:76:55]
  wire        _harnessSysPLLNode_clk_out3;	// @[XilinxShell.scala:76:55]
  wire        _harnessSysPLLNode_locked;	// @[XilinxShell.scala:76:55]
  wire        _mig_auto_buffer_in_a_ready;	// @[Arty100TShell.scala:239:23]
  wire        _mig_auto_buffer_in_d_valid;	// @[Arty100TShell.scala:239:23]
  wire [2:0]  _mig_auto_buffer_in_d_bits_opcode;	// @[Arty100TShell.scala:239:23]
  wire [1:0]  _mig_auto_buffer_in_d_bits_param;	// @[Arty100TShell.scala:239:23]
  wire [2:0]  _mig_auto_buffer_in_d_bits_size;	// @[Arty100TShell.scala:239:23]
  wire [3:0]  _mig_auto_buffer_in_d_bits_source;	// @[Arty100TShell.scala:239:23]
  wire        _mig_auto_buffer_in_d_bits_sink;	// @[Arty100TShell.scala:239:23]
  wire        _mig_auto_buffer_in_d_bits_denied;	// @[Arty100TShell.scala:239:23]
  wire [63:0] _mig_auto_buffer_in_d_bits_data;	// @[Arty100TShell.scala:239:23]
  wire        _mig_auto_buffer_in_d_bits_corrupt;	// @[Arty100TShell.scala:239:23]
  wire        _mig_io_port_ui_clk;	// @[Arty100TShell.scala:239:23]
  wire        _mig_io_port_ui_clk_sync_rst;	// @[Arty100TShell.scala:239:23]
  wire        _mig_io_port_mmcm_locked;	// @[Arty100TShell.scala:239:23]
  wire        _mig_io_port_init_calib_complete;	// @[Arty100TShell.scala:239:23]
  wire        _ddrGroup_auto_out_1_clock;	// @[ClockGroup.scala:31:15]
  wire        _ddrGroup_auto_out_1_reset;	// @[ClockGroup.scala:31:15]
  wire        _ddrGroup_auto_out_0_clock;	// @[ClockGroup.scala:31:15]
  wire        _ddrGroup_auto_out_0_reset;	// @[ClockGroup.scala:31:15]
  wire        _dutGroup_auto_out_clock;	// @[ClockGroup.scala:31:15]
  wire        _dutGroup_auto_out_reset;	// @[ClockGroup.scala:31:15]
  wire        _dutWrangler_auto_out_3_reset;	// @[Harness.scala:31:31]
  wire        _dutWrangler_auto_out_2_clock;	// @[Harness.scala:31:31]
  wire        _dutWrangler_auto_out_1_clock;	// @[Harness.scala:31:31]
  wire        _dutWrangler_auto_out_0_clock;	// @[Harness.scala:31:31]
  wire        _dutWrangler_auto_out_0_reset;	// @[Harness.scala:31:31]
  wire        _chiptop_serial_tl_clock;	// @[Harness.scala:24:27]
  wire        _chiptop_serial_tl_bits_in_ready;	// @[Harness.scala:24:27]
  wire        _chiptop_serial_tl_bits_out_valid;	// @[Harness.scala:24:27]
  wire [31:0] _chiptop_serial_tl_bits_out_bits;	// @[Harness.scala:24:27]
  wire        _chiptop_tl_slave_0_a_valid;	// @[Harness.scala:24:27]
  wire [2:0]  _chiptop_tl_slave_0_a_bits_opcode;	// @[Harness.scala:24:27]
  wire [2:0]  _chiptop_tl_slave_0_a_bits_param;	// @[Harness.scala:24:27]
  wire [2:0]  _chiptop_tl_slave_0_a_bits_size;	// @[Harness.scala:24:27]
  wire [3:0]  _chiptop_tl_slave_0_a_bits_source;	// @[Harness.scala:24:27]
  wire [31:0] _chiptop_tl_slave_0_a_bits_address;	// @[Harness.scala:24:27]
  wire [7:0]  _chiptop_tl_slave_0_a_bits_mask;	// @[Harness.scala:24:27]
  wire [63:0] _chiptop_tl_slave_0_a_bits_data;	// @[Harness.scala:24:27]
  wire        _chiptop_tl_slave_0_a_bits_corrupt;	// @[Harness.scala:24:27]
  wire        _chiptop_tl_slave_0_d_ready;	// @[Harness.scala:24:27]
  reg  [1:0]  ddrBlockDuringReset_c_value;	// @[Counter.scala:61:40]
  reg         ddrBlockDuringReset_r;	// @[Reg.scala:35:20]
  reg  [1:0]  ddrBlockDuringReset_c_value_1;	// @[Counter.scala:61:40]
  reg         ddrBlockDuringReset_r_1;	// @[Reg.scala:35:20]
  reg  [25:0] counter;	// @[Harness.scala:67:28]
  reg  [1:0]  on;	// @[Harness.scala:68:23]
  wire        bundleIn_0_9 = on == 2'h2;	// @[Harness.scala:68:23, :69:60]
  wire        _WIRE_1 = ~_reset_ibuf_O | _powerOnReset_fpga_power_on_power_on_reset;	// @[Arty100TShell.scala:323:28, :334:{8,26}, Xilinx.scala:104:21]
  always @(posedge _dutWrangler_auto_out_0_clock) begin	// @[Harness.scala:31:31]
    if (_dutWrangler_auto_out_0_reset | ~_mig_io_port_init_calib_complete) begin	// @[Arty100TShell.scala:239:23, Harness.scala:31:31, :83:{55,58}]
      ddrBlockDuringReset_c_value <= 2'h0;	// @[Counter.scala:61:40]
      ddrBlockDuringReset_r <= 1'h0;	// @[LazyModule.scala:411:29, Reg.scala:35:20]
      ddrBlockDuringReset_c_value_1 <= 2'h0;	// @[Counter.scala:61:40]
      ddrBlockDuringReset_r_1 <= 1'h0;	// @[LazyModule.scala:411:29, Reg.scala:35:20]
    end
    else begin	// @[Harness.scala:31:31]
      ddrBlockDuringReset_c_value <= ddrBlockDuringReset_c_value + 2'h1;	// @[Counter.scala:61:40, :77:24, Harness.scala:69:60]
      ddrBlockDuringReset_r <= (&ddrBlockDuringReset_c_value) | ddrBlockDuringReset_r;	// @[Counter.scala:61:40, :73:24, Reg.scala:35:20, :36:{18,22}]
      ddrBlockDuringReset_c_value_1 <= ddrBlockDuringReset_c_value_1 + 2'h1;	// @[Counter.scala:61:40, :77:24, Harness.scala:69:60]
      ddrBlockDuringReset_r_1 <= (&ddrBlockDuringReset_c_value_1) | ddrBlockDuringReset_r_1;	// @[Counter.scala:61:40, :73:24, Reg.scala:35:20, :36:{18,22}]
    end
  end // always @(posedge)
  always @(posedge _sys_clock_ibufg_O) begin	// @[ClockOverlay.scala:32:22]
    if (counter == 26'h2155554)	// @[Harness.scala:67:28, :70:30]
      counter <= 26'h0;	// @[Harness.scala:67:28, :70:21]
    else	// @[Harness.scala:70:30]
      counter <= counter + 26'h1;	// @[Harness.scala:67:28, :70:61]
    if (counter == 26'h0) begin	// @[Harness.scala:67:28, :70:21, :71:21]
      if (bundleIn_0_9)	// @[Harness.scala:69:60]
        on <= 2'h0;	// @[Counter.scala:61:40, Harness.scala:68:23]
      else	// @[Harness.scala:69:60]
        on <= on + 2'h1;	// @[Harness.scala:68:23, :69:60, :72:58]
    end
  end // always @(posedge)
  `ifndef SYNTHESIS
    `ifdef FIRRTL_BEFORE_INITIAL
      `FIRRTL_BEFORE_INITIAL
    `endif // FIRRTL_BEFORE_INITIAL
    logic [31:0] _RANDOM_0;
    logic [31:0] _RANDOM_1;
    initial begin
      `ifdef INIT_RANDOM_PROLOG_
        `INIT_RANDOM_PROLOG_
      `endif // INIT_RANDOM_PROLOG_
      `ifdef RANDOMIZE_REG_INIT
        _RANDOM_0 = `RANDOM;
        _RANDOM_1 = `RANDOM;
        ddrBlockDuringReset_c_value = _RANDOM_0[1:0];	// @[Counter.scala:61:40]
        ddrBlockDuringReset_r = _RANDOM_0[2];	// @[Counter.scala:61:40, Reg.scala:35:20]
        ddrBlockDuringReset_c_value_1 = _RANDOM_0[4:3];	// @[Counter.scala:61:40]
        ddrBlockDuringReset_r_1 = _RANDOM_0[5];	// @[Counter.scala:61:40, Reg.scala:35:20]
        counter = _RANDOM_0[31:6];	// @[Counter.scala:61:40, Harness.scala:67:28]
        on = _RANDOM_1[1:0];	// @[Harness.scala:68:23]
      `endif // RANDOMIZE_REG_INIT
    end // initial
    `ifdef FIRRTL_AFTER_INITIAL
      `FIRRTL_AFTER_INITIAL
    `endif // FIRRTL_AFTER_INITIAL
  `endif // not def SYNTHESIS
  ChipTop chiptop (	// @[Harness.scala:24:27]
    .serial_tl_bits_in_valid   (_bits_in_queue_io_deq_valid),	// @[SerialAdapter.scala:34:28]
    .serial_tl_bits_in_bits    (_bits_in_queue_io_deq_bits),	// @[SerialAdapter.scala:34:28]
    .serial_tl_bits_out_ready  (_bits_out_queue_io_enq_ready),	// @[SerialAdapter.scala:28:29]
    .tl_slave_0_a_ready        (ddrBlockDuringReset_r & _mig_auto_buffer_in_a_ready),	// @[Arty100TShell.scala:239:23, Blockable.scala:33:18, :35:30, :37:20, Reg.scala:35:20]
    .tl_slave_0_d_valid        (ddrBlockDuringReset_r_1 & _mig_auto_buffer_in_d_valid),	// @[Arty100TShell.scala:239:23, Blockable.scala:32:18, :35:30, :36:20, Reg.scala:35:20]
    .tl_slave_0_d_bits_opcode  (_mig_auto_buffer_in_d_bits_opcode),	// @[Arty100TShell.scala:239:23]
    .tl_slave_0_d_bits_param   (_mig_auto_buffer_in_d_bits_param),	// @[Arty100TShell.scala:239:23]
    .tl_slave_0_d_bits_size    (_mig_auto_buffer_in_d_bits_size),	// @[Arty100TShell.scala:239:23]
    .tl_slave_0_d_bits_source  (_mig_auto_buffer_in_d_bits_source),	// @[Arty100TShell.scala:239:23]
    .tl_slave_0_d_bits_sink    (_mig_auto_buffer_in_d_bits_sink),	// @[Arty100TShell.scala:239:23]
    .tl_slave_0_d_bits_denied  (_mig_auto_buffer_in_d_bits_denied),	// @[Arty100TShell.scala:239:23]
    .tl_slave_0_d_bits_data    (_mig_auto_buffer_in_d_bits_data),	// @[Arty100TShell.scala:239:23]
    .tl_slave_0_d_bits_corrupt (_mig_auto_buffer_in_d_bits_corrupt),	// @[Arty100TShell.scala:239:23]
    .custom_boot               (_plusarg_reader_out),	// @[PlusArg.scala:80:11]
    .clock_clock               (_dutWrangler_auto_out_0_clock),	// @[Harness.scala:31:31]
    .reset                     (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .serial_tl_clock           (_chiptop_serial_tl_clock),
    .serial_tl_bits_in_ready   (_chiptop_serial_tl_bits_in_ready),
    .serial_tl_bits_out_valid  (_chiptop_serial_tl_bits_out_valid),
    .serial_tl_bits_out_bits   (_chiptop_serial_tl_bits_out_bits),
    .tl_slave_0_a_valid        (_chiptop_tl_slave_0_a_valid),
    .tl_slave_0_a_bits_opcode  (_chiptop_tl_slave_0_a_bits_opcode),
    .tl_slave_0_a_bits_param   (_chiptop_tl_slave_0_a_bits_param),
    .tl_slave_0_a_bits_size    (_chiptop_tl_slave_0_a_bits_size),
    .tl_slave_0_a_bits_source  (_chiptop_tl_slave_0_a_bits_source),
    .tl_slave_0_a_bits_address (_chiptop_tl_slave_0_a_bits_address),
    .tl_slave_0_a_bits_mask    (_chiptop_tl_slave_0_a_bits_mask),
    .tl_slave_0_a_bits_data    (_chiptop_tl_slave_0_a_bits_data),
    .tl_slave_0_a_bits_corrupt (_chiptop_tl_slave_0_a_bits_corrupt),
    .tl_slave_0_d_ready        (_chiptop_tl_slave_0_d_ready)
  );
  ResetWrangler dutWrangler (	// @[Harness.scala:31:31]
    .auto_in_3_clock  (_mig_io_port_ui_clk),	// @[Arty100TShell.scala:239:23]
    .auto_in_3_reset  (~_mig_io_port_mmcm_locked | _mig_io_port_ui_clk_sync_rst),	// @[Arty100TShell.scala:239:23, :262:{17,35}]
    .auto_in_2_clock  (_ddrGroup_auto_out_1_clock),	// @[ClockGroup.scala:31:15]
    .auto_in_2_reset  (_ddrGroup_auto_out_1_reset),	// @[ClockGroup.scala:31:15]
    .auto_in_1_clock  (_ddrGroup_auto_out_0_clock),	// @[ClockGroup.scala:31:15]
    .auto_in_1_reset  (_ddrGroup_auto_out_0_reset),	// @[ClockGroup.scala:31:15]
    .auto_in_0_clock  (_dutGroup_auto_out_clock),	// @[ClockGroup.scala:31:15]
    .auto_in_0_reset  (_dutGroup_auto_out_reset),	// @[ClockGroup.scala:31:15]
    .auto_out_3_reset (_dutWrangler_auto_out_3_reset),
    .auto_out_2_clock (_dutWrangler_auto_out_2_clock),
    .auto_out_1_clock (_dutWrangler_auto_out_1_clock),
    .auto_out_0_clock (_dutWrangler_auto_out_0_clock),
    .auto_out_0_reset (_dutWrangler_auto_out_0_reset)
  );
  ClockGroup_7 dutGroup (	// @[ClockGroup.scala:31:15]
    .auto_in_member_0_clock (_harnessSysPLLNode_clk_out1),	// @[XilinxShell.scala:76:55]
    .auto_in_member_0_reset (~_harnessSysPLLNode_locked | ~_reset_ibuf_O),	// @[Arty100TShell.scala:323:28, Harness.scala:60:56, PLLFactory.scala:78:{20,35}, XilinxShell.scala:76:55]
    .auto_out_clock         (_dutGroup_auto_out_clock),
    .auto_out_reset         (_dutGroup_auto_out_reset)
  );
  ClockGroup_8 ddrGroup (	// @[ClockGroup.scala:31:15]
    .auto_in_member_1_clock (_harnessSysPLLNode_clk_out3),	// @[XilinxShell.scala:76:55]
    .auto_in_member_1_reset (~_harnessSysPLLNode_locked | ~_reset_ibuf_O),	// @[Arty100TShell.scala:323:28, Harness.scala:60:56, PLLFactory.scala:78:{20,35}, XilinxShell.scala:76:55]
    .auto_in_member_0_clock (_harnessSysPLLNode_clk_out2),	// @[XilinxShell.scala:76:55]
    .auto_in_member_0_reset (~_harnessSysPLLNode_locked | ~_reset_ibuf_O),	// @[Arty100TShell.scala:323:28, Harness.scala:60:56, PLLFactory.scala:78:{20,35}, XilinxShell.scala:76:55]
    .auto_out_1_clock       (_ddrGroup_auto_out_1_clock),
    .auto_out_1_reset       (_ddrGroup_auto_out_1_reset),
    .auto_out_0_clock       (_ddrGroup_auto_out_0_clock),
    .auto_out_0_reset       (_ddrGroup_auto_out_0_reset)
  );
  XilinxArty100TMIG mig (	// @[Arty100TShell.scala:239:23]
    .clock                         (_dutWrangler_auto_out_0_clock),	// @[Harness.scala:31:31]
    .reset                         (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .auto_buffer_in_a_valid        (ddrBlockDuringReset_r & _chiptop_tl_slave_0_a_valid),	// @[Blockable.scala:32:18, :35:30, :36:20, Harness.scala:24:27, Reg.scala:35:20]
    .auto_buffer_in_a_bits_opcode  (_chiptop_tl_slave_0_a_bits_opcode),	// @[Harness.scala:24:27]
    .auto_buffer_in_a_bits_param   (_chiptop_tl_slave_0_a_bits_param),	// @[Harness.scala:24:27]
    .auto_buffer_in_a_bits_size    (_chiptop_tl_slave_0_a_bits_size),	// @[Harness.scala:24:27]
    .auto_buffer_in_a_bits_source  (_chiptop_tl_slave_0_a_bits_source),	// @[Harness.scala:24:27]
    .auto_buffer_in_a_bits_address (_chiptop_tl_slave_0_a_bits_address),	// @[Harness.scala:24:27]
    .auto_buffer_in_a_bits_mask    (_chiptop_tl_slave_0_a_bits_mask),	// @[Harness.scala:24:27]
    .auto_buffer_in_a_bits_data    (_chiptop_tl_slave_0_a_bits_data),	// @[Harness.scala:24:27]
    .auto_buffer_in_a_bits_corrupt (_chiptop_tl_slave_0_a_bits_corrupt),	// @[Harness.scala:24:27]
    .auto_buffer_in_d_ready        (ddrBlockDuringReset_r_1 & _chiptop_tl_slave_0_d_ready),	// @[Blockable.scala:33:18, :35:30, :37:20, Harness.scala:24:27, Reg.scala:35:20]
    .io_port_ddr3_dq               (ddr_ddr3_dq),
    .io_port_ddr3_dqs_n            (ddr_ddr3_dqs_n),
    .io_port_ddr3_dqs_p            (ddr_ddr3_dqs_p),
    .io_port_sys_clk_i             (_dutWrangler_auto_out_1_clock),	// @[Harness.scala:31:31]
    .io_port_clk_ref_i             (_dutWrangler_auto_out_2_clock),	// @[Harness.scala:31:31]
    .io_port_aresetn               (~_dutWrangler_auto_out_3_reset),	// @[Arty100TShell.scala:266:21, Harness.scala:31:31]
    .io_port_sys_rst               (_WIRE_1),	// @[Arty100TShell.scala:334:26]
    .auto_buffer_in_a_ready        (_mig_auto_buffer_in_a_ready),
    .auto_buffer_in_d_valid        (_mig_auto_buffer_in_d_valid),
    .auto_buffer_in_d_bits_opcode  (_mig_auto_buffer_in_d_bits_opcode),
    .auto_buffer_in_d_bits_param   (_mig_auto_buffer_in_d_bits_param),
    .auto_buffer_in_d_bits_size    (_mig_auto_buffer_in_d_bits_size),
    .auto_buffer_in_d_bits_source  (_mig_auto_buffer_in_d_bits_source),
    .auto_buffer_in_d_bits_sink    (_mig_auto_buffer_in_d_bits_sink),
    .auto_buffer_in_d_bits_denied  (_mig_auto_buffer_in_d_bits_denied),
    .auto_buffer_in_d_bits_data    (_mig_auto_buffer_in_d_bits_data),
    .auto_buffer_in_d_bits_corrupt (_mig_auto_buffer_in_d_bits_corrupt),
    .io_port_ddr3_addr             (ddr_ddr3_addr),
    .io_port_ddr3_ba               (ddr_ddr3_ba),
    .io_port_ddr3_ras_n            (ddr_ddr3_ras_n),
    .io_port_ddr3_cas_n            (ddr_ddr3_cas_n),
    .io_port_ddr3_we_n             (ddr_ddr3_we_n),
    .io_port_ddr3_reset_n          (ddr_ddr3_reset_n),
    .io_port_ddr3_ck_p             (ddr_ddr3_ck_p),
    .io_port_ddr3_ck_n             (ddr_ddr3_ck_n),
    .io_port_ddr3_cke              (ddr_ddr3_cke),
//    .io_port_ddr3_cs_n             (ddr_ddr3_cs_n),
    .io_port_ddr3_dm               (ddr_ddr3_dm),
    .io_port_ddr3_odt              (ddr_ddr3_odt),
    .io_port_ui_clk                (_mig_io_port_ui_clk),
    .io_port_ui_clk_sync_rst       (_mig_io_port_ui_clk_sync_rst),
    .io_port_mmcm_locked           (_mig_io_port_mmcm_locked),
    .io_port_init_calib_complete   (_mig_io_port_init_calib_complete)
  );
  harnessSysPLLNode harnessSysPLLNode (	// @[XilinxShell.scala:76:55]
    .clk_in1  (_sys_clock_ibufg_O),	// @[ClockOverlay.scala:32:22]
    .reset    (_WIRE_1),	// @[Arty100TShell.scala:334:26]
    .clk_out1 (_harnessSysPLLNode_clk_out1),
    .clk_out2 (_harnessSysPLLNode_clk_out2),
    .clk_out3 (_harnessSysPLLNode_clk_out3),
    .locked   (_harnessSysPLLNode_locked)
  );
  IBUFG sys_clock_ibufg (	// @[ClockOverlay.scala:32:22]
    .I (sys_clock),
    .O (_sys_clock_ibufg_O)
  );
  UIntToAnalog_1 a2b (	// @[Util.scala:58:21]
    .a    (uart_txd),
    .b    (_uart_to_serial_io_uart_txd),	// @[HarnessBinders.scala:31:36]
    .b_en (1'h1)	// @[Counter.scala:118:16]
  );
  AnalogToUInt_1 bundleIn_0_rxd_a2b (	// @[Util.scala:30:21]
    .a (uart_rxd),
    .b (_bundleIn_0_rxd_a2b_b)
  );
  AsyncQueue bits_out_queue (	// @[SerialAdapter.scala:28:29]
    .io_enq_clock (_chiptop_serial_tl_clock),	// @[Harness.scala:24:27]
    .io_enq_reset (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .io_enq_valid (_chiptop_serial_tl_bits_out_valid),	// @[Harness.scala:24:27]
    .io_enq_bits  (_chiptop_serial_tl_bits_out_bits),	// @[Harness.scala:24:27]
    .io_deq_clock (_dutWrangler_auto_out_0_clock),	// @[Harness.scala:31:31]
    .io_deq_reset (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .io_deq_ready (_ram_io_ser_out_ready),	// @[SerialAdapter.scala:58:24]
    .io_enq_ready (_bits_out_queue_io_enq_ready),
    .io_deq_valid (_bits_out_queue_io_deq_valid),
    .io_deq_bits  (_bits_out_queue_io_deq_bits)
  );
  AsyncQueue bits_in_queue (	// @[SerialAdapter.scala:34:28]
    .io_enq_clock (_dutWrangler_auto_out_0_clock),	// @[Harness.scala:31:31]
    .io_enq_reset (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .io_enq_valid (_ram_io_ser_in_valid),	// @[SerialAdapter.scala:58:24]
    .io_enq_bits  (_ram_io_ser_in_bits),	// @[SerialAdapter.scala:58:24]
    .io_deq_clock (_chiptop_serial_tl_clock),	// @[Harness.scala:24:27]
    .io_deq_reset (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .io_deq_ready (_chiptop_serial_tl_bits_in_ready),	// @[Harness.scala:24:27]
    .io_enq_ready (_bits_in_queue_io_enq_ready),
    .io_deq_valid (_bits_in_queue_io_deq_valid),
    .io_deq_bits  (_bits_in_queue_io_deq_bits)
  );
  SerialRAM ram (	// @[SerialAdapter.scala:58:24]
    .clock                (_dutWrangler_auto_out_0_clock),	// @[Harness.scala:31:31]
    .reset                (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .io_ser_in_ready      (_bits_in_queue_io_enq_ready),	// @[SerialAdapter.scala:34:28]
    .io_ser_out_valid     (_bits_out_queue_io_deq_valid),	// @[SerialAdapter.scala:28:29]
    .io_ser_out_bits      (_bits_out_queue_io_deq_bits),	// @[SerialAdapter.scala:28:29]
    .io_tsi_ser_in_valid  (_serial_width_adapter_io_wide_out_valid),	// @[HarnessBinders.scala:33:42]
    .io_tsi_ser_in_bits   (_serial_width_adapter_io_wide_out_bits),	// @[HarnessBinders.scala:33:42]
    .io_tsi_ser_out_ready (_serial_width_adapter_io_wide_in_ready),	// @[HarnessBinders.scala:33:42]
    .io_ser_in_valid      (_ram_io_ser_in_valid),
    .io_ser_in_bits       (_ram_io_ser_in_bits),
    .io_ser_out_ready     (_ram_io_ser_out_ready),
    .io_tsi_ser_in_ready  (_ram_io_tsi_ser_in_ready),
    .io_tsi_ser_out_valid (_ram_io_tsi_ser_out_valid),
    .io_tsi_ser_out_bits  (_ram_io_tsi_ser_out_bits),
    .io_adapter_state     (_ram_io_adapter_state)
  );
  UARTToSerial uart_to_serial (	// @[HarnessBinders.scala:31:36]
    .clock               (_dutWrangler_auto_out_0_clock),	// @[Harness.scala:31:31]
    .reset               (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .io_uart_rxd         (_bundleIn_0_rxd_a2b_b),	// @[Util.scala:30:21]
    .io_serial_in_valid  (_serial_width_adapter_io_narrow_out_valid),	// @[HarnessBinders.scala:33:42]
    .io_serial_in_bits   (_serial_width_adapter_io_narrow_out_bits),	// @[HarnessBinders.scala:33:42]
    .io_serial_out_ready (_serial_width_adapter_io_narrow_in_ready),	// @[HarnessBinders.scala:33:42]
    .io_uart_txd         (_uart_to_serial_io_uart_txd),
    .io_serial_in_ready  (_uart_to_serial_io_serial_in_ready),
    .io_serial_out_valid (_uart_to_serial_io_serial_out_valid),
    .io_serial_out_bits  (_uart_to_serial_io_serial_out_bits),
    .io_dropped          (led_4)
  );
  SerialWidthAdapter serial_width_adapter (	// @[HarnessBinders.scala:33:42]
    .clock               (_dutWrangler_auto_out_0_clock),	// @[Harness.scala:31:31]
    .reset               (_dutWrangler_auto_out_0_reset),	// @[Harness.scala:31:31]
    .io_narrow_in_valid  (_uart_to_serial_io_serial_out_valid),	// @[HarnessBinders.scala:31:36]
    .io_narrow_in_bits   (_uart_to_serial_io_serial_out_bits),	// @[HarnessBinders.scala:31:36]
    .io_narrow_out_ready (_uart_to_serial_io_serial_in_ready),	// @[HarnessBinders.scala:31:36]
    .io_wide_in_valid    (_ram_io_tsi_ser_out_valid),	// @[SerialAdapter.scala:58:24]
    .io_wide_in_bits     (_ram_io_tsi_ser_out_bits),	// @[SerialAdapter.scala:58:24]
    .io_wide_out_ready   (_ram_io_tsi_ser_in_ready),	// @[SerialAdapter.scala:58:24]
    .io_narrow_in_ready  (_serial_width_adapter_io_narrow_in_ready),
    .io_narrow_out_valid (_serial_width_adapter_io_narrow_out_valid),
    .io_narrow_out_bits  (_serial_width_adapter_io_narrow_out_bits),
    .io_wide_in_ready    (_serial_width_adapter_io_wide_in_ready),
    .io_wide_out_valid   (_serial_width_adapter_io_wide_out_valid),
    .io_wide_out_bits    (_serial_width_adapter_io_wide_out_bits)
  );
  plusarg_reader_Arty100THarness_UNIQUIFIED #(
    .FORMAT("custom_boot_pin=%d"),
    .DEFAULT(0),
    .WIDTH(1)
  ) plusarg_reader_Arty100THarness_UNIQUIFIED (	// @[PlusArg.scala:80:11]
    .out (_plusarg_reader_out)
  );
  IBUF reset_ibuf (	// @[Arty100TShell.scala:323:28]
    .I (reset),
    .O (_reset_ibuf_O)
  );
  PowerOnResetFPGAOnly powerOnReset_fpga_power_on (	// @[Xilinx.scala:104:21]
    .clock          (_sys_clock_ibufg_O),	// @[ClockOverlay.scala:32:22]
    .power_on_reset (_powerOnReset_fpga_power_on_power_on_reset)
  );
  assign led_7 = bundleIn_0_9;	// @[Harness.scala:69:60]
  assign led_6 = _reset_ibuf_O;	// @[Arty100TShell.scala:323:28]
  assign led_5 = _mig_io_port_init_calib_complete;	// @[Arty100TShell.scala:239:23]
  assign led_0 = _ram_io_adapter_state[0];	// @[HarnessBinders.scala:42:57, SerialAdapter.scala:58:24]
  assign led_1 = _ram_io_adapter_state[1];	// @[HarnessBinders.scala:43:58, SerialAdapter.scala:58:24]
  assign led_2 = _ram_io_adapter_state[2];	// @[HarnessBinders.scala:44:58, SerialAdapter.scala:58:24]
  assign led_3 = _ram_io_adapter_state[3];	// @[HarnessBinders.scala:45:58, SerialAdapter.scala:58:24]
endmodule

