from codec import Codec
from heapq import *
from itertools import count

class Huffman(Codec):
    def __init__(self, distribution):
        h = list(map(lambda x,y: (x[1],y,x[0]), distribution.items(), count()))
        heapify(h)
        while len(h) > 1:
            (ap,c,a) = heappop(h)
            (bp,_,b) = heappop(h)
            heappush(h, (ap+bp, c, (a,b)))
        
        self.tree = h[0][2]

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
