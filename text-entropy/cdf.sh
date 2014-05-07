#!/bin/sh
/usr/bin/awk '{total += $2; print $1 " " total}'
