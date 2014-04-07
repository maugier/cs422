#!/usr/bin/env python

import sys

sum = 0
word = None

for line in sys.stdin:
    line = line.strip()
    (w,c) = line.split('\t', 1)
    c = int(c)
    if (w == word):
        sum += c
    else:
        if word is not None:     
            print ("%s\t%s" % (word, sum));
        sum = c
        word = w

if word:
    print ("%s\t%s" % (word,sum))
