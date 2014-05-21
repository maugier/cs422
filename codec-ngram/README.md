Natural Language Stealth Channel
================================

Building
--------

There is no python packaging at the moment, so the main code doesn't need to be
built yet.

If you want to rebuild the histogram for Domain Names Huffman, run `make`
to recreate domain.py (requires a haskell compiler, sorry :) from top500.txt, 
which must be a text file of sample domain names. top500.txt will be built from top500.csv if needed, which was downloaded from http://moz.com/top500/domains/csv

Getting data
------------

To use the Markov codec you need data files. They were not included in the repo because they are somewhat large.

For ngram-based Markov you need a file in such a format:


    ..a 10
    ..b 8
    .ab 4
    abc 3
    abb 3
    ab. 1
    etc...


The dot is the sentinel byte for start and end of sentence.

For token-based Markov you need a file with a similar format with whitespace-separated tokens instead of bytes, and `_NUL` as the sentinel token, like so:

    _NUL hello 10
    _NUL i 10
    hello world 5
    hello there 3
    i am 3
    i will 2
    will _NUL 1
    will not 2
    etc...


You can download our sample data at http://lacalsrv6.epfl.ch/cs422. Uncompress
the files with

    unxz *.xz


Running
-------

You will need to figure out your own codec combinations, but a sample generic
one for a steganographic channel would be

    import codec
    import huffman
    import markov

    rtm = codec.Reverse(markov.TokenMarkov("ntokens"))

    u8b = codec.UTF8() / codec.Binary()

    classic_code = u8b / rtm / codec.Words()
    dns_code = huffman.DNSHuffman() / codec.Padding() / rtm / Words()

    rcm = codec.Reverse(markov.NGramMarkov("ngrams"))

    charbased_generic_code = u8b / rcm 

    simple_token_huffman = u8b / huffman.FileHuffman("top1000tokens")

Note that loading a large datafile requires significant memory and will take
some time. Don't panic and watch for resource usage if you are afraid the code
is hung.
