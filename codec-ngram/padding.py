from codec import *

class Padding(Codec):

    def encode(self, clear):
        stop = [False]
        def consume():
            yield from clear
            stop[0] = True
            yield 1
            while True:
                yield 0
        padded = consume()
        while not stop[0]:
            yield next(padded)


    def decode(self, clear):
        (zero, one) = (0,0)
        try:
            while True:
                while next(clear) == 1:
                    one += 1
                zero += 1
                while next(clear) == 0:
                    zero += 1
                yield from [1] * one   # TODO replace by something in constant space
                yield from [0] * zero
                one = 1
                zero = 0
        except StopIteration:
            yield from [1] * (one - 1)
