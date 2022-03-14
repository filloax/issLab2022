#!/bin/sh

rsync -av  --exclude 'DomainSystemConfig.json' raspi:issLab2022/ ./
