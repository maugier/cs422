#!/usr/bin/env python2

import json
import math
import itertools
import re
import sys
import unicodedata

NGRAM_LENGTH=3

def chartype(c):
    if c.isspace():
        return 0
    elif unicodedata.category(c)[0] == 'L':
        return 1
    else:
        return 2


def tokenize(s):
    # remove hashtags and handles
    s = re.sub(r'\s[@#]\S+','',s)

    # remove URLs and trailing spaces
    s = re.sub(r'http://\S+','',s).strip()

    for (k,g) in itertools.groupby(s, chartype):
        if k == 0:
            continue
        else:
            yield ''.join(g).lower()

if __name__ == "__main__":
  for line in sys.stdin:
    try:
        tweet = json.loads(line)
        if tweet['lang'] != u'en':
            continue

        text = tweet['text']

        #tokens = ([u'_NUL'] * (NGRAM_LENGTH - 1)) + list(tokenize(text)) + [u'_NUL']
        tokens = list(tokenize(text))

        #while(len(tokens) >= NGRAM_LENGTH):
        #   print("%s\t1" % u' '.join(tokens[0:NGRAM_LENGTH]).encode("utf8"))
        #   tokens = tokens[1:]
        for t in tokens:
            print("%s\t1" % t)

    except KeyError:
        continue
        # text was missing
    except ValueError:
        # json decoding failed ?
        continue
