/*
 * created by Rui Santos, http://randomnerdtutorials.wordpress.com
 * Control DC motor with Smartphone via bluetooth
 * 2013
 */
// include the library code:
#include <LiquidCrystal.h>
#include <SoftwareSerial.h>


#define ECHOPIN 7        // Pin to receive echo pulse 
#define TRIGPIN 6        // Pin to send trigger pulse

#define TOWARDS "towards"
#define AWAY "away"
#define STATIONARY "stationary"

#define STATIONARY_THRESHOLD 10

boolean isDebug = false;

// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(12, 11, 10, 5, 4, 3, 2);

SoftwareSerial mySerial(8, 9); // RX, TX

int command = 0;
float distance = 0.0;
String direction = STATIONARY;
int stationaryCount = 0;

void setup() {
  lcd.begin(16, 2);
  lcd.print("Multivac On!");
  mySerial.begin(9600);

  pinMode(ECHOPIN, INPUT);
  pinMode(TRIGPIN, OUTPUT);

  lcd.clear();
  lcd.print("Multivac On!");
}

int getDistance() {
  // Start Ranging -Generating a trigger of 10us burst
  digitalWrite(TRIGPIN, LOW);
  delayMicroseconds(2);
  digitalWrite(TRIGPIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIGPIN, LOW);
  // Distance Calculation

  float distance = pulseIn(ECHOPIN, HIGH);
  distance = distance / 58;

  return distance;
}

void loop() {
  lcd.setCursor(0, 1 );
  float currDistance = getDistance();
  float diffDistance = (distance - currDistance);
//  String direction = STATIONARY;

  float changePercent = abs(diffDistance) * 100.0 / distance;
  if (changePercent > 5.0) {
    distance = currDistance;
    if ( diffDistance > 0 )
      direction = TOWARDS;
    else
      direction = AWAY;
  } else {
    if (++stationaryCount > STATIONARY_THRESHOLD) {
      direction = STATIONARY;
      stationaryCount = 0;
    }
  }

  //if some date is sent, reads it and saves in state
  if (mySerial.available() > 0) {
    command = mySerial.read();
    if (command != 0) {
      String response;
      if (command == 1 || command == 'P' || command == 'p') {
        response = "car";
      } else {
        response = "NaN";
      }

      response += ",";
      response += direction;
      response += "\n";
      mySerial.print(response);
    }
  } else {
    if (isDebug) {
      mySerial.println(direction);
    }
  }


  String line = "Cmd:";
  line += command;
  line += " d:";
  line += int(distance);

  int written = lcd.print(line);
  while (written <= 16) {
    written += lcd.print(" ");
  }

  delay(100);
}
