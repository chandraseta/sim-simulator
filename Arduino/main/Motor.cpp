#include "motor.h"

Motor::Motor(int enaPin, int fwdPin, int bwdPin): enaPin(enaPin), fwdPin(fwdPin), bwdPin(bwdPin) {}

void Motor::init() {
  pinMode(enaPin, OUTPUT);
  pinMode(fwdPin, OUTPUT);
  pinMode(bwdPin, OUTPUT);

  speed = 0;
  direction = 0;
}

void Motor::setSpeed(int pwm) {
  direction = (pwm > 0) + (pwm >= 0) - 1;
  speed = pwm * ((direction == -1) ? -1 : 1);

  if (speed >= 255) {
    digitalWrite(enaPin, HIGH);
  } else if (speed <= 0) {
    digitalWrite(enaPin, LOW);
  } else {
    analogWrite(enaPin, speed);
  }

  switch (direction) {
    case 1:
      digitalWrite(fwdPin, HIGH);
      digitalWrite(bwdPin, LOW);
      break;

    case -1:
      digitalWrite(fwdPin, LOW);
      digitalWrite(bwdPin, HIGH);
      break;

    default:
      digitalWrite(fwdPin, LOW);
      digitalWrite(bwdPin, LOW);
      break;
  }
}

int Motor::getSpeed() {
  return speed;
}

int Motor::getDirection() {
  return direction;
}

void Motor::stop() {
  speed = 0;
  direction = 0;

  digitalWrite(enaPin, LOW);
  digitalWrite(fwdPin, LOW);
  digitalWrite(bwdPin, LOW);
}
