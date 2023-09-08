#!/bin/bash
pushd $CHIPYARD
source env.sh
cd generators/gemmini
git config remote.origin.fetch "+refs/heads/*:refs/remotes/origin/*"
git fetch && git checkout v0.7.1
git submodule update --init --recursive
popd
