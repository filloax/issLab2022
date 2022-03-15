#!/bin/bash

RASP_HOST=${1:-raspi}
rsync -av --exclude '.git' --exclude '.gitignore'  --exclude 'DomainSystemConfig.json' ./ "$RASP_HOST:issLab2022/"
