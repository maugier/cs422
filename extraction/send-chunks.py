#!/usr/bin/env python3

import argparse
from chunked import chunked
import subprocess
import tarchunk


parser = argparse.ArgumentParser( description="Resize tar chunks and send them to some command")
parser.add_argument('-s', '--size', metavar="N", help='chunk size in number of tweets', default=50000);
parser.add_argument('command', metavar="COMMAND", help='command to invoke to process chunks. Chunk number will be passed as argument, and data provided on stdin.')
parser.add_argument('files', metavar="TARFILE", nargs='+', help="tarfiles to unpack and repack at the adequate size")

args = parser.parse_args()

count = 0;
chunk = 0;

splitCount = 1000000

def alltweets():
    for tf in args.files:
        yield from tarchunk.open(tf, nojson=True)
                
count = 0
for chunk in chunked(alltweets(), size=splitCount):
    p = subprocess.Popen([args.command, str(count).zfill(5)],
                         stdin=subprocess.PIPE, )
    for line in chunk:
        p.stdin.write(line)

    p.stdin.close()
    p.wait()

    count += 1


