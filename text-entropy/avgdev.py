#!/usr/bin/env python3

def square(x):
    return x*x

class AvgDev (Object) :
    def __init__(self):
        self.s1 = 0
        self.s2 = 0
        self.c = 0

    def __iadd__(self, val):
        self.s1 += val
        self.s2 += val*val
        self.c  += 1
        return self

    def addmany(self,val,weight):
        self.s1 += val*weight
        self.s2 += val*val*weight
        self.c += weight

    def avg(self):
        return self.s1 / self.c

    def var(self):
        return (self.s2 / self.c) - square(self.avg())




import sys

avg = {}
var = {}
over = {}

for line in sys.stdin:
    (lang,ent,count) = line.split()
    over[lang] = over.get(lang,0) + int(count)
    avg[lang] = avg.get(lang,0) + (float(count)*float(ent))

for l in avg:
    print("{0}: {1}".format(l, avg[l] / over[l]));
