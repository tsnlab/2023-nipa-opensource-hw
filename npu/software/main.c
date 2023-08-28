#include <stdio.h>
#include <stdlib.h>

#include "bfloat16.h"

#define STR1(x) #x
#ifndef STR
#define STR(x) STR1(x)
#endif

#define CAT_(A, B) A##B
#define CAT(A, B) CAT_(A, B)

#define ROCC_INSTRUCTION_R_R_R(x, rd, rs1, rs2, func7)                               \
  {                                                                                  \
    asm volatile(                                                                    \
        ".insn r " STR(CAT(CUSTOM_, x)) ", " STR(0x7) ", " STR(func7) ", %0, %1, %2" \
        : "=r"(rd)                                                                   \
        : "r"(rs1), "r"(rs2));                                                       \
  }


#define ROCC_INSTRUCTION_0_R_R(x, rs1, rs2, func3, func7)                                   \
  {                                                                                  \
    asm volatile(                                                                    \
        ".insn r " STR(CAT(CUSTOM_, x)) ", " STR(func3) ", " STR(func7) ", x0, %0, %1" \
        :                                                                            \
        : "r"(rs1), "r"(rs2));                                                       \
  }


#define NPU_LOAD_F16(count, sram, dram) ROCC_INSTRUCTION_0_R_R(3, (count) | ((sram) << 16), dram, 0x3, 2)
#define NPU_STORE_F16(count, sram, dram) ROCC_INSTRUCTION_0_R_R(3, (count) | ((sram) << 16), dram, 0x3, 3)
#define NPU_ADD_F16(count, addrC, addrA, addrB) ROCC_INSTRUCTION_0_R_R(3, (count) | ((addrC) << 16), (addrA) | ((addrB) << 16), 0x3, 4)
#define NPU_SUB_F16(count, addrC, addrA, addrB) ROCC_INSTRUCTION_0_R_R(3, (count) | ((addrC) << 16), (addrA) | ((addrB) << 16), 0x3, 5)
#define NPU_MUL_F16(count, addrC, addrA, addrB) ROCC_INSTRUCTION_0_R_R(3, (count) | ((addrC) << 16), (addrA) | ((addrB) << 16), 0x3, 6)
#define NPU_DIV_F16(count, addrC, addrA, addrB) ROCC_INSTRUCTION_0_R_R(3, (count) | ((addrC) << 16), (addrA) | ((addrB) << 16), 0x3, 7)

#define NPU_ADD(a, b, c) ROCC_INSTRUCTION_R_R_R(3, c, a, b, 0)
#define NPU_SUB(a, b, c) ROCC_INSTRUCTION_R_R_R(3, c, a, b, 1)
#define NPU_MUL(a, b, c) ROCC_INSTRUCTION_R_R_R(3, c, a, b, 2)
#define NPU_DIV(a, b, c) ROCC_INSTRUCTION_R_R_R(3, c, a, b, 3)

static uint64_t read_cycles() {
    uint64_t cycles;
    asm volatile ("rdcycle %0" : "=r" (cycles));
    return cycles;
}

#define N 4
int main(void) {
	int i;
	uint64_t A[N], B[N], Fadd[N], Fsub[N], Fmul[N], Fdiv[N], Radd[N], Rsub[N], Rmul[N], Rdiv[N];
	uint32_t start, end;
	for (i=0; i<N; i++) { A[i] = f32_to_bf16(rand() / 256.0); B[i] = f32_to_bf16(rand() / 256.0); }

	printf("A: "); for (i=0; i<N; i++) printf("%x ", A[i]); puts("");
	printf("B: "); for (i=0; i<N; i++) printf("%x ", B[i]); puts("");

	start = read_cycles(); for (i=0; i<N; i++) Fadd[i] = bf16_add(A[i], B[i]); end = read_cycles(); printf("fpu-add Cycles taken: %lu\n", end-start);
	start = read_cycles(); for (i=0; i<N; i++) Fsub[i] = bf16_sub(A[i], B[i]); end = read_cycles(); printf("fpu-sub Cycles taken: %lu\n", end-start);
	start = read_cycles(); for (i=0; i<N; i++) Fmul[i] = bf16_mul(A[i], B[i]); end = read_cycles(); printf("fpu-mul Cycles taken: %lu\n", end-start);
	start = read_cycles(); for (i=0; i<N; i++) Fdiv[i] = bf16_div(A[i], B[i]); end = read_cycles(); printf("fpu-div Cycles taken: %lu\n", end-start);
	
	printf("fpu-add: "); for (i=0; i<N; i++) printf("%x ", Fadd[i]); puts("");
	printf("fpu-sub: "); for (i=0; i<N; i++) printf("%x ", Fsub[i]); puts("");
	printf("fpu-mul: "); for (i=0; i<N; i++) printf("%x ", Fmul[i]); puts("");
	printf("fpu-div: "); for (i=0; i<N; i++) printf("%x ", Fdiv[i]); puts("");


	start = read_cycles(); for (i=0; i<N; i++) NPU_ADD(A[i], B[i], Radd[i]); asm volatile("fence.i"); end = read_cycles(); printf("npu-add Cycles taken: %lu\n", end-start);
	start = read_cycles(); for (i=0; i<N; i++) NPU_SUB(A[i], B[i], Rsub[i]); asm volatile("fence.i"); end = read_cycles(); printf("npu-sub Cycles taken: %lu\n", end-start);
	start = read_cycles(); for (i=0; i<N; i++) NPU_MUL(A[i], B[i], Rmul[i]); asm volatile("fence.i"); end = read_cycles(); printf("npu-mul Cycles taken: %lu\n", end-start);
	start = read_cycles(); for (i=0; i<N; i++) NPU_DIV(A[i], B[i], Rdiv[i]); asm volatile("fence.i"); end = read_cycles(); printf("npu-div Cycles taken: %lu\n", end-start);
	
    
	printf("npu-add: "); for (i=0; i<4; i++) printf("%x ", Radd[i]); puts("");
	printf("npu-sub: "); for (i=0; i<4; i++) printf("%x ", Rsub[i]); puts("");
	printf("npu-mul: "); for (i=0; i<4; i++) printf("%x ", Rmul[i]); puts("");
	printf("npu-div: "); for (i=0; i<4; i++) printf("%x ", Rdiv[i]); puts("");
	return 0;
}

/*

example result:

A: 4ab0 4a97 4a41 49d4 
B: 4a81 4a8e 4a03 4aa0 
fpu-add Cycles taken: 833
fpu-sub Cycles taken: 853
fpu-mul Cycles taken: 846
fpu-div Cycles taken: 935
fpu-add: 4b18 4b12 4aa2 4ad5 
fpu-sub: 49bc 4890 4978 ca56 
fpu-mul: 55b1 55a7 54c5 5504 
fpu-div: 3fae 3f88 3fbc 3ea9 
npu-add Cycles taken: 282
npu-sub Cycles taken: 307
npu-mul Cycles taken: 272
npu-div Cycles taken: 308
npu-add: 4b18 4b12 4aa2 4ad5 
npu-sub: 49bc 4890 4978 ca56 
npu-mul: 55b1 55a8 54c6 5504 
npu-div: 3faf 3f88 3fbd 3eaa

*/

