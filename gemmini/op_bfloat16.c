// See LICENSE for license details.

#include <stdio.h>
#include <stdlib.h>
#include "include/gemmini_testutils.h"
#include "include/bfloat16.h"

#define MAT_DIM 256

bfloat16 setf_zero() {
    return 0;
}

bfloat16 setf_0or1() {
    return rand() % 2 ? BFLOAT16_1 : 0;
}

bfloat16 setf_random() {
    return f32_to_bf16((rand() % 64 - 32) / 32.0f);
}

// dimI & dimJ must be multiple of DIM
void mvin_big(void *A, uint32_t addr, int scale, int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *) A;
    int ptr_acc = (DIM - 1) * dimJ;
    gemmini_extended2_config_ld(dimI * sizeof(bfloat16), scale, true);
    for (int i = 0; i < dimI; i += DIM) {
        for (int j = 0; j < dimJ; j += DIM) {
            gemmini_mvin(pA, addr);
            pA += DIM;
            addr += DIM;
        }
        pA += ptr_acc;
    }
}

void mvout_big(void *A, uint32_t addr, int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *) A;
    int ptr_acc = (DIM - 1) * dimJ;
    gemmini_config_st(dimI * sizeof(bfloat16));
    for (int i = 0; i < dimI; i += DIM) {
        for (int j = 0; j < dimJ; j += DIM) {
            gemmini_mvout(pA, addr);
            pA += DIM;
            addr += DIM;
        }
        pA += ptr_acc;
    }
}

int main()
{
    printf("MAT_DIM: %d\n", MAT_DIM);
    gemmini_flush(0);

    static bfloat16 A[MAT_DIM][MAT_DIM] row_align(1);
    static bfloat16 B[MAT_DIM][MAT_DIM] row_align(1);
    static bfloat16 C[MAT_DIM][MAT_DIM] row_align(1);
    static bfloat16 D[MAT_DIM][MAT_DIM] row_align_acc(1);

    static bfloat16 gold[MAT_DIM][MAT_DIM];

    uint32_t A_acc_addr = 1 << (ADDR_LEN - 1);
    uint32_t B_acc_addr = (1 << (ADDR_LEN - 1)) | (1 << (ADDR_LEN - 2));
    uint32_t C_acc_addr = 1 << (ADDR_LEN - 1);

    unsigned long cpu_start, cpu_end, start, end;

    static float buffer[MAT_DIM * MAT_DIM * 2];

    bf16_matset(A, setf_0or1, MAT_DIM, MAT_DIM);
    bf16_matset(B, setf_0or1, MAT_DIM, MAT_DIM);
    bf16_matset(D, setf_zero, MAT_DIM, MAT_DIM);

    // vecadd
    printf("Starting slow CPU vecadd\n");
    cpu_start = read_cycles();

    bf16_matadd(A, B, gold, MAT_DIM, MAT_DIM);

    cpu_end = read_cycles();
    printf("Cycles taken: %u\n", cpu_end - cpu_start);

    printf("Starting gemmini vecadd\n");
    start = read_cycles();

    mvin_big(A, A_acc_addr, BFLOAT16_1, MAT_DIM, MAT_DIM);
    mvin_big(B, B_acc_addr, BFLOAT16_1, MAT_DIM, MAT_DIM);

    gemmini_config_ex(0, NO_ACTIVATION, 0);
    gemmini_config_st(MAT_DIM * sizeof(elem_t));
    mvout_big(C, C_acc_addr, MAT_DIM, MAT_DIM);
    gemmini_fence();

    end = read_cycles();
    printf("vecadd Cycles taken: %u\n", end - start);

    if (!bf16_matequal(C, gold, MAT_DIM, MAT_DIM)) {
        printf("A:\n");
        bf16_printMatrix(A, MAT_DIM, MAT_DIM);
        printf("B:\n");
        bf16_printMatrix(B, MAT_DIM, MAT_DIM);
        printf("C:\n");
        bf16_printMatrix(C, MAT_DIM, MAT_DIM);
        printf("Gold:\n");
        bf16_printMatrix(gold, MAT_DIM, MAT_DIM);
        printf("vecadd fail!\n");
    }

    // vecsub
    printf("Starting slow CPU vecsub\n");
    cpu_start = read_cycles();

    bf16_matsub(A, B, gold, MAT_DIM, MAT_DIM);

    cpu_end = read_cycles();
    printf("Cycles taken: %u\n", cpu_end - cpu_start);

    printf("Starting gemmini vecsub\n");
    start = read_cycles();

    mvin_big(A, A_acc_addr, BFLOAT16_1, MAT_DIM, MAT_DIM);
    mvin_big(B, B_acc_addr, BFLOAT16_MINUS_1, MAT_DIM, MAT_DIM);

    gemmini_config_ex(0, NO_ACTIVATION, 0);
    gemmini_config_st(MAT_DIM * sizeof(elem_t));
    mvout_big(C, C_acc_addr, MAT_DIM, MAT_DIM);
    gemmini_fence();

    end = read_cycles();
    printf("vecsub Cycles taken: %u\n", end - start);

    if (!bf16_matequal(C, gold, MAT_DIM, MAT_DIM)) {
        printf("A:\n");
        bf16_printMatrix(A, MAT_DIM, MAT_DIM);
        printf("B:\n");
        bf16_printMatrix(B, MAT_DIM, MAT_DIM);
        printf("C:\n");
        bf16_printMatrix(C, MAT_DIM, MAT_DIM);
        printf("Gold:\n");
        bf16_printMatrix(gold, MAT_DIM, MAT_DIM);
        printf("vecsub fail!\n");
    }

    // vecmul
    printf("Starting slow CPU vecmul\n");
    cpu_start = read_cycles();

    bf16_matmul(A, B, D, gold, MAT_DIM, MAT_DIM, 1, 1, MAT_DIM, MAT_DIM, MAT_DIM);

    cpu_end = read_cycles();
    printf("Cycles taken: %u\n", cpu_end - cpu_start);
 
    printf("Starting gemmini vecmul\n");
    start = read_cycles();

    tiled_matmul_auto(MAT_DIM, MAT_DIM, 1,
                      (elem_t *)A, (elem_t *)B, NULL, (elem_t *)C,
                      1, MAT_DIM, MAT_DIM, MAT_DIM,
                      MVIN_SCALE_IDENTITY, MVIN_SCALE_IDENTITY, MVIN_SCALE_IDENTITY,
                      NO_ACTIVATION, ACC_SCALE_IDENTITY, 0, false,
                      false, false, false, true, 0, WS);

    gemmini_fence();

    end = read_cycles();
    printf("vecmul Cycles taken: %u\n", end - start);
    if (!bf16_matequal(C, gold, MAT_DIM, MAT_DIM)) {
        printf("A:\n");
        bf16_printMatrix(A, MAT_DIM, 1);
        printf("B:\n");
        bf16_printMatrix(B, 1, MAT_DIM);
        printf("C:\n");
        bf16_printMatrix(C, MAT_DIM, MAT_DIM);
        printf("Gold:\n");
        bf16_printMatrix(gold, MAT_DIM, MAT_DIM);
        printf("vecmul fail!\n");
    }
    return 0;
}