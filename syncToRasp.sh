#!/bin/sh

rsync -av --exclude '.git' --exclude '.gitignore'  --exclude 'DomainSystemConfig.json' ./ raspi:issLab2022/ 
