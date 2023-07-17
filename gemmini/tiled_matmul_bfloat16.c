// See LICENSE for license details.

#include <stdio.h>
#include <stdlib.h>
#include "include/gemmini_testutils.h"
#include "include/bfloat16.h"

#define MAT_DIM 4

bfloat16 setf_zero() {
    return 0;
}

bfloat16 setf_0or1() {
    return rand() % 2 ? 0x3F80 : 0;
}

bfloat16 setf_random() {
    return f32_to_bf16((rand() % 64 - 32) / 32.0f);
}

int main()
{
    printf("MAT_DIM_I: %d\n", MAT_DIM);
    printf("MAT_DIM_J: %d\n", MAT_DIM);
    printf("MAT_DIM_K: %d\n", MAT_DIM);

    gemmini_flush(0);

    static bfloat16 A[MAT_DIM][MAT_DIM] row_align(1);
    static bfloat16 B[MAT_DIM][MAT_DIM] row_align(1);
    static bfloat16 C[MAT_DIM][MAT_DIM] row_align(1);
    static bfloat16 D[MAT_DIM][MAT_DIM] row_align_acc(1);

    static bfloat16 gold[MAT_DIM][MAT_DIM];

    static float buffer[MAT_DIM * MAT_DIM * 2];

    bf16_matset(A, setf_0or1, MAT_DIM);
    bf16_matset(B, setf_0or1, MAT_DIM);
    bf16_matset(D, setf_zero, MAT_DIM);

    printf("Starting slow CPU matmul\n");
    unsigned long cpu_start = read_cycles();

    bf16_matmul(A, B, D, gold, MAT_DIM, buffer);

    unsigned long cpu_end = read_cycles();
    printf("Cycles taken: %u\n", cpu_end - cpu_start);

    printf("Starting gemmini matmul\n");
    unsigned long start = read_cycles();

    tiled_matmul_auto(MAT_DIM, MAT_DIM, MAT_DIM,
                      (elem_t *)A, (elem_t *)B, (elem_t *)D, (elem_t *)C,
                      MAT_DIM, MAT_DIM, MAT_DIM, MAT_DIM,
                      MVIN_SCALE_IDENTITY, MVIN_SCALE_IDENTITY, MVIN_SCALE_IDENTITY,
                      NO_ACTIVATION, ACC_SCALE_IDENTITY, 0, false,
                      false, false, false, true, 0, WS);

    unsigned long end = read_cycles();
    printf("Cycles taken: %u\n", end - start);

    printf("A:\n");
    bf16_printMatrix(A, MAT_DIM);
    printf("B:\n");
    bf16_printMatrix(B, MAT_DIM);
    printf("C:\n");
    bf16_printMatrix(C, MAT_DIM);
    printf("D:\n");
    bf16_printMatrix(D, MAT_DIM);
    printf("Gold:\n");
    bf16_printMatrix(gold, MAT_DIM);
    printf("\n");

    exit(!bf16_matequal(C, gold, MAT_DIM));
}