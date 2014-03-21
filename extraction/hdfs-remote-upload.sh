#!/bin/bash

# Upload a single chunk to an HDFS path

PATH="/team16/tweets/"
BZ2="/bin/bzip2"
SSH="/usr/bin/ssh"

echo "$BZ2 | $SSH icdatasrv4.epfl.ch hadoop dfs -put - \"$PATH/$1.bz2\"" >&2
$BZ2 | $SSH icdatasrv4.epfl.ch hadoop dfs -put - "$PATH/$1.bz2"
