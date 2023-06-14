`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 06/09/2023 11:14:32 AM
// Design Name: 
// Module Name: RocketTestTop
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module RocketTestTop(
        input  wire clock,
        input  wire reset,
        input  wire interrupt,
        inout  wire uart_rxd,
        inout  wire uart_txd,
        inout  wire [15:0]ddr3_dq,
        inout  wire [1:0] ddr3_dqs_n,
        inout  wire [1:0] ddr3_dqs_p,
        output wire [13:0]ddr3_addr,
        output wire [2:0] ddr3_ba,
        output wire ddr3_ras_n,
        output wire ddr3_cas_n,
        output wire ddr3_we_n,
        output wire ddr3_reset_n,
        output wire ddr3_ck_p,
        output wire ddr3_ck_n,
        output wire ddr3_cke,
        output wire ddr3_cs_n,
        output wire [1:0] ddr3_dm,
        output wire ddr3_odt
    );
/*
    wire        wrapperio_interrupt;
    wire        wrapperio_s_axi_arready;
    wire [31:0] wrapperio_s_axi_rdata;
    wire        wrapperio_s_axi_rlast;
    wire [1:0]  wrapperio_s_axi_rresp;
    wire        wrapperio_s_axi_rvalid;
    wire        wrapperio_s_axi_awready;
    wire [1:0]  wrapperio_s_axi_bresp;
    wire        wrapperio_s_axi_bvalid;
    wire        wrapperio_s_axi_wready;
    wire        wrapperio_clock_out;
    wire        wrapperio_reset_out;
    wire [31:0] wrapperio_s_axi_araddr;
    wire [1:0]  wrapperio_s_axi_arburst;
    wire [3:0]  wrapperio_s_axi_arcache;
    wire [7:0]  wrapperio_s_axi_arlen;
    wire [2:0]  wrapperio_s_axi_arprot;
    wire        wrapperio_s_axi_arsize;
    wire        wrapperio_s_axi_arvalid;
    wire        wrapperio_s_axi_rready;
    wire [31:0] wrapperio_s_axi_awaddr;
    wire [1:0]  wrapperio_s_axi_awburst;
    wire [3:0]  wrapperio_s_axi_awcache;
    wire [7:0]  wrapperio_s_axi_awlen;
    wire [2:0]  wrapperio_s_axi_awprot;
    wire        wrapperio_s_axi_awsize;
    wire        wrapperio_s_axi_awvalid;
    wire        wrapperio_s_axi_bready;
    wire [31:0] wrapperio_s_axi_wdata;
    wire        wrapperio_s_axi_wlast;
    wire [3:0]  wrapperio_s_axi_wstrb;
    wire        wrapperio_s_axi_wvalid;
*/
    Arty100THarness rocketchip(
        .sys_clock(clock),
        .reset(reset),
        .uart_rxd(uart_rxd),
        .uart_txd(uart_txd),
        .ddr_ddr3_dq(ddr3_dq),
        .ddr_ddr3_dqs_n(ddr3_dqs_n),
        .ddr_ddr3_dqs_p(ddr3_dqs_p),
        .ddr_ddr3_addr(ddr3_addr),
        .ddr_ddr3_ba(ddr3_ba),
        .ddr_ddr3_ras_n(ddr3_ras_n),
        .ddr_ddr3_cas_n(ddr3_cas_n),
        .ddr_ddr3_we_n(ddr3_we_n),
        .ddr_ddr3_reset_n(ddr3_reset_n),
        .ddr_ddr3_ck_p(ddr3_ck_p),
        .ddr_ddr3_ck_n(ddr3_ck_n),
        .ddr_ddr3_cke(ddr3_cke),
        .ddr_ddr3_cs_n(ddr3_cs_n),
        .ddr_ddr3_dm(ddr3_dm),
        .ddr_ddr3_odt(ddr3_odt) /*,
        
    .wrapperio_interrupt       (wrapperio_interrupt),
    .wrapperio_s_axi_arready   (wrapperio_s_axi_arready),
    .wrapperio_s_axi_rdata     (wrapperio_s_axi_rdata),
    .wrapperio_s_axi_rlast     (wrapperio_s_axi_rlast),
    .wrapperio_s_axi_rresp     (wrapperio_s_axi_rresp),
    .wrapperio_s_axi_rvalid    (wrapperio_s_axi_rvalid),
    .wrapperio_s_axi_awready   (wrapperio_s_axi_awready),
    .wrapperio_s_axi_bresp     (wrapperio_s_axi_bresp),
    .wrapperio_s_axi_bvalid    (wrapperio_s_axi_bvalid),
    .wrapperio_s_axi_wready    (wrapperio_s_axi_wready),
    .wrapperio_clock_out       (wrapperio_clock_out),
    .wrapperio_reset_out       (wrapperio_reset_out),
    .wrapperio_s_axi_araddr    (wrapperio_s_axi_araddr),
    .wrapperio_s_axi_arburst   (wrapperio_s_axi_arburst),
    .wrapperio_s_axi_arcache   (wrapperio_s_axi_arcache),
    .wrapperio_s_axi_arlen     (wrapperio_s_axi_arlen),
    .wrapperio_s_axi_arprot    (wrapperio_s_axi_arprot),
    .wrapperio_s_axi_arsize    (wrapperio_s_axi_arsize),
    .wrapperio_s_axi_arvalid   (wrapperio_s_axi_arvalid),
    .wrapperio_s_axi_rready    (wrapperio_s_axi_rready),
    .wrapperio_s_axi_awaddr    (wrapperio_s_axi_awaddr),
    .wrapperio_s_axi_awburst   (wrapperio_s_axi_awburst),
    .wrapperio_s_axi_awcache   (wrapperio_s_axi_awcache),
    .wrapperio_s_axi_awlen     (wrapperio_s_axi_awlen),
    .wrapperio_s_axi_awprot    (wrapperio_s_axi_awprot),
    .wrapperio_s_axi_awsize    (wrapperio_s_axi_awsize),
    .wrapperio_s_axi_awvalid   (wrapperio_s_axi_awvalid),
    .wrapperio_s_axi_bready    (wrapperio_s_axi_bready),
    .wrapperio_s_axi_wdata     (wrapperio_s_axi_wdata),
    .wrapperio_s_axi_wlast     (wrapperio_s_axi_wlast),
    .wrapperio_s_axi_wstrb     (wrapperio_s_axi_wstrb),
    .wrapperio_s_axi_wvalid    (wrapperio_s_axi_wvalid)
    */
    );
endmodule


