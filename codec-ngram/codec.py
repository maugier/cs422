
class Codec(object):
    def __init__(self):
        self.up = None
        self.down = None

    def encode(self, clear):
        raise Exception("Not implemented")

    def decode(self, cipher):
        raise Exception("Not implemented")

    def __div__(upper, lower):
        return CodecStack(upper, lower)

    def __truediv__(upper, lower):
        return CodecStack(upper, lower)

    def __lshift__(self, clear):
        return self.encode(iter(clear))

    def __rshift__(self, cipher):
        return self.decode(iter(cipher))

class CodecStack(Codec):
    def __init__(self, upper, lower):
        self.upper = upper
        self.lower = lower

    def encode(self, clear):
        return self.lower.encode(self.upper.encode(clear))

    def decode(self, cipher):
        return self.upper.decode(self.lower.decode(cipher))


class Reverse(Codec):
    def __init__(self, rev):
        self.rev = rev

    def encode(self, clear):
        return self.rev.decode(clear)

    def decode(self, cipher):
        return self.rev.encode(cipher)

class Unicode(Codec):
    def __init__(self):
        self.up = str
        self.down = int

    def encode(self, clear):
        for s in clear:
            yield ord(s)
    def decode(self, cipher):
        for c in cipher:
            yield unichr(c)
        

class Binary(Codec):
    def __init__(self, bits=8, big_endian=False):
        if big_endian:
            raise Exception("Big Endian not supported yet")

        self.bits = bits
        self.up = int
        self.down = 'bit'

    def encode(self, clear):
        for c in clear:
            for i in range(0,self.bits):
                yield (c % 2)
                c = c // 2

    def decode(self, cipher):
        while True:
            a = 1
            c = 0
            for i in range(0, self.bits):
                c += next(cipher)*a
                a *= 2
            yield c


class RLE(Codec):
    def encode(self, clear):
        current = None
        count = 0
        
        try:
            while True:
                n = next(clear)
                if n == current:
                    count += 1
                else:
                    if count > 0:
                        yield count
                        yield current
                    count = 1
                    current = n
        except StopIteration:
            if count > 0:
                yield count
                yield current
            raise

    def decode(self, cipher):
        count = next(cipher)
        symbol = next(cipher)
        for i in range(0,count):
            yield symbol

class Words(Codec):
    def encode(self, clear):
        return ' '.join(clear)

    def decode(self, cipher):
        yield from cipher.split()
