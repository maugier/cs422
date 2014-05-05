#!/usr/bin/env python2

import json
import math
import re
import sys
import unicodedata

NGRAM_LENGTH=4


def collapse(s):
    wasspace = False
    for c in s:
        if c == u' ':
            if not wasspace:
                wasspace = True
                yield '.'
        else:
            wasspace = False
            yield c



def letter(c):
    if unicodedata.category(c)[0] == 'L':
        return c.lower()
    else:
        return u' '


if __name__ == "__main__":
  for line in sys.stdin:
    try:
        tweet = json.loads(line)
        if tweet['lang'] != u'en':
            continue

        text = tweet['text']

        tokens = ([u'.'] * (NGRAM_LENGTH - 1)) + list(collapse(letter(c) for c in text)) + [u'.']

        while(len(tokens) >= NGRAM_LENGTH):
            print("%s\t1" % u''.join(tokens[0:NGRAM_LENGTH]).encode("utf8"))
            tokens = tokens[1:]

    except KeyError:
        continue
        # text was missing
    except ValueError:
        # json decoding failed ?
        continue
