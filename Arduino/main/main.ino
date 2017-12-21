#include <LCD.h>
#include <LiquidCrystal_I2C.h>
#include <NewPing.h>
#include <SimpleTimer.h>
#include <Wire.h>
#include "Motor.h"
#include "SevenSegment.h"

//////////////////////////
// VARIABLE DECLARATION //
//////////////////////////

SimpleTimer timer;
char serialData, prevSerialData = 0x00;

// LCD
const int lcdAddr = 0x3f;
const int rows = 2;
const int cols = 16;
LiquidCrystal_I2C lcd(lcdAddr, 2, 1, 0, 4, 5, 6, 7); // Magic Number
char stringBuffer[16] = "";

// Motor
const int leftFwd = 4;
const int leftEna = 10;
const int leftBwd = 5;
const int rightFwd = 6;
const int rightEna = 11;
const int rightBwd = 7;
Motor leftMotor(leftEna, leftFwd, leftBwd);
Motor rightMotor(rightEna, rightFwd, rightBwd);
int motorTimer;
bool slowMotors;

// Seven Segment
const int latchPin = 12;
const int clockPin = 8;
const int dataPin = 9;
SevenSegment sSegment(latchPin, clockPin, dataPin);
int sSegData[4] = {0b00000000, 0b10111010, 0b11100110, 0b00100010}; // clear, d, P, r

// Switch
const int switchPin = 2;
volatile bool switchState = true;

// Ultrasonic
const int trigPin = A0;
const int echoPin = A1;
NewPing sonar(trigPin, echoPin, 500);


//////////////////////////////////
// FUNCTION FORWARD DECLARATION //
//////////////////////////////////

void checkSwitch();
void checkMotors();
void moveMotors();
void slowDownMotors();
void updateUltrasonic();
void updateSevenSegment();


//////////////////
// MAIN PROGRAM //
//////////////////

void setup() {
  Serial.begin(9600);
  serialData = 0x00;

  // LCD
  lcd.begin(cols, rows);
  lcd.setBacklightPin(3, POSITIVE);
  lcd.setBacklight(HIGH);

  // Motor
  leftMotor.init();
  rightMotor.init();
  motorTimer = timer.setInterval(100, slowDownMotors);
  timer.disable(motorTimer);
  slowMotors = false;

  // Switch
  pinMode(switchPin, INPUT_PULLUP);
  attachInterrupt(digitalPinToInterrupt(switchPin), checkSwitch, CHANGE);

  // Seven Segment
  sSegment.init();
  sSegment.setData(sSegData[2]);
  timer.setInterval(100, updateSevenSegment);

  // Ultrasonic
  timer.setInterval(500, updateUltrasonic);
}

void loop() {
  timer.run();
  checkMotors();
}

void serialEvent() {
  if (Serial.available()) {
    serialData = Serial.read();
    moveMotors();
  }
}


/////////////////////////////
// FUNCTION IMPLEMENTATION //
/////////////////////////////

void checkSwitch() {
  switchState = digitalRead(switchPin);
  if (!switchState) {
    Serial.write(0x03);
    leftMotor.stop();
    rightMotor.stop();
  }
}

void checkMotors() {
  if (slowMotors) {
    timer.enable(motorTimer);
  } else {
    timer.disable(motorTimer);
  }
}

void moveMotors() {
  int fwdSpeed = (switchState) ? 255 : 0;

  switch (serialData) {
    case 'F':
      slowMotors = false;
      leftMotor.setSpeed(fwdSpeed);
      rightMotor.setSpeed(fwdSpeed);
      break;

    case 'B':
      slowMotors = false;
      leftMotor.setSpeed(-255);
      rightMotor.setSpeed(-255);
      break;

    case 'L':
      slowMotors = false;
      leftMotor.setSpeed(-fwdSpeed);
      rightMotor.setSpeed(fwdSpeed);
      break;

    case 'R':
      slowMotors = false;
      leftMotor.setSpeed(fwdSpeed);
      rightMotor.setSpeed(-fwdSpeed);
      break;

    case 'S':
      slowMotors = true;
      break;

    default:
      break;
  }
}

void slowDownMotors() {
  int leftSpeed = leftMotor.getSpeed();
  int rightSpeed = rightMotor.getSpeed();

  if (leftSpeed > 0) {
    leftSpeed -= 16;
    if (leftSpeed < 0)
      leftSpeed = 0;
    leftMotor.setSpeed(leftSpeed * leftMotor.getDirection());
  }

  if (rightSpeed > 0) {
    rightSpeed -= 16;
    if (rightSpeed < 0)
      rightSpeed = 0;
    rightMotor.setSpeed(rightSpeed * rightMotor.getDirection());
  }
}

void updateUltrasonic() {
  unsigned long ping = sonar.ping_median(20);
  lcd.clear();
  lcd.home();
  // sprintf(stringBuffer, "Switch: %d", switchState);    
  sprintf(stringBuffer, "Distance: %dcm", sonar.convert_cm(ping));
  lcd.print(stringBuffer);

  lcd.setCursor(0,1);
  sprintf(stringBuffer, "Motor: %d %d",
    leftMotor.getSpeed() * leftMotor.getDirection(),
    rightMotor.getSpeed() * rightMotor.getDirection());
  lcd.print(stringBuffer);
}

void updateSevenSegment() {
  if (leftMotor.getSpeed() == 0 && rightMotor.getSpeed() == 0) {
    sSegment.setData(sSegData[2]);
  } else {
    switch (serialData) {
      case 'F':
      case 'L':
      case 'R':
        sSegment.setData(sSegData[1]);
        break;

      case 'B':
        sSegment.setData(sSegData[3]);
        break;

      default:
        break;
    }
  }
}