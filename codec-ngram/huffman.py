from codec import Codec
from heapq import *
from itertools import count

class Huffman(Codec):
    """Huffman codec, encodes a stream of symbols to a stream of bits.

    distribution: an iterable over pairs of (symbol, frequency) used
    to build the compression tree.

    pad: when decoding, pad the input stream with zeros if reqired. If
    set to false (default), decoding will raise an exception if the bitstream
    does not stop on symbol boundaries.

    Example:
    h = Huffman([('a',5),('b',2),('c',3)])
    """
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

    def encode(self, clear):
        for s in clear:
            yield from self.code[s]

    def decode(self, symbols):
        t = self.tree
        src = iter(symbols)
        try:
            while True:
                while(isinstance(t, tuple)):
                    if next(src):
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

def HuffmanFile(f, **kw):
    """Build a Huffman code from frequencies contained in a file.

    f: filename to read. Expected to point to a text file of lines
    containing a whitespace-separated pair of symbol and frequency.

    other options (like pad) are passed to the underlying Huffman object.
    """
    with open(f, "r") as handle:
        return Huffman(line.split() for line in handle, **kw)    

def DNSHuffman(**kw):
    """Huffman codec suitable for encoding of domain names."""
    import domain
    return Huffman(domain.histogram, **kw)
