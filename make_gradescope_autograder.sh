#!/usr/bin/env bash

echo "Building Autograder..."

zip -r autograder.zip src run_autograder setup.sh
mkdir -p zips
mv autograder.zip out/

echo "---"
echo "DONE. Find autograder.zip and MyString.java in the out directory."
