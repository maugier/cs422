
def chunked(gen, chunkSize = 50000):
    i = iter(gen)
    go = [True]  # yes, disgusting scoping hack
    while go[0]:
        def f() :
            for x in range(0, chunkSize):
                try:
                    yield next(i)
                except StopIteration:
                    go[0] = False
                    raise
        yield f()
