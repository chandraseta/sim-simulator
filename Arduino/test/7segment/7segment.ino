const int latchPin = 12;//Pin connected to ST_CP of 74HC595
const int clockPin = 8;//Pin connected to SH_CP of 74HC595 
const int dataPin = 9; //Pin connected to DS of 74HC595 

// d, P, r
int data[3] = {0b10111010, 0b11100110, 0b00100010};


void setup() {
	pinMode(latchPin, OUTPUT);
	pinMode(clockPin, OUTPUT);
	pinMode(dataPin, OUTPUT);
  Serial.begin(9600);
}

void loop() {
	for (int i = 0; i < 3; ++i) {
		digitalWrite(latchPin, LOW);
		shiftOut(dataPin, clockPin, MSBFIRST, ~data[i]);
		digitalWrite(latchPin, HIGH);
    Serial.println(data[i]);
		delay(1000);
	}
}
