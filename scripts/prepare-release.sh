#!/usr/bin/env bash
# Invoked by @semantic-release/exec (prepareCmd) with the new version already
# decided by commit-analyzer. Sets that version on the parent, domain and
# server poms in CI's working tree and builds the .deb with that name; nothing
# is committed, since this repo's ruleset blocks the bot from pushing to main.
set -euo pipefail

VERSION="$1"

mvn -B org.codehaus.mojo:versions-maven-plugin:2.22.0:set \
  -DnewVersion="${VERSION}" \
  -DprocessAllModules=true \
  -DgenerateBackupPoms=false

mvn -B -pl server -am package
