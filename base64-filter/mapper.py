#!/usr/bin/env python2

import json
import re
import sys

from collections import Counter


def histogram(s): 
    h = Counter();
    for letter in s:
        h[letter] += 1
    return h

pat = re.compile("^[A-Za-z0-9+/]+={0,2}$")

for line in sys.stdin:
    try:
        x = json.loads(line)
        # = histogram(x['text'])
        #dna = (1.0 * sum(h[c] for c in "ATGCatgc")) / len(x['text']) 
        #if dna > 0.5:
        text = x['text']
        if len(text) > 16 and pat.match(text):
            sys.stdout.write(line)

    except KeyError:
        continue
        # text was missing
    except ValueError:
        # json decoding failed ?
        continue
