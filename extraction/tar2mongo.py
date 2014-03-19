import argparse
import tarchunk
import mongo

parser = argparse.ArgumentParser( description="Put tarred tweets in mongodb")
parser.add_argument('-h', '--host', help="MongoDB host")
parser.add_argument('-p', '--port', help="MongoDB port")
parser.add_argument('-d', '--database', help="MongoDB database")
parser.add_argument('files', metavar="TARFILE", nargs='*', help="tarfiles to put to mongo")

args = parser.parse_args()

conn = pymongo.MongoClient(**(args.__dict__))

db = conn[args.database)

for tf in args.files:
	for tweet in tarchunk.open(tf):
		db.insert(tweet)
