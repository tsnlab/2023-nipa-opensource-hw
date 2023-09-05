BUILDSBT=$(CHIPYARD)/build.sbt
ARTYCONFIG=$(CHIPYARD)/fpga/src/main/scala/arty100t/Configs.scala

chipyard:
	git clone https://github.com/ucb-bar/chipyard.git
	pushd chipyard
	git checkout 1.9.1
	./build-setup.sh riscv-tools
	popd

fetch-gemmini:
	pushd $(CHIPYARD)
	source env.sh
	cd generators/gemmini
	git config remote.origin.fetch "+refs/heads/*:refs/remotes/origin/*"
	git fetch && git checkout v0.7.1
	git submodule update --init --recursive
	popd

alter-files:
	@GEMDIR=$(CHIPYARD)/generators/gemmini
	@ROCCDIR=$(CHIPYARD)/generators/gemmini/software/gemmini/rocc-tests
	sed -i '46s/50/20' $(ARTYCONFIG)
	sed -i '47s/50/20' $(ARTYCONFIG)
	sed -i '48s/new/\/\/new' $(ARTYCONFIG)
	sed -i '48 i new gemmini.CustomGemminiSoCConfig ++'
	cp -f gemmini/gemmini.cc $(GEMDIR)/software/libgemmini
	cp -f gemmini/CustomConfigs.scala $(GEMDIR)/src/main/scala/gemmini
	cp -f gemmini/gemmini.h $(ROCCDIR)/include
	cp -f gemmini/bfloat16.h $(ROCCDIR)/include
	cp -f gemmini/printf.h $(ROCCDIR)/include
	cp -f gemmini/op_bfloat16.c $(ROCCDIR)/bareMetalC
	cp -f gemmini/tiled_matmul_bfloat16.c $(ROCCDIR)/bareMetalC
	sed -i '55 tiled_matmul_bfloat16 \\' $(ROCCDIR)/bareMetalC/Makefile
	sed -i '56 op_bfloat16 \\' $(ROCCDIR)/bareMetalC/Makefile
	
build-gemmini:
	pushd $(CHIPYARD)/generators/gemmini
	make -C software/libgemmini install
	./scripts/build-spike.sh
	./scripts/setup-paths.sh
	cd software/gemmini-rocc-tests
	./build.sh
	popd

run-waveform:
	pushd $(CHIPYARD)/generators/gemmini
	./scripts/build-verilator.sh --debug
	./scripts/run-verilator.sh $(CHIPYARD)/generators/gemmini/software/gemmini/rocc-tests/build/bareMetalC/op_bfloat16-baremetal

all: fetch-gemmini alter-files build-gemmini run-waveform