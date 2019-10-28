#include <SoftwareSerial.h>
#define enA 9
#define in1 6
#define in2 7


SoftwareSerial EEBlue(10, 11); // RX | TX
String string = "";
String readin = "";

char incomingByte;
bool isTurning = false;
int val = 0;


void setup()
{
  Serial.begin(9600);
  EEBlue.begin(9600);
  Serial.println("The bluetooth gates are open.\n Connect to HC-05 from any other bluetooth device with 1234 as pairing key!.");

  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  // Set initial rotation direction
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
}

void loop()
{
  int potValue = analogRead(A1); // Read potentiometer value
  int pwmOutput = map(potValue, 0, 1023, 0 , 255);
  analogWrite(enA, pwmOutput);

  if (EEBlue.available())
  {
    while (EEBlue.available() > 0)
    {
      incomingByte = EEBlue.read();
      readin = String(incomingByte);
    }
  }

  if (string != "")
  {
    Serial.println("----------------------------");
  }

  if (readin == "a")
  {
    string = "a";
    readin = "";
  }
  if (readin == "b")
  {
    string = "b";
    readin = "";
  }
  if (readin == "c")
  {
    string = "c";
    readin = "";
  }

  if (string.equals("c"))
  {
    digitalWrite(in1, LOW);
    digitalWrite(in2, LOW);
    Serial.println(string);
    string = "";
  }
  else if (string.equals("a"))
  {
    Serial.println(string);
    myDelay2(1);
    string = "";
  }
  else if (string.equals("b"))
  {
    Serial.println(string);
    myDelay(1);
    string = "";
  }

  if (string.equals(""))
  {
    delay(600);
    digitalWrite(in1, LOW);
    digitalWrite(in2, LOW);
  }
}

void myDelay(int del) { //bass higher, pitch strings lower
  unsigned long myPrevMillis = millis();
  while (millis() - myPrevMillis <= del)
  {
    digitalWrite(in1, HIGH);
  }
}

void myDelay2(int del) { //bass lower, pitch strings higher
  unsigned long myPrevMillis = millis();
  while (millis() - myPrevMillis <= del)
  {
    digitalWrite(in2, HIGH);
  }
}
