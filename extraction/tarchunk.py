#!/usr/bin/env python3

import bz2
import io
import json
import tarfile
import sys

(good,bad,empty) = (0,0,0)

def open(tarname):
        """Opens a tar file containing bzip2-compressed chunks of lines containing
        JSON objects.

        Use as an iterator, like this:

        for obj in tarchunk.open("blah.tar"):
            print o['text']
             
        """
        global good, bad
        tar = tarfile.open(tarname, mode='r|*')

        for tarinfo in tar:

            name = tarinfo.name
            try:   
                obj = tar.extractfile(tarinfo)
                if obj is None:
                        continue

                for line in bz2.open(obj):
                    yield json.loads(line.decode('utf8'))
                
                good += 1

            except Exception as e:
                print("Choked on {0}: {1}".format(name, e))
                bad += 1



if __name__ == '__main__':
        write = sys.stdout.buffer.write
        for ar in sys.argv[1:]:
                for obj in open(ar):
                    print(obj)
        print ("Processed {0} good {1} bad".format(good,bad), file=sys.stderr)
