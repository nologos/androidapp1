#!/usr/bin/env python3
import subprocess
import logging as log
import sys

log.basicConfig(level=log.INFO)
# current commit
with open("app/build.gradle") as f:
    for line in f:
        if "versionName" in line:
            current_commit_version = line.split()[1].strip('"')
            break
log.info(current_commit_version)


# previous commit
output = subprocess.check_output(['git', 'show', 'HEAD~1:app/build.gradle'])
lines = output.decode().split('\n')
version_line = [line for line in lines if 'versionName' in line][0]
previous_commit_version = version_line.split()[1].strip('"')
log.info(previous_commit_version)

if current_commit_version != previous_commit_version:
    log.info("new version different")
    sys.exit(1)
else:
    log.info("even version")
    sys.exit(0)


# configure followup job if $? == 1 to run the CD workflow