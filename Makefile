SHELL := /bin/bash

fetch-gemmini:
	source scripts/fetch-gemmini.sh

alter-files:
	source scripts/alter-files.sh
	
build-gemmini:
	source scripts/build-gemmini.sh

run-waveform:
	source scripts/run-waveform.sh

all: fetch-gemmini alter-files build-gemmini run-waveform
