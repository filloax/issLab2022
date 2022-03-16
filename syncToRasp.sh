#!/bin/bash

RASP_HOST=${1:-raspi}
rsync -av --exclude '.git' --exclude 'build/distributions' --exclude '.gitignore' --exclude 'RadarSystemConfig.json'  --exclude 'DomainSystemConfig.json' ./ "$RASP_HOST:issLab2022/"
