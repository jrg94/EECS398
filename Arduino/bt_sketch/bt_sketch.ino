#include <SPI.h>

#define RXD 0         // Recieve
#define TXD 1         // Transmit
#define LOCK_PIN 6    // TODO: Map these correctly
#define UNLOCK_PIN 7  // TODO: Map these correctly
#define PIN_COUNT 14  // The total number of digital pins
#define PIN_LOW 0     // The low value to be recieved over serial
#define PIN_HIGH 1    // The high value to be recieved over serial

char password[4]; //chars arduino receives
char correct_pw[4] = {'A', 'B', 'C', 'D'}; //user defines password
int test_pw = 0; //test if correct pw was used 0 = false, 1 = true

int incomingByte = 0;

void setup() {
  Serial.begin(9600);
  Serial.println("Smart Lock Technology 0.10 (2016)");
  Serial.flush(); // Blocks until outgoing transmission is complete
  
  pinMode(LOCK_PIN, OUTPUT);
  pinMode(UNLOCK_PIN, OUTPUT);
  pinMode(RXD,INPUT);
  pinMode(TXD,OUTPUT);
  pinMode(13,OUTPUT);
  digitalWrite(LOCK_PIN, HIGH); // default lock to locked state
}

void loop() {

  while (Serial.available() > 0) {
    // read the incoming byte:
    incomingByte = Serial.read();
    
    // say what you got:
    Serial.print("I received: ");
    Serial.println(incomingByte, DEC);
  }
  /**
  while (Serial.available()) {
    for (int i = 0; i < 4; i++) { //when data is available read it
      password[i] = Serial.read(); //read bytes to password array
    }
    for (int i = 0; i < 4; i++) {
      if (password[i] == correct_pw [i]) { // compare received password to actual password
        test_pw = 1; //passwords match set =1
      }
      else
      {
        test_pw = 0; //passwords do not match set =0
        break;
      }
    }
  }
  if (test_pw == 1) { //unlock the lock for 10s
    Serial.println("The door has been unlocked");
    digitalWrite(lock_pin, LOW);
    delay(10000);
    Serial.println("The door has been locked");
    test_pw = 0;
  }
  else
  {
    digitalWrite(lock_pin, HIGH); //password did not match keep door locked
  }*/
}

void set_digitalwrite(int pin_num, int pin_val) {
  switch (pin_num) {
  case 13:
    pinMode(13, OUTPUT);
    digitalWrite(13, pin_val);  
    // add your code here      
    break;
  case 12:
    pinMode(12, OUTPUT);
    digitalWrite(12, pin_val);   
    // add your code here       
    break;
  case 11:
    pinMode(11, OUTPUT);
    digitalWrite(11, pin_val);         
    // add your code here 
    break;
  case 10:
    pinMode(10, OUTPUT);
    digitalWrite(10, pin_val);         
    // add your code here 
    break;
  case 9:
    pinMode(9, OUTPUT);
    digitalWrite(9, pin_val);         
    // add your code here 
    break;
  case 8:
    pinMode(8, OUTPUT);
    digitalWrite(8, pin_val);         
    // add your code here 
    break;
  case 7:
    pinMode(7, OUTPUT);
    digitalWrite(7, pin_val);         
    // add your code here 
    break;
  case 6:
    pinMode(6, OUTPUT);
    digitalWrite(6, pin_val);         
    // add your code here 
    break;
  case 5:
    pinMode(5, OUTPUT);
    digitalWrite(5, pin_val); 
    // add your code here       
    break;
  case 4:
    pinMode(4, OUTPUT);
    digitalWrite(4, pin_val);         
    // add your code here 
    break;
  case 3:
    pinMode(3, OUTPUT);
    digitalWrite(3, pin_val);         
    // add your code here 
    break;
  case 2:
    pinMode(2, OUTPUT);
    digitalWrite(2, pin_val); 
    // add your code here       
    break;      
  default: 
    // if nothing else matches, do the default
    // default is optional
    break;
  }
}
  
