#include <Wire.h>
#include <LCD.h>
#include <LiquidCrystal_I2C.h>
#include <NewPing.h>

// Timing, State management
unsigned long currMillis = 0;
unsigned long sSegPrevMillis = 0;
char sSegState = 0;
unsigned long lcdPrevMillis = 0;
char lcdState = 0;
unsigned long usPrevMillis = 0;

// 7-segment
const int latchPin = 12;
const int clockPin = 8;
const int dataPin = 9;
int data[3] = {0b10111010, 0b11100110, 0b00100010};

// Limit switch
const int buttonPin = 7;
const int ledPin = 13;

// LCD
const int lcdAddr = 0x3f;
const int rows = 2;
const int cols = 16;
LiquidCrystal_I2C lcd(lcdAddr, 2, 1, 0, 4, 5, 6, 7); // Magic Number

// Motor
const int leftFwd = 2;
const int leftEna = 10;
const int leftBwd = 3;
const int rightFwd = 4;
const int rightEna = 11;
const int rightBwd = 5;

// Ultrasonic
const int trigPin = A0;
const int echoPin = A1;
NewPing sonar(trigPin, echoPin, 200);

void runSevenSegment();
void runrunLimitSwitch();
void runLcd();
void runrunUltrasonic();

void setup() {
    // Serial
    Serial.begin(9600);
    
    // 7-segment
    pinMode(latchPin, OUTPUT);
	pinMode(clockPin, OUTPUT);
	pinMode(dataPin, OUTPUT);

    // Limit switch
    pinMode(buttonPin, INPUT);
    pinMode(ledPin, OUTPUT);

    // LCD
    lcd.begin(cols, rows);
    lcd.setBacklightPin(3, POSITIVE);

    // Motor
    pinMode(leftFwd, OUTPUT);
    pinMode(leftEna, OUTPUT);
    pinMode(leftBwd, OUTPUT);
    pinMode(rightFwd, OUTPUT);
    pinMode(rightEna, OUTPUT);
    pinMode(rightBwd, OUTPUT);
    
    // Ultrasonic
    // None
}

void loop() {
    // Timing
    currMillis = millis();

    runSevenSegment();
    runLimitSwitch();
    runLcd();
    runMotor();
    runUltrasonic();

}

void runSevenSegment() {
    if (currMillis - sSegPrevMillis > 1000) {
        sSegPrevMillis = currMillis;

        digitalWrite(latchPin, LOW);
        shiftOut(dataPin, clockPin, MSBFIRST, ~data[sSegState]);
        digitalWrite(latchPin, HIGH);
        sSegState = (sSegState + 1) % 3;
    }
}

void runLcd() {
    if (currMillis - lcdPrevMillis > 1000) {
        lcdPrevMillis = currMillis;

        switch (lcdState) {
            case 0:
                lcd.setBacklight(HIGH);
                lcd.home();
                lcd.print("Hello");
                break;

            case 1:
                lcd.setCursor(5, 1);
                lcd.print("World");
                break;

            case 2:
                lcd.clear();
                break;

            default:
                lcd.setBacklight(LOW);
        }

        lcdState = (lcdState + 1) % 4;
    }
}

void runLimitSwitch() {
    digitalWrite(ledPin, !digitalRead(buttonPin));
}

void runMotor() {
    digitalWrite(13, !digitalRead(13));
    digitalWrite(leftFwd, HIGH);
    digitalWrite(leftBwd, LOW);
    digitalWrite(rightFwd, HIGH);
    digitalWrite(rightBwd, LOW);

    digitalWrite(leftEna, HIGH);
    digitalWrite(rightEna, HIGH);

    // unsigned long t = currMillis / 25;
    // int pwm = 255 - (t % 256);
    // if (pwm < 128)
    //     pwm = 255 - pwm;
    // analogWrite(leftEna, pwm);
    // analogWrite(rightEna, pwm);
}

void runUltrasonic() {
    if (currMillis - usPrevMillis > 500) {
        usPrevMillis = currMillis;

        unsigned long ping = sonar.ping_median(20);
        Serial.print("Ping: ");
        Serial.println(sonar.convert_cm(ping));
    }
}

