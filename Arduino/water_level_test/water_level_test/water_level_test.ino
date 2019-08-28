void setup() {
  Serial.begin(9600);
  pinMode(13, OUTPUT);
}

void loop() {
  int level = analogRead(A0);
  Serial.println(level);

  if(level >= 100)
    digitalWrite(13,HIGH);
  else
    digitalWrite(13,LOW);

    delay(500);
}
