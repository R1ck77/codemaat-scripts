#!/bin/bash

# current script location. Blessed be SO…
ME_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CODE_MAAT="java -jar ${ME_DIR}/code-maat-*-standalone.jar"

# check some old vr theater files, just for sport
FIRST_COMMIT=`git rev-list --max-parents=0 HEAD`
LAST_COMMIT=19dec9277689c6a82e376b6c7fcb2b42a9a14fae
WATCHED_FILES=assets

GIT_HISTORY=`mktemp -t git_history_XXXX.txt`
CLOC_RESULT=`mktemp -t cloc_XXXX.csv`
CODEMAT_RESULTS=`mktemp -t codemaat_XXXX.csv`

echo "* writing git output to ${GIT_HISTORY}…"
git log --pretty=format:'[%h] "%an" %ad %s' --date=short --numstat ${FIRST_COMMIT}..${LAST_COMMIT} -- ${WATCHED_FILES}  >${GIT_HISTORY}

echo "* extracting the salient bits with code maat in ${CODEMAT_RESULTS}"
${CODE_MAAT} -c git -a revisions -l ${GIT_HISTORY} >${CODEMAT_RESULTS}

echo "* checking the commit out…"
git checkout ${LAST_COMMIT}

echo "* cloc-ing the code in ${CLOC_RESULT}…"
cloc --by-file --csv --quiet ${WATCHED_FILES} --report-file=${CLOC_RESULT}

echo "* let's print some codemat basic statistics…"
python ${ME_DIR}/scripts\ 4/merge_comp_freqs.py ${CODEMAT_RESULTS} ${CLOC_RESULT}
