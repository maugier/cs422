from collections import defaultdict
from codec import Codec

class Markov(object):

    def __init__(self, ngrams):
        self._table = defaultdict(lambda: defaultdict(lambda: 0))
        for (ngram, count) in ngrams:
                state = ngram[:-1]
                step = ngram[-1:]
                self._table[state][step] += 1

        self._sums = {}
        for (state, steps) in self._table.items():
            self._sums[state] = sum(steps.values())

        self._state = "." * len(next(self._sums.keys()))

    @staticmethod
    def load(filename):
        with open(filename, "r") as f:
            def reader():
                for line in f:
                    yield line.split()
            return Markov(reader())
                

    

# Test code
test = Markov([(".a",1),("ab",1),("ac",1),("ba",1),("bc",1),("ca",1),("cb",1)])

