#!/usr/bin/env bash
echo 'Sending Discord Webhook';
export BACKTICK='`';
export TIMESTAMP=$(date --utc +%FT%TZ);
export COMMIT_FORMATTED="[$BACKTICK${TRAVIS_COMMIT:0:7}$BACKTICK](https://github.com/OldaranT/EllirionCore/commit/$TRAVIS_COMMIT)";
declare -A authors=( ["OldaranT"]="224691335481786368" ["Chris Coerdes"]="194476736996573185" ["nikro412956"]="202843805857218560" ["RagbeccaPlebs"]="127701720187928576" )
curl -v -H User-Agent:bot -H Content-Type:application/json -d '{"avatar_url":"https://i.imgur.com/kOfUGNS.png","username":"Travis CI","content":"<@'${authors[$AUTHOR_NAME]}'>","embeds":[{"author":{"name":"Build #'"$TRAVIS_BUILD_NUMBER"' Passed - '"$AUTHOR_NAME"'","url":"https://travis-ci.com/'"$REPO_OWNER"'/'"$REPO_NAME"'/builds/'"$TRAVIS_BUILD_ID"'"},"url":"https://github.com/'"$REPO_OWNER"'/'"$REPO_NAME"'/commit/'"$TRAVIS_COMMIT"'","title":"['"$TRAVIS_REPO_SLUG"':'"$TRAVIS_BRANCH"'] ","color":65280,"fields":[{"name":"_ _", "value": "'"$COMMIT_FORMATTED"' - '"$TRAVIS_COMMIT_MESSAGE"'"}],"timestamp":"'"$TIMESTAMP"'","footer":{"text":"Travis CI"}}]}' $DISCORD_WEBHOOK_URL;

