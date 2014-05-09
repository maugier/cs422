all: domain.py

domain.py: top500.txt histogram
	./histogram < $< > $@

top500.txt: top500.csv
	./csv2txt $< > $@

histogram: histogram.hs
	ghc -O3 --make $<


