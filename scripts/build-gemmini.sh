#!/bin/bash
pushd $CHIPYARD/generators/gemmini
make -C software/libgemmini install
./scripts/build-spike.sh
./scripts/setup-paths.sh
cd software/gemmini-rocc-tests
./build.sh
popd
