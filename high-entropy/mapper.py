#!/usr/bin/env python2

import json
import math
import re
import sys


def entropy(seq):
    bins = {}
    total = 0
    for x in seq:
        bins[x] = 1 + bins.get(x,0)
        total += 1

    return 0 - sum((float(p) / total) * math.log(float(p) / total, 2) for p in bins.itervalues())


if __name__ == "__main__":
  bins = {}
  for line in sys.stdin:
    try:
        tweet = json.loads(line)
        e = entropy(tweet['text'])
        if e > 6.6:
            sys.stdout.write(line)

    except KeyError:
        continue
        # text was missing
    except ValueError:
        # json decoding failed ?
        continue
