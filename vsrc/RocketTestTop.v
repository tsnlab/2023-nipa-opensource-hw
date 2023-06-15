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

    wire        wrapperio_interrupt;
    wire        wrapperio_clock_out;
    wire        wrapperio_reset_out;
    wire        wrapperio_m_axi_arready;
    wire [31:0] wrapperio_m_axi_rdata;
    wire        wrapperio_m_axi_rlast;
    wire [1:0]  wrapperio_m_axi_rresp;
    wire        wrapperio_m_axi_rvalid;
    wire        wrapperio_m_axi_awready;
    wire [1:0]  wrapperio_m_axi_bresp;
    wire        wrapperio_m_axi_bvalid;
    wire        wrapperio_m_axi_wready;
    wire [31:0] wrapperio_m_axi_araddr;
    wire [1:0]  wrapperio_m_axi_arburst;
    wire [3:0]  wrapperio_m_axi_arcache;
    wire [7:0]  wrapperio_m_axi_arlen;
    wire [2:0]  wrapperio_m_axi_arprot;
    wire        wrapperio_m_axi_arsize;
    wire        wrapperio_m_axi_arvalid;
    wire        wrapperio_m_axi_rready;
    wire [31:0] wrapperio_m_axi_awaddr;
    wire [1:0]  wrapperio_m_axi_awburst;
    wire [3:0]  wrapperio_m_axi_awcache;
    wire [7:0]  wrapperio_m_axi_awlen;
    wire [2:0]  wrapperio_m_axi_awprot;
    wire        wrapperio_m_axi_awsize;
    wire        wrapperio_m_axi_awvalid;
    wire        wrapperio_m_axi_bready;
    wire [31:0] wrapperio_m_axi_wdata;
    wire        wrapperio_m_axi_wlast;
    wire [3:0]  wrapperio_m_axi_wstrb;
    wire        wrapperio_m_axi_wvalid;
   
    wire        wrapperio_s_axi_arready;
    wire [31:0] wrapperio_s_axi_rdata;
    wire        wrapperio_s_axi_rlast;
    wire [1:0]  wrapperio_s_axi_rresp;
    wire        wrapperio_s_axi_rvalid;
    wire        wrapperio_s_axi_awready;
    wire [1:0]  wrapperio_s_axi_bresp;
    wire        wrapperio_s_axi_bvalid;
    wire        wrapperio_s_axi_wready;
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
    
    wire bram_rst;
    wire bram_clk;
    wire bram_en;
    wire [3:0] bram_we;
    wire [11:0] bram_addr;
    wire [31:0] bram_wrdata;
    wire [31:0] bram_rddata;

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
        .ddr_ddr3_odt(ddr3_odt),
        
    .wrapperio_interrupt       (wrapperio_interrupt),
    .wrapperio_clock_out       (wrapperio_clock_out),
    .wrapperio_reset_out       (wrapperio_reset_out),
    .wrapperio_m_axi_arready   (wrapperio_m_axi_arready),
    .wrapperio_m_axi_rdata     (wrapperio_m_axi_rdata),
    .wrapperio_m_axi_rlast     (wrapperio_m_axi_rlast),
    .wrapperio_m_axi_rresp     (wrapperio_m_axi_rresp),
    .wrapperio_m_axi_rvalid    (wrapperio_m_axi_rvalid),
    .wrapperio_m_axi_awready   (wrapperio_m_axi_awready),
    .wrapperio_m_axi_bresp     (wrapperio_m_axi_bresp),
    .wrapperio_m_axi_bvalid    (wrapperio_m_axi_bvalid),
    .wrapperio_m_axi_wready    (wrapperio_m_axi_wready),
    .wrapperio_m_axi_araddr    (wrapperio_m_axi_araddr),
    .wrapperio_m_axi_arburst   (wrapperio_m_axi_arburst),
    .wrapperio_m_axi_arcache   (wrapperio_m_axi_arcache),
    .wrapperio_m_axi_arlen     (wrapperio_m_axi_arlen),
    .wrapperio_m_axi_arprot    (wrapperio_m_axi_arprot),
    .wrapperio_m_axi_arsize    (wrapperio_m_axi_arsize),
    .wrapperio_m_axi_arvalid   (wrapperio_m_axi_arvalid),
    .wrapperio_m_axi_rready    (wrapperio_m_axi_rready),
    .wrapperio_m_axi_awaddr    (wrapperio_m_axi_awaddr),
    .wrapperio_m_axi_awburst   (wrapperio_m_axi_awburst),
    .wrapperio_m_axi_awcache   (wrapperio_m_axi_awcache),
    .wrapperio_m_axi_awlen     (wrapperio_m_axi_awlen),
    .wrapperio_m_axi_awprot    (wrapperio_m_axi_awprot),
    .wrapperio_m_axi_awsize    (wrapperio_m_axi_awsize),
    .wrapperio_m_axi_awvalid   (wrapperio_m_axi_awvalid),
    .wrapperio_m_axi_bready    (wrapperio_m_axi_bready),
    .wrapperio_m_axi_wdata     (wrapperio_m_axi_wdata),
    .wrapperio_m_axi_wlast     (wrapperio_m_axi_wlast),
    .wrapperio_m_axi_wstrb     (wrapperio_m_axi_wstrb),
    .wrapperio_m_axi_wvalid    (wrapperio_m_axi_wvalid),
    .wrapperio_s_axi_arready   (wrapperio_s_axi_arready),
    .wrapperio_s_axi_rdata     (wrapperio_s_axi_rdata),
    .wrapperio_s_axi_rlast     (wrapperio_s_axi_rlast),
    .wrapperio_s_axi_rresp     (wrapperio_s_axi_rresp),
    .wrapperio_s_axi_rvalid    (wrapperio_s_axi_rvalid),
    .wrapperio_s_axi_awready   (wrapperio_s_axi_awready),
    .wrapperio_s_axi_bresp     (wrapperio_s_axi_bresp),
    .wrapperio_s_axi_bvalid    (wrapperio_s_axi_bvalid),
    .wrapperio_s_axi_wready    (wrapperio_s_axi_wready),
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
    
    
    );
    
    axi_bram_ctrl_0 bram_ctrl(
    .s_axi_aclk      (wrapperio_clock_out),
    .s_axi_aresetn   (~wrapperio_reset_out),
    .s_axi_arready   (wrapperio_m_axi_arready),
    .s_axi_rdata     (wrapperio_m_axi_rdata),
    .s_axi_rlast     (wrapperio_m_axi_rlast),
    .s_axi_rresp     (wrapperio_m_axi_rresp),
    .s_axi_rvalid    (wrapperio_m_axi_rvalid),
    .s_axi_awready   (wrapperio_m_axi_awready),
    .s_axi_bresp     (wrapperio_m_axi_bresp),
    .s_axi_bvalid    (wrapperio_m_axi_bvalid),
    .s_axi_wready    (wrapperio_m_axi_wready),
    .s_axi_araddr    (wrapperio_m_axi_araddr),
    .s_axi_arburst   (wrapperio_m_axi_arburst),
    .s_axi_arcache   (wrapperio_m_axi_arcache),
    .s_axi_arlen     (wrapperio_m_axi_arlen),
    .s_axi_arprot    (wrapperio_m_axi_arprot),
    .s_axi_arsize    (wrapperio_m_axi_arsize),
    .s_axi_arvalid   (wrapperio_m_axi_arvalid),
    .s_axi_rready    (wrapperio_m_axi_rready),
    .s_axi_awaddr    (wrapperio_m_axi_awaddr),
    .s_axi_awburst   (wrapperio_m_axi_awburst),
    .s_axi_awcache   (wrapperio_m_axi_awcache),
    .s_axi_awlen     (wrapperio_m_axi_awlen),
    .s_axi_awprot    (wrapperio_m_axi_awprot),
    .s_axi_awsize    (wrapperio_m_axi_awsize),
    .s_axi_awvalid   (wrapperio_m_axi_awvalid),
    .s_axi_bready    (wrapperio_m_axi_bready),
    .s_axi_wdata     (wrapperio_m_axi_wdata),
    .s_axi_wlast     (wrapperio_m_axi_wlast),
    .s_axi_wstrb     (wrapperio_m_axi_wstrb),
    .s_axi_wvalid    (wrapperio_m_axi_wvalid),
    .s_axi_awlock    (0),
    .s_axi_arlock    (0),
    .bram_rst_a      (bram_rst),
    .bram_clk_a      (bram_clk),
    .bram_en_a       (bram_en),
    .bram_we_a       (bram_we),
    .bram_addr_a     (bram_addr),
    .bram_wrdata_a   (bram_wrdata),
    .bram_rddata_a   (bram_rddata)
    );
 
    blk_mem_gen_0 bram(
    .clka            (bram_clk),
    .ena             (bram_en),
    .addra           (bram_addr), 
    .wea             (bram_we),
    .dina            (bram_wrdata),
    .douta           (bram_rddata)
    );
    
endmodule


