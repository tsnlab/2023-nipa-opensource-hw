#!/bin/bash
ARTYCONFIG=$CHIPYARD/fpga/src/main/scala/arty100t/Configs.scala
GEMDIR=$CHIPYARD/generators/gemmini
ROCCDIR=$CHIPYARD/generators/gemmini/software/gemmini/rocc-tests
sed -i '46s/50/20/' $ARTYCONFIG
sed -i '47s/50/20/' $ARTYCONFIG
sed -i '48s/new/\/\/new/' $ARTYCONFIG
sed -i '48 i new gemmini.CustomGemminiSoCConfig ++' $ARTYCONFIG
cp -f gemmini/gemmini.cc $GEMDIR/software/libgemmini
cp -f gemmini/CustomConfigs.scala $GEMDIR/src/main/scala/gemmini
cp -f gemmini/gemmini.h $ROCCDIR/include
cp -f gemmini/bfloat16.h $ROCCDIR/include
cp -f gemmini/printf.h $ROCCDIR/include
cp -f gemmini/op_bfloat16.c $ROCCDIR/bareMetalC
cp -f gemmini/tiled_matmul_bfloat16.c $ROCCDIR/bareMetalC
sed -i '55 i tiled_matmul_bfloat16 \\ /' $ROCCDIR/bareMetalC/Makefile
sed -i '56 i op_bfloat16 \\ /' $ROCCDIR/bareMetalC/Makefile

