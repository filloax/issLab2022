#!/bin/sh

RASP_HOST=${1:-raspi}
rsync -av  --exclude 'DomainSystemConfig.json' "$RASP_HOST:issLab2022/" ./
