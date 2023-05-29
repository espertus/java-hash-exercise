#!/usr/bin/env bash

echo "Building Autograder..."

zip -r autograder.zip src run_autograder setup.sh
mv autograder.zip zips/

echo "---"
echo "DONE. Locate it in the zips directory."
