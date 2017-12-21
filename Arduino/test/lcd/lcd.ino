#include <Wire.h>
#include <LCD.h>
#include <LiquidCrystal_I2C.h>

const int lcdAddr = 0x3f;
const int rows = 2;
const int cols = 16;

const String msg[] = {"Hello", "World"};

LiquidCrystal_I2C lcd(lcdAddr, 2, 1, 0, 4, 5, 6, 7); // Magic Number

void setup() {
//  findI2CAddr();
  
	lcd.begin(cols, rows);
	lcd.setBacklightPin(3, POSITIVE);
}

void loop() {
  lcd.setBacklight(HIGH);
	lcd.home();
	lcd.print(msg[0]);
	delay(1000);
	
	lcd.setCursor(5, 1);
	lcd.print(msg[1]);
	delay(1000);
	
	lcd.clear();
	delay(1000);
	
	lcd.setBacklight(LOW);
	delay(1000);
  
}
