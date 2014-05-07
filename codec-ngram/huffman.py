from codec import Codec
from heapq import *

class Huffman(Codec):
    def __init__(self, distribution):
        h = list(map(lambda x: (x[1],x[0]), distribution.items()))
        heapify(h)
        while len(h) > 1:
            (ap,a) = heappop(h)
            (bp,b) = heappop(h)
            heappush(h, (ap+bp, (a,b)))
        
        self.tree = h[0][1]

        def mkcode(t,p,c):
            if isinstance(t, tuple):
                mkcode(t[0], p + (0,), c)
                mkcode(t[1], p + (1,), c)
            else:
                c[t] = p

        self.code = {}
        mkcode(self.tree, (), self.code)

            
    def encode(self, clear):
        for s in clear:
            yield from self.code[s]

    def decode(self, symbols):
        while True:
            t = self.tree
            while(isinstance(t, tuple)):
                if next(symbols):
                    t = t[1]
                else:
                    t = t[0]
            yield t
