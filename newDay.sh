#!/bin/bash

if [ -z "$1" ]; then
  echo "Please provide a day number as an argument."
  exit 1
fi

dayNumber=$(printf "%02d" $1)
sourceFileName="src/DayXX.kt"
targetFileName="src/Day${dayNumber}.kt"
inputFileName="src/Day${dayNumber}.txt"
testFileName="src/Day${dayNumber}_test.txt"

if [ ! -f "$sourceFileName" ]; then
  echo "Source file $sourceFileName does not exist."
  exit 1
fi

cp "$sourceFileName" "$targetFileName"
echo "Copied $sourceFileName to $targetFileName"

touch "$inputFileName"
touch "$testFileName"
echo "Created $inputFileName and $testFileName"

git add "$targetFileName" "$inputFileName" "$testFileName"
