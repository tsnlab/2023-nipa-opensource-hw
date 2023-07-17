#ifndef BFLOAT16_H
#define BFLOAT16_H

#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>

typedef uint16_t bfloat16;
#define BFLOAT16_EPSILON 0.0078125

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

static void mass_bf16_to_f32(bfloat16 * bfs, float *fs, int count) {
    for (int i = 0; i < count; i++) {
        fs[i] = bf16_to_f32(bfs[i]);
    }
}

static void mass_f32_to_bf16(float *fs, bfloat16 * bfs, int count) {
    for (int i = 0; i < count; i++) {
        bfs[i] = f32_to_bf16(fs[i]);
    }
}

static void bf16_matset(bfloat16 *A[], bfloat16 (*setf)(), int dim) {
    bfloat16 *pA = (bfloat16 *)A;
    for (int i = 0; i < dim * dim; i++) {
        *pA++ = setf();
    }
}

static void bf16_matscale(bfloat16 *A[], float scale, int dim) {
    bfloat16 *pA = (bfloat16 *)A;
    for (int i = 0; i < dim * dim; i++) {
        *pA++ = f32_to_bf16(bf16_to_f32(*pA++) * scale);
    }
}

static void bf16_matrelu(bfloat16 *A[], float scale, int dim) {
    bfloat16 *pA = (bfloat16 *)A;
    for (int i = 0; i < dim * dim; i++) {
        *pA = f32_to_bf16(bf16_to_f32(*pA++));
        if (*pA < 0) *pA = 0;
        pA++;
    }
}

static bool bf16_matequal(bfloat16 *A[], bfloat16 *B[], int dim) {
    bfloat16 *pA = (bfloat16 *)A, *pB = (bfloat16 *)B;
    for (int i = 0; i < dim * dim; i++) {
        if (!bf16_equal(*pA++, *pB++)) return false;
    }
    return true;
}

static void bf16_matadd(bfloat16 *A[], bfloat16 *B[], bfloat16 *C[], int dim) {
    bfloat16 *pA = (bfloat16 *)A, *pB = (bfloat16 *)B, *pC = (bfloat16 *)C;
    for (int i = 0; i < dim * dim; i++) {
        *pC++ = f32_to_bf16(bf16_to_f32(*pA++) + bf16_to_f32(*pB++));
    }
}

// buffer needs to be 8*dim^2 bytes
static void bf16_matmul(bfloat16 *A[], bfloat16 *B[], bfloat16 *D[], bfloat16 *C[], int dim, void *buffer) {
    int dimsq = dim*dim;
    float *fa = (float *)buffer;
    float *fb = fa + dimsq;
    float accum, mul;
    bfloat16 *pC = (bfloat16 *)C, *pD = (bfloat16 *)D;
    
    mass_bf16_to_f32((bfloat16 *)A, fa, dimsq);
    mass_bf16_to_f32((bfloat16 *)B, fb, dimsq);

    for (int i = 0; i < dim; i++) {
        for (int j = 0; j < dim; j++) {
            accum = bf16_to_f32(pD[i * dim + j]);
            for (int k = 0; k < dim; k++) {
                accum += fa[i * dim + k] * fb[k * dim + j];
            }
            pC[i * dim + j] = f32_to_bf16(accum);
        }
    }
}

#ifndef NO_PRINT_BF16
#include "include/printf.h"

static void bf16_printMatrix(bfloat16 *A[], int dim) {
    bfloat16 *pA = (bfloat16 *)A;
    for (int i = 0; i < dim; i++) {
        for (int j = 0; j < dim; j++) {
            printf_ex("%r ", pA[i * dim + j]);
        }
        printf("\n");
    }
}

static void f32_printMatrix(float *A[], int dim) {
    float *pA = (float *)A;
    float f;
    for (int i = 0; i < dim; i++) {
        for (int j = 0; j < dim; j++) {
            printf_ex("%f ", pA[i * dim + j]);
        }
        printf("\n");
    }
}
#endif

#endif