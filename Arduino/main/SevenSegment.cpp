#include "SevenSegment.h"

SevenSegment::SevenSegment(int latchPin, int clockPin, int dataPin): latchPin(latchPin), clockPin(clockPin), dataPin(dataPin) {}

void SevenSegment::init() {
  pinMode(latchPin, OUTPUT);
  pinMode(clockPin, OUTPUT);
  pinMode(dataPin, OUTPUT);

  this->setData(0x00);
}

void SevenSegment::setData(char data) {
  this->data = data;

  digitalWrite(latchPin, LOW);
  shiftOut(dataPin, clockPin, MSBFIRST, ~data);
  digitalWrite(latchPin, HIGH);
}

char SevenSegment::getData() {
  return data;
}