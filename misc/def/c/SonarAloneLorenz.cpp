#include <iostream>
#include <pigpio.h>
#include <fstream>
#include <cmath>


#define TRIG 2
#define ECHO 3

#define CLOSE 18
#define MEDIUM 21
#define FAR 60

#define POS_LEFT 0.055
#define POS_RIGHT 0.24
#define POS_FORWARD 0.14
using namespace std;
/*
g++  SonarAlone.c -l wiringPi -o  SonarAlone
 */
void setup() {
	cout << "setUp " << endl;
	if(gpioInitialise()<0){exit(1);}
	gpioSetMode(TRIG, PI_OUTPUT);
	gpioSetMode(ECHO, PI_INPUT);
	//TRIG pin must start PI_OFF
	gpioWrite(TRIG, PI_OFF);
	gpioDelay(30);
}

int getCM() {
	//Send trig pulse
	gpioWrite(TRIG, PI_ON);
	gpioDelay(10);
	gpioWrite(TRIG, PI_OFF);
	auto now = gpioTick();
	auto startTime = gpioTick();
	
	
	//Wait for echo start
	while(gpioRead(ECHO) == PI_OFF && startTime-now<60000){
		startTime = gpioTick();
	}
	now = gpioTick();
	auto endTime = gpioTick();
	while(gpioRead(ECHO) == PI_ON && endTime-now<60000){
		endTime = gpioTick();
	}

	long travelTime = endTime - startTime;

	//Get distance in cm
	int distance = travelTime / 58;

	return distance;
}

int main(void) {
	int cm ;
	setup();
	while(1) {
 		cm = getCM();
		//cout << "distance(" << cm  << ",forward,front)" << endl;
//		if (cm != 0){
			cout <<  cm   << endl;
//		}
		gpioDelay(30);
	}
 	return 0;
}
