#!/usr/local/bin/python

import json
import sys

for line in sys.stdin:
    try:
        x = json.loads(line)
        print '%s\t%s' % (x['lang'], 1)
    except Exception:
        continue

