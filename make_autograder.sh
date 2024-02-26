#!/usr/bin/env bash

echo "Building Autograder..."

zip -r autograder.zip src run_autograder setup.sh
mkdir -p zips
mv autograder.zip zips/

echo "---"
echo "DONE. Find it in the zips directory."
