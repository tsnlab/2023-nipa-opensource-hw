#include <stdio.h> 
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

int main(void) {
	int i;
	uint64_t A[4], B[4], Radd[4], Rsub[4], Rmul[4], Rdiv[4];
	A[0] = f32_to_bf16(1.0); A[1] = f32_to_bf16(3.0); A[2] = f32_to_bf16(5.0); A[3] = f32_to_bf16(7.0);
	B[0] = f32_to_bf16(4.0); B[1] = f32_to_bf16(3.0); B[2] = f32_to_bf16(2.0); B[3] = f32_to_bf16(1.0);
	printf("A: "); for (i=0; i<4; i++) printf("%x ", A[i]); puts("");
	printf("B: "); for (i=0; i<4; i++) printf("%x ", B[i]); puts("");
	printf("fpu-add: "); for (i=0; i<4; i++) printf("%x ", bf16_add(A[i], B[i])); puts("");
	printf("fpu-sub: "); for (i=0; i<4; i++) printf("%x ", bf16_add(A[i], bf16_neg(B[i]))); puts("");
	printf("fpu-mul: "); for (i=0; i<4; i++) printf("%x ", bf16_mul(A[i], B[i])); puts("");
	printf("fpu-div: "); printf("%x %x %x %x\n",
		(int)f32_to_bf16(0.25), (int)f32_to_bf16(1.0),
		(int)f32_to_bf16(2.5), (int)f32_to_bf16(7.0));
NPU_ADD(A[1], B[1], Radd[1]);
	uint32_t start = read_cycles();

	for (i=0; i<4; i++) {
		NPU_ADD(A[i], B[i], Radd[i]);
		NPU_SUB(A[i], B[i], Rsub[i]);
		NPU_MUL(A[i], B[i], Rmul[i]);
		NPU_DIV(A[i], B[i], Rdiv[i]);
	}
	
/*	
	NPU_LOAD_F16(4, 0x0, A);
	NPU_LOAD_F16(4, 0x10, B); 

	NPU_ADD_F16(4, 0x100, 0x0, 0x10);
	NPU_STORE_F16(4, 0x100, Radd);

	NPU_SUB_F16(4, 0x110, 0x0, 0x10);
	NPU_STORE_F16(4, 0x110, Rsub);

	NPU_MUL_F16(4, 0x120, 0x0, 0x10);
	NPU_STORE_F16(4, 0x120, Rmul);
	NPU_DIV_F16(4, 0x130, 0x0, 0x10);
	NPU_STORE_F16(4, 0x130, Rdiv);
*/
    asm volatile("fence.i");

	uint32_t end = read_cycles();
    printf("Cycles taken: %lu\n", end-start);

	printf("npu-add: "); for (i=0; i<4; i++) printf("%x ", Radd[i]); puts("");
	printf("npu-sub: "); for (i=0; i<4; i++) printf("%x ", Rsub[i]); puts("");
	printf("npu-mul: "); for (i=0; i<4; i++) printf("%x ", Rmul[i]); puts("");
	printf("npu-div: "); for (i=0; i<4; i++) printf("%x ", Rdiv[i]); puts("");
	return 0;
}
