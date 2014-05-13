from codec import Codec
from heapq import *
from itertools import count
import domain

class Huffman(Codec):
    def __init__(self, distribution, pad=False):
        h = list(map(lambda x,y: (int(x[1]),y,x[0]), distribution, count()))
        heapify(h)
        while len(h) > 1:
            (ap,c,a) = heappop(h)
            (bp,_,b) = heappop(h)
            heappush(h, (ap+bp, c, (a,b)))
        
        self.tree = h[0][2]
        self.pad = pad


        def mkcode(t,p,c):
            if isinstance(t, tuple):
                mkcode(t[0], p + (0,), c)
                mkcode(t[1], p + (1,), c)
            else:
                c[t] = p

        self.code = {}
        mkcode(self.tree, (), self.code)

    def real_encode(self, clear):
        for s in clear:
            yield from self.code[s]

    def encode(self, clear):
        if self.pad:
            yield from unpad(self.real_encode(clear))
        else:
            yield from self.real_encode(clear)

    def decode(self, symbols):
        t = self.tree

        if self.pad:
            symbols = padded(symbols)

        try:
            while True:
                while(isinstance(t, tuple)):
                    if next(symbols):
                        t = t[1]
                    else:
                        t = t[0]
                yield t
                t = self.tree
        except StopIteration:
            if t is self.tree: # decoding stopped on proper boundary
                raise 
            if not self.pad: # decoding stopped on improper boundary and padding
                             # was not requested:

                raise Exception("Input stream interrupted")

            while(isinstance(t, tuple)):
                t = t[0]

            yield t


def pad(padding, src):
    yield from src
    yield padding

def unpad(src):
    x = next(src)
    while True:
        (x,y) = (next(src), x)
        yield y

def HuffmanFile(f, **kw):
    with open(f, "r") as handle:
        return Huffman(line.split() for line in handle, **kw)    

def DNSHuffman(**kw):
    return Huffman(domain.histogram, **kw)
