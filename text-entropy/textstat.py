#!/usr/bin/env python3

import sys
from math import sqrt

avg = {}
var = {}
over = {}

for line in sys.stdin:
    (lang,ent,count) = line.split()
    over[lang] = over.get(lang,0) + int(count)
    avg[lang] = avg.get(lang,0) + (float(count)*float(ent))
    var[lang] = var.get(lang,0) + (float(count)*float(ent)*float(ent))

for l in avg:
    a = avg[l] / over[l]
    d = sqrt(var[l] / over[l] - a*a)
    print("{0}: {1} {2}".format(l,a,d));

