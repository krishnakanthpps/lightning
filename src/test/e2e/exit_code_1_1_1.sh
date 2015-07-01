#!/bin/bash

java \
    -jar target/lightning*.jar \
    -xml src/test/resources/xml/1_1_1.xml \
    -csv src/test/resources/csv/10_transactions.csv \
    > src/test/resources/results/actual/1_1_1.txt
OUT=$?

echo "EXIT CODE TEST"
if [ $OUT -eq 2 ];then
    echo "EXIT CODE = $OUT"
    echo "TEST PASSED"
    exit 0
else
    echo "EXIT CODE = $OUT"
    echo "TEST FAILED"
    exit 1
fi