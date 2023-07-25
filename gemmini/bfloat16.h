#ifndef BFLOAT16_H
#define BFLOAT16_H

#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>

typedef uint16_t bfloat16;
#define BFLOAT16_EPSILON    0.0078125
#define BFLOAT16_1          ((bfloat16)0x3F80)
#define BFLOAT16_MINUS_1    ((bfloat16)0xBF80)
#define BF(x)               f32_to_bf16(x)

static float bf16_to_f32(bfloat16 bf) {
    float result;
    *(uint32_t *)&result = (uint32_t)bf << 16;
    return result;
}

static float f32_to_bf16(float f) {
    return (*(uint32_t *)&f) >> 16;
}

static inline bfloat16 bf16_neg(bfloat16 a) {
    return ((a & 0x8000) ? (a & 0x7FFF) : (a | 0x8000));
}

static inline bfloat16 bf16_add(bfloat16 a, bfloat16 b) {
    return f32_to_bf16(bf16_to_f32(a) + bf16_to_f32(b));
}

static inline bool bf16_less(bfloat16 a, bfloat16 b) {
    return (bf16_to_f32(a) < bf16_to_f32(b));
}

static inline bfloat16 bf16_mul(bfloat16 a, bfloat16 b) {
    return f32_to_bf16(bf16_to_f32(a) * bf16_to_f32(b));
}


static inline bool bf16_equal(bfloat16 a, bfloat16 b) {
    float diff = bf16_to_f32(a) - bf16_to_f32(b);
    return -BFLOAT16_EPSILON < diff && diff < BFLOAT16_EPSILON;
}

static void mass_bf16_to_f32(bfloat16 *bfs, float *fs, int count) {
    for (int i = 0; i < count; i++) {
        fs[i] = bf16_to_f32(bfs[i]);
    }
}

static void mass_f32_to_bf16(float *fs, bfloat16 *bfs, int count) {
    for (int i = 0; i < count; i++) {
        bfs[i] = f32_to_bf16(fs[i]);
    }
}

static void bf16_matset(void *A, bfloat16 (*setf)(), int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *)A;
    for (int i = 0; i < dimI * dimJ; i++) {
        *pA++ = setf();
    }
}

static void bf16_matscale(void *A, float scale, int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *)A;
    for (int i = 0; i < dimI * dimJ; i++) {
        *pA++ = f32_to_bf16(bf16_to_f32(*pA++) * scale);
    }
}

static void bf16_matrelu(void *A, float scale, int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *)A;
    for (int i = 0; i < dimI * dimJ; i++) {
        if (*pA < 0) *pA = 0;
        pA++;
    }
}

static bool bf16_matequal(void *A, void *B, int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *)A, *pB = (bfloat16 *)B;
    for (int i = 0; i < dimI * dimJ; i++) {
        if (!bf16_equal(*pA++, *pB++)) return false;
    }
    return true;
}

static void bf16_matadd(void *A, void *B, void *C, int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *)A, *pB = (bfloat16 *)B, *pC = (bfloat16 *)C;
    for (int i = 0; i < dimI * dimJ; i++) {
        *pC++ = f32_to_bf16(bf16_to_f32(*pA++) + bf16_to_f32(*pB++));
    }
}

static void bf16_matsub(void *A, void *B, void *C, int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *)A, *pB = (bfloat16 *)B, *pC = (bfloat16 *)C;
    for (int i = 0; i < dimI * dimJ; i++) {
        *pC++ = f32_to_bf16(bf16_to_f32(*pA++) - bf16_to_f32(*pB++));
    }
}

// buffer needs to be 4 * (dimI + dimJ) * dimK bytes
static void bf16_matmul_buffered(void *A, void *B, void *D, void *C, int dimI, int dimJ, int dimK, void *buffer) {
    float *fa = (float *)buffer;
    float *fb = fa + dimI * dimK;
    float accum, mul;
    bfloat16 *pC = (bfloat16 *)C, *pD = (bfloat16 *)D;
    
    mass_bf16_to_f32((bfloat16 *)A, fa, dimI * dimK);
    mass_bf16_to_f32((bfloat16 *)B, fb, dimK * dimJ);

    for (int i = 0; i < dimI; i++) {
        for (int j = 0; j < dimJ; j++) {
            accum = bf16_to_f32(pD[i * dimI + j]);
            for (int k = 0; k < dimK; k++) {
                accum += fa[i * dimI + k] * fb[k * dimK + j];
            }
            pC[i * dimI + j] = f32_to_bf16(accum);
        }
    }
}

static void bf16_matmul(void *A, void *B, void *D, void *C,
                        int dimI, int dimJ, int dimK,
                        int strideA, int strideB, int strideD, int strideC) {
    float accum, mul;
    bfloat16 *pA = (bfloat16 *)A, *pB = (bfloat16 *)B;
    bfloat16 *pC = (bfloat16 *)C, *pD = (bfloat16 *)D;

    for (int i = 0; i < dimI; i++) {
        for (int j = 0; j < dimJ; j++) {
            accum = bf16_to_f32(pD[i * strideD + j]);
            for (int k = 0; k < dimK; k++) {
                accum += bf16_to_f32(pA[i * strideA + k]) * bf16_to_f32(pB[k * strideB + j]);
            }
            pC[i * strideC + j] = f32_to_bf16(accum);
        }
    }
}

#ifndef NO_PRINT_BF16
#include "include/printf.h"

static void bf16_printMatrix(void *A, int dimI, int dimJ) {
    bfloat16 *pA = (bfloat16 *)A;
    for (int i = 0; i < dimI; i++) {
        for (int j = 0; j < dimJ; j++) {
            printf_ex("%r ", pA[i * dimI + j]);
        }
        printf("\n");
    }
}

static void f32_printMatrix(void *A, int dimI, int dimJ) {
    float *pA = (float *)A;
    float f;
    for (int i = 0; i < dimI; i++) {
        for (int j = 0; j < dimJ; j++) {
            printf_ex("%f ", pA[i * dimI + j]);
        }
        printf("\n");
    }
}
#endif

#endif