#include <NewPing.h>

const int trigPin = A0;
const int echoPin = A1;

NewPing sonar(trigPin, echoPin, 200);

void setup() {
  Serial.begin(9600);
  pinMode(13, OUTPUT);
}

void loop() {
  delay(50);
  unsigned int ping = sonar.ping_median(20);
  Serial.print("Ping: ");
  Serial.println(sonar.convert_cm(ping));
  digitalWrite(13, sonar.convert_cm(ping) > 50);

  delay(500);
}
