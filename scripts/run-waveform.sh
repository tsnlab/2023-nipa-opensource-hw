#!/bin/bash
pushd $CHIPYARD/generators/gemmini
./scripts/build-verilator.sh --debug
./scripts/run-verilator.sh $CHIPYARD/generators/gemmini/software/gemmini/rocc-tests/build/bareMetalC/op_bfloat16-baremetal
popd
