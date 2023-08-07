BUILDSBT=$(CHIPYARD)/build.sbt
ARTYCONFIG=$(CHIPYARD)/fpga/src/main/scala/arty100t/Configs.scala
all:
ifeq ($(CHIPYARD),)
	echo "Please set CHIPYARD variable to location of chipyard directory."
else
	sed -i "138 i npu," $(BUILDSBT)
	echo "\n" >> $(BUILDSBT)
	echo "lazy val npu = (project in file(\"generators/npu\"))" >> $(BUILDSBT)
	echo "  .dependsOn(rocketchip)" >> $(BUILDSBT)
	echo "  .settings(libraryDependencies ++= rocketLibDeps.value)" >> $(BUILDSBT)
	echo "  .settings(commonSettings)" >> $(BUILDSBT)
	cp -r npu $(CHIPYARD)/generators
	sed -i "36 i new npu.NPURoCCConfig ++" $(ARTYCONFIG)
endif
