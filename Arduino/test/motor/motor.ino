// PWM Pin: 3, 5, 6, 9, 10, 11

const int leftFwd = 2;
const int leftEna = 10;
const int leftBwd = 3;
const int rightFwd = 4;
const int rightEna = 11;
const int rightBwd = 5;

void setup() {
  pinMode(leftFwd, OUTPUT);
  pinMode(leftEna, OUTPUT);
  pinMode(leftBwd, OUTPUT);
  pinMode(rightFwd, OUTPUT);
  pinMode(rightEna, OUTPUT);
  pinMode(rightBwd, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  digitalWrite(leftFwd, HIGH);
  digitalWrite(leftBwd, LOW);
  digitalWrite(rightFwd, HIGH);
  digitalWrite(rightBwd, LOW);

  Serial.println("Begin");
//  digitalWrite(leftEna, HIGH);
//  digitalWrite(rightEna, HIGH);
  delay(2000);
  for (int i = 255; i >= 0; i -= 16) {
    digitalWrite(leftEna, HIGH);
    digitalWrite(rightEna, HIGH);
    delay(100);
    analogWrite(leftEna, i);
    analogWrite(rightEna, i);
    Serial.println(i);
    delay(900);
  }
}
