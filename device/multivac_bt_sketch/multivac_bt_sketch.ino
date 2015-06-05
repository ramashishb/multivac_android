/*
 * created by Rui Santos, http://randomnerdtutorials.wordpress.com
 * Control DC motor with Smartphone via bluetooth
 * 2013
 */
// include the library code:
#include <LiquidCrystal.h>
#include <SoftwareSerial.h>

#define DEVICE_PROFILE "car"

#define ECHOPIN 7        // Pin to receive echo pulse 
#define TRIGPIN 6        // Pin to send trigger pulse

#define EVENT_MOVE_TOWARDS "towards"
#define EVENT_MOVE_AWAY "away"
#define EVENT_STATIONARY "stationary"

#define STATE_MOTION_NONE "none"
#define STATE_MOTION_AWAY "moving_away"
#define STATE_AWAY "away"
#define STATE_MOTION_TOWARDS "moving_towards"
#define STATE_NEAR "near"

#define STATIONARY_THRESHOLD 10

// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(12, 11, 10, 5, 4, 3, 2);
SoftwareSerial mySerial(8, 9); // RX, TX

// states
int command = 0;

//float distanceStateStart = 0.0;
float distance = 0.0;

String motionState = STATE_MOTION_NONE;
int stationaryCount = 0;

float referenceDistance = 0;
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

  float distanceL = pulseIn(ECHOPIN, HIGH);
  distanceL = distanceL / 58;

  return distanceL;
}

void loop() {
  if (referenceDistance == 0) {
      distance = referenceDistance = getDistance();
  }
  
  float currDistance = getDistance();

  float deltaMoved = (distance - currDistance);
  float absDistance = abs(deltaMoved);

  String newState = motionState;
  if (absDistance > 10.0 && currDistance < referenceDistance) {
    // so the person moved
    distance = currDistance;

    if ( motionState != STATE_NEAR && deltaMoved > 0 ) {
      newState = STATE_MOTION_TOWARDS;
    } else if (motionState != STATE_AWAY && deltaMoved < 0) {
      newState = STATE_MOTION_AWAY;
    }
  } else if (absDistance < 5.0) {
    if (++stationaryCount > STATIONARY_THRESHOLD) {
      newState = STATE_MOTION_NONE;
      stationaryCount = 0;
    }
  }

  ////////////////////
  //Send a response if we have received an explicit command to send message or change
  bool shouldSendEvent = false;
  String eventToSend = "";

  // check if there was an explicit command to send
  if (mySerial.available() > 0) {
    command = mySerial.read();
    if (command != 0) {
      shouldSendEvent = (command == 1 || command == 'P' || command == 'p');
    }
  }

  if (newState == motionState) {
    // check if moved too far or too close,
    if (motionState == STATE_MOTION_TOWARDS && currDistance <= 100.0) {
      motionState = STATE_NEAR;
      shouldSendEvent = true;
    } else if (motionState == STATE_MOTION_AWAY && currDistance > 100.0)   {
      motionState = STATE_AWAY;
      shouldSendEvent = true;
    }
  } else {
    // new direction or stop
    motionState = newState;
    shouldSendEvent = true;
  }


  if (shouldSendEvent) {
    if (motionState == STATE_AWAY) {
      eventToSend = EVENT_MOVE_AWAY;
    } else if (motionState == STATE_NEAR) {
      eventToSend = EVENT_MOVE_TOWARDS;
    }      else {
      eventToSend = EVENT_STATIONARY;
    }
    String response = DEVICE_PROFILE;
    response += ",";
    response += eventToSend;
    response += "\n";
    mySerial.print(response);
  }



  //if some date is sent, reads it and saves in state
  //  if (mySerial.available() > 0) {
  //    command = mySerial.read();
  //    if (command != 0) {
  //      String response;
  //      if (command == 1 || command == 'P' || command == 'p') {
  //        response = "car";
  //      } else {
  //        response = "NaN";
  //      }
  //
  //      response += ",";
  //      response += direction;
  //      response += "\n";
  //      mySerial.print(response);
  //    }
  //  } else {
  //    if (isDebug) {
  //      mySerial.println(direction);
  //    }
  //  }


  String line = "Cmd:";
  line += command;
  line += " d:";
  line += int(distance);

  lcd.setCursor(0, 1 );
  int written = lcd.print(line);
  while (written <= 16) {
    written += lcd.print(" ");
  }

  delay(100);
}
