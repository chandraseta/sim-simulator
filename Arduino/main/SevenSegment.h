#pragma once

#include <Arduino.h>

class SevenSegment {
public:
  SevenSegment(int latchPin, int clockPin, int dataPin);
  void init();
  void setData(char data);
  char getData();

private:
  const int latchPin;
  const int clockPin;
  const int dataPin;

  char data;
};