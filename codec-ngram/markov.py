from collections import defaultdict
from codec import Codec
from huffman import Huffman

class Markov(object):

    def __init__(self, ngrams):
        table = defaultdict(lambda: defaultdict(lambda: 0))
        for (ngram, count) in ngrams:
                state = ngram[:-1]
                step = ngram[-1:]
                self._table[state][step] += 1

        model = {}
        for (state, steps) in table.items():
            model[state] = Huffman(steps.items())

        self._state = "." * len(next(self._sums.keys()))

                

    

# Test code
test = Markov([(".a",1),("ab",1),("ac",1),("ba",1),("bc",1),("ca",1),("cb",1)])

