#!/bin/sh

RASP_HOST=${1:-raspi}
rsync -av  --exclude 'DomainSystemConfig.json' --exclude 'RadarSystemConfig.json'  "$RASP_HOST:issLab2022/" ./
