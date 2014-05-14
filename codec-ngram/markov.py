from collections import defaultdict
from codec import Codec
from huffman import Huffman

class Markov(Codec):

    def __init__(self, ngrams):
        statelen = None
        table = defaultdict(lambda: defaultdict(lambda: 0))
        for (ngram, count) in ngrams:
                state = ngram[:-1]
                step = ngram[-1:]
                if statelen is None:
                    statelen = len(state)
                elif len(state) != statelen:
                    raise("Inconsistent state size in input ({0} vs {1})".format(len(state), statelen))


                table[state][step] += 1

        self.state = "." * statelen
        model = {}
        for (state, steps) in table.items():
            model[state] = Huffman(steps.items())

        self.model = model

    
    def encode(self, clear):
        state = self.state
        for c in clear:
            yield from self.model[state].encode((c,))
            state = state[1:] + c

    def decode(self, cipher):
        stop = [False]
        def wrap(cipher):
            yield from cipher
            stop[0] = True
            while True:
                yield 0

        state = self.state
        src = wrap(cipher)
        while not stop[0]:
            c = next(self.model[state].decode(src))
            state = state[1:] + c
            yield c
    

# Test code
test = Markov([(".a",1),("ab",1),("ac",1),("ba",1),("bc",1),("ca",1),("cb",1)])


def NGramMarkov(filename):
    with open(filename, "r") as h:
        return Markov(l.split() for l in h)

def TokenMarkov(filename):
    def process(line):
        words = line.split()
        return (words[:-1], words[-1])
    with open(filename, "r") as h:
        return Markov(process(line) for line in h)
