
import types

class Codec(object):
    """
    An abstract class representing a codec, as an invertible
    transform on generators. It is lazy and should run in constant space
    wrt. the size of the input.

    Codecs can be stacked with the / operator.

    The << and >> operators are shorthands for the .encode() and
    .decode() methods, that will in addition transform the returned
    generator into a list (for more convenient debugging.) Use encode
    and decode directly if you need the lazy behaviour.
    """

    def __init__(self):
        self.up = None
        self.down = None

    def encode(self, clear):
        """Encode some data.

        clear - an iterable over some kind of tokens
        returns an iterator over some other kind of token
        """
        raise Exception("Not implemented")

    def decode(self, cipher):
        """Decode some data.
        
        reciprocal of encode. Implementations should make sure
        that y = c.decode(c.encode(x)) is equivalent to
        y = iter(x)
        """

        raise Exception("Not implemented")

    def __div__(upper, lower):
        return CodecStack(upper, lower)

    def __truediv__(upper, lower):
        return CodecStack(upper, lower)

    def __lshift__(self, clear):
        out = self.encode(clear)
        if isinstance(out, types.GeneratorType):
            return list(out)
        else:
            return out

    def __rshift__(self, cipher):
        out = self.decode(cipher)
        if isinstance(out, types.GeneratorType):
            return list(out)
        else:
            return out

class CodecStack(Codec):
    """Represents the result of stacking two codecs.
    
    a / b is equivalent to CodecStack(a,b)
    """
    def __init__(self, upper, lower):
        self.upper = upper
        self.lower = lower

    def encode(self, clear):
        return self.lower.encode(self.upper.encode(clear))

    def decode(self, cipher):
        return self.upper.decode(self.lower.decode(cipher))


class Reverse(Codec):
    """A reversed codec.

    This wrapper will swap the use of encode() and decode.()
    """
    def __init__(self, rev):
        self.rev = rev

    def encode(self, clear):
        return self.rev.decode(clear)

    def decode(self, cipher):
        return self.rev.encode(cipher)

class Ascii(Codec):
    """Codec that transforms bytes into their integer values."""
    def __init__(self):
        self.up = str
        self.down = int

    def encode(self, clear):
        for s in clear:
            yield ord(s)
    def decode(self, cipher):
        for c in cipher:
            yield chr(c)

class UTF8(Codec):
    """Codec that transforms characters into the integer values
    of the bytes of its UTF8 encoding"""
    def encode(self, clear):
        return (x for c in clear for x in c.encode('utf8'))

    def decode(self, cipher):
        return bytes(cipher).decode("utf8")
        

class Binary(Codec):
    """Codec that expands integers into fixed-length binary expansions."""
    def __init__(self, bits=8, big_endian=False):
        """Builds a Binary codec.

        bits: fixed number of bits to expand to.
        big_endian: expand the number MSB first instead of LSB first.
        """

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
        src = iter(cipher)
        while True:
            a = 1
            c = 0
            for i in range(0, self.bits):
                c += next(src)*a
                a *= 2
            yield c


class RLE(Codec):
    """Run Length Encoding. 

    Encodes any type of symbol (implementing equality) into an alternating
    stream of symbols and integers.
    """
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
    """Word splitter.

    Encodes a list of tokens into a string of space-separated tokens.

    Will split back on whitespace when decoding.
    """
    def encode(self, clear):
        return ' '.join(clear)

    def decode(self, cipher):
        yield from cipher.split()
