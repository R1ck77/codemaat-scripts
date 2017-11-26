#!/bin/bash

FIRST_COMMIT=$1
LAST_COMMIT=$2
NORMALIZATION=$3

# current script location. Blessed be SO…
ME_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CODE_MAAT="java -jar ${ME_DIR}/code-maat-*-standalone.jar"
JSON_CONVERSION_DIR="${ME_DIR}/json-conversion"

if [ $NORMALIZATION -lt 1 ] 
then
    echo "Automatic normalization…"
else
    echo "${NORMALIZATION} normalization factor"
fi

GIT_HISTORY=`mktemp -t git_history_XXXX.txt`
CLOC_RESULT=`mktemp -t cloc_XXXX.csv`
CODEMAT_RESULTS=`mktemp -t codemaat_XXXX.csv`
MERGED_RESULTS=`mktemp -t merged_XXXX.csv`
JSON_RESULTS=${MERGED_RESULTS}.json

echo "* writing git output to ${GIT_HISTORY}…"
git log --pretty=format:'[%h] "%an" %ad %s' --date=short --numstat ${FIRST_COMMIT}..${LAST_COMMIT}  >${GIT_HISTORY}

echo "* printing some basic updates:"
${CODE_MAAT} -c git -a summary -l ${GIT_HISTORY}

echo "* extracting the salient bits with code maat in ${CODEMAT_RESULTS}"
${CODE_MAAT} -c git -a revisions -l ${GIT_HISTORY} >${CODEMAT_RESULTS}

echo "* checking the commit out…"
git checkout ${LAST_COMMIT}

echo "* cloc-ing the code in ${CLOC_RESULT}…"
cloc --by-file --csv --quiet . --report-file=${CLOC_RESULT}

echo "* merging the complexity and changes into ${MERGED_RESULTS}…"
pushd ${JSON_CONVERSION_DIR}
lein run merge ${CODEMAT_RESULTS} ${CLOC_RESULT} >${MERGED_RESULTS}
popd

echo "* converting the complexity into json to ${JSON_RESULTS}…"
pushd ${JSON_CONVERSION_DIR}
lein run jsonify ${MERGED_RESULTS} ${NORMALIZATION} >${JSON_RESULTS}
popd
