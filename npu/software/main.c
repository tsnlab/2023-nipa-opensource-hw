#include <stdio.h>

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


#define NPU_CUSTOM(rd, rs1, rs2) ROCC_INSTRUCTION_R_R_R(3, rd, rs1, rs2, 0)

int main(void) {
    int x = 100, y = 200, z;
    printf("100 + 200 = ");
    NPU_CUSTOM(z, x, y);
    printf("%d\n", z);
    return 0;
}