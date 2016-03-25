#include <SPI.h>

#define RXD 0               // Recieve
#define TXD 1               // Transmit
#define START_CMD_CHAR '*'  // The character that signals a command
#define CMD_LOCK 10         // The value of a lock command
#define CMD_UNLOCK 11       // The value of an unlock command
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
  
  pinMode(RXD,INPUT);
  pinMode(TXD,OUTPUT);
}

/**
 * Constantly loops
 */
void loop() {

  Serial.flush();

  // Default values for incoming transmission
  int command = -1;

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

  // Takes the command and attempts to run it
  run_command(command);
}

/**
 * MUST AVOID SETTING UNLOCK_PIN AND LOCK_PIN TO 1
 * 
 * The first line is redundant since we make sure that
 * both pins are set to LOW before we exit
 */
void lock() {
  set_digitalwrite(UNLOCK_PIN, LOW);
  set_digitalwrite(LOCK_PIN, HIGH);
  delay(1000);
  set_digitalwrite(LOCK_PIN, LOW);
  Serial.println("Lock was successful");
}

/**
 * MUST AVOID SETTING UNLOCK_PIN AND LOCK_PIN TO 1
 * 
 * The first line is redundant since we make sure that
 * both pins are set to LOW before we exit
 */
void unlock() {
  set_digitalwrite(LOCK_PIN, LOW);
  set_digitalwrite(UNLOCK_PIN, HIGH);
  delay(1000);
  set_digitalwrite(LOCK_PIN, LOW);
  Serial.println("Unlock was successful");
}

/**
 * Takes an integer command and attempts to run it
 */
void run_command(int command) {
  switch (command) {
    case CMD_LOCK:
      lock();
      break;
    case CMD_UNLOCK:
      unlock();
      break;
    default:
      Serial.println("Failed to find this command");
      break;
  }
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
  
