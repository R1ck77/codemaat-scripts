#!/bin/bash

# check some old vr theater files, just for sport
FIRST_COMMIT=`git rev-list --max-parents=0 HEAD`
LAST_COMMIT=19dec9277689c6a82e376b6c7fcb2b42a9a14fae
WATCHED_FILES=assets

GIT_HISTORY=`mktemp -t git_history_XXXX.csv`
CLOC_RESULT=`mktemp -t cloc_XXXX.csv`

echo "* writing git output to ${GIT_HISTORY}…"
git log --pretty=format:'[%h] "%an" %ad %s' --date=short --numstat ${FIRST_COMMIT}..${LAST_COMMIT} -- ${WATCHED_FILES}  >${GIT_HISTORY}

echo "* checking the commit out…"
git checkout ${LAST_COMMIT}

echo "* cloc-ing the code in ${CLOC_RESULT}…"
cloc --by-file --csv --quiet ${WATCHED_FILES} >${CLOC_RESULT}

echo "* let's print some codemat basic statistics…"
