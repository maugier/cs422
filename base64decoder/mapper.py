#!/usr/bin/env python2

import json
import sys
from base64 import b64decode

for line in sys.stdin:
    try:
        x = json.loads(line)
        text = x['text']
        dec = b64decode(x['text'])
        cars = set()
        for c in dec:
            cars.add(c)

        if len(cars) > 4:
            x['b64decoded'] = dec
            sys.stdout.write(json.dumps(x) + "\n")

    except Exception:
        continue
