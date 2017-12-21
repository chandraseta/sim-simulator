#pragma once

#include <Arduino.h>

class Motor {
public:
  Motor(int enaPin, int fwdPin, int bwdPin);
  void init();
  void setSpeed(int pwm);
  int getSpeed();
  int getDirection();
  void stop();

private:
  const int enaPin;
  const int fwdPin;
  const int bwdPin;

  int speed;
  int direction;
};
