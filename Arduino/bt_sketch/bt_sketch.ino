#include <SPI.h>

#define RXD 0               // Recieve
#define TXD 1               // Transmit
#define START_CMD_CHAR '*'  // The character that signals a command
#define CMD_DIGITALWRITE 10 // The value of a digital write command
#define LOCK_PIN 6          // TODO: Map these correctly
#define UNLOCK_PIN 7        // TODO: Map these correctly
#define PIN_COUNT 14        // The total number of digital pins
#define PIN_LOW 0           // The low value to be recieved over serial
#define PIN_HIGH 1          // The high value to be recieved over serial

char password[4]; //chars arduino receives
char correct_pw[4] = {'A', 'B', 'C', 'D'}; //user defines password
int test_pw = 0; //test if correct pw was used 0 = false, 1 = true

int incomingByte = 0;

/**
 * Runs during initial  setup
 */
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

/**
 * Constantly loops
 */
void loop() {

  Serial.flush();

  // Default values for incoming transmission
  int command = -1;
  int pin_num = -1;
  int pin_val = -1;

  char get_char = ' ';

  // Reruns loop until there is data to read
  if (Serial.available() < 1) {
    return; 
  }

  // Reruns loop if the start command character is wrong
  get_char = Serial.read();
  if (get_char != START_CMD_CHAR) {
    return;
  }

  // Parse the command type, pin number, and value
  command = Serial.parseInt();
  pin_num = Serial.parseInt();
  pin_val = Serial.parseInt();

  // If the command type is digital write
  if (command == CMD_DIGITALWRITE) {
    
    // Converts the incoming pin value to LOW or HIGH
    if (pin_val = PIN_LOW) {
      pin_val = LOW;
    }
    else if (pin_val = PIN_HIGH) {
      pin_val = HIGH;
    }
    else {
      return;
    }

    set_digitalwrite(pin_num, pin_val);
    return;
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

/**
 * A helper function for writing a value to a pin
 */
void set_digitalwrite(int pin_num, int pin_val) {
  switch (pin_num) {
  case 13:
    pinMode(13, OUTPUT);
    digitalWrite(13, pin_val);  
    break;
  case 12:
    pinMode(12, OUTPUT);
    digitalWrite(12, pin_val);   
    break;
  case 11:
    pinMode(11, OUTPUT);
    digitalWrite(11, pin_val);         
    break;
  case 10:
    pinMode(10, OUTPUT);
    digitalWrite(10, pin_val);         
    break;
  case 9:
    pinMode(9, OUTPUT);
    digitalWrite(9, pin_val);         
    break;
  case 8:
    pinMode(8, OUTPUT);
    digitalWrite(8, pin_val);         
    break;
  case 7:
    pinMode(7, OUTPUT);
    digitalWrite(7, pin_val);         
    break;
  case 6:
    pinMode(6, OUTPUT);
    digitalWrite(6, pin_val);         
    break;
  case 5:
    pinMode(5, OUTPUT);
    digitalWrite(5, pin_val); 
    break;
  case 4:
    pinMode(4, OUTPUT);
    digitalWrite(4, pin_val);         
    break;
  case 3:
    pinMode(3, OUTPUT);
    digitalWrite(3, pin_val);         
    break;
  case 2:
    pinMode(2, OUTPUT);
    digitalWrite(2, pin_val); 
    break;      
  default: 
    Serial.println("Failed to write to a pin");
    break;
  }
}
  
