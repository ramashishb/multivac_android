/*
 * created by Rui Santos, http://randomnerdtutorials.wordpress.com
 * Control DC motor with Smartphone via bluetooth
 * 2013
 */
// include the library code:
#include <LiquidCrystal.h>
#include <SoftwareSerial.h>


// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(12, 11, 10, 5, 4, 3, 2);

SoftwareSerial mySerial(8, 9); // RX, TX

void setup() {
  lcd.begin(16, 2);
  lcd.print("Multivac On!");
  mySerial.begin(9600);
}

void loop() {
  //if some date is sent, reads it and saves in state
  if (mySerial.available() > 0) {
    int command = mySerial.read();
    if (command != 0) {
      lcd.clear();
  lcd.print("Multivac On!");
  
      lcd.setCursor(0, 1 );
      lcd.print("Command:");
      lcd.print(command);

      if (command == 1 || command == 'P' || command == 'p') {
        mySerial.print("car\n");
      } else {
        mySerial.print("NaN\n");
      }
    }
  }
}
