import sys
import RPi.GPIO as GPIO
from time import sleep

repeats = 5
if len(sys.argv) > 1:
    repeats = int(sys.argv[1])

GPIO.setmode(GPIO.BCM)
GPIO.setup(25,GPIO.OUT)

for line in sys.stdin:
    print(line)
    v = float(line)
    if v <= 20:
        if repeats == 0:
            GPIO.output(25, GPIO.HIGH)
        else:
            for i in range(repeats):
                GPIO.output(25, GPIO.HIGH)
                sleep(0.1)
                GPIO.output(25, GPIO.LOW)
                sleep(0.1)
    else:
        GPIO.output(25, GPIO.LOW)
        


