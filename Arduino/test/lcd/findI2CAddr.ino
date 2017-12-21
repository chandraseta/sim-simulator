#include <Wire.h>

int findI2CAddr() {
    Serial.begin(9600);

  // Wait for serial port to connect
  while (!Serial);

  Serial.println("I2C scanner. Scanning...");
  byte count = 0;
 
  Wire.begin();
  Serial.println("WIre begin");
  for (byte i = 1; i < 120; i++) {
    Wire.beginTransmission(i);
    if (Wire.endTransmission() == 0) {
      Serial.print("Found address ");
      Serial.print(i, DEC);
      Serial.print("(0x");
      Serial.print(i, HEX);
      Serial.println(")");
      count++;
      delay (1);  // maybe unneeded?
    } // end of good response
    else {
      Serial.println(i);
    }
  } // end of for loop
  Serial.println("Done");
  Serial.print("Found ");
  Serial.print(count, DEC);
  Serial.println(" device(s).");
}

