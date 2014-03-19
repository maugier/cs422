import argparse
import tarchunk
from collections import Counter

parser = argparse.ArgumentParser( description="Compute tweet language distribution")
parser.add_argument('files', metavar="TARFILE", nargs='*', help="tarfiles to analyze")

args = parser.parse_args()

langs = Counter()
total = 0;

for tf in args.files:
        for tweet in tarchunk.open(tf):
            if 'lang' in tweet:
                langs[tweet['lang']] += 1
            total += 1

print("Total: {0}".format(total))
print(langs)
