#!/usr/bin/env python2

import json
import re
import sys
from base64 import b64decode

chk = re.compile(r"^\s*[A-Za-z0-9+=]+\s*$")

for line in sys.stdin:
    try:
        x = json.loads(line)
        text = x['text']

        #remove hashtags and handles
        text = re.sub(r'\s[@#]\S+','',text)

        if not chk.match(text):
            continue


        dec = b64decode(x['text'])
        cars = set()
        for c in dec:
            cars.add(c)

        if len(cars) < 5:
            continue

        if not any(x.isalpha() for x in cars):
            continue

        x['b64decoded'] = dec
        sys.stdout.write(json.dumps(x) + "\n")

    except Exception:
        continue
