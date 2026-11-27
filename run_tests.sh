#!/bin/sh
# Run from the project folder:  sh run_tests.sh
javac *.java || exit 1
failed=0
for input in tests/*.txt; do
    expected="${input%.txt}.expected"
    if java Main < "$input" | diff --strip-trailing-cr "$expected" - ; then
        echo "PASS $input"
    else
        echo "FAIL $input"
        failed=1
    fi
done
exit $failed
