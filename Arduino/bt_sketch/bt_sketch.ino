#include <SPI.h>

/**
 * Currently serves as our lockup loop to protect from brute forcing
 * It may be beneficial to just turn of the device, so we don't run
 * the battery dry on this loop
 * Definitely not a huge fan of this implementation, but it does provide
 * a level of security
 * Of course, someone could easily use this is as a prank and just keep
 * locking up someones door
 * EDIT: Added some base level security by storing a mac address
 * Now the hacker cannot even attempt to brute force commands until they 
 * have the MAC address of the owner (eventually owners?)
 * EDIT: Standardized incoming commands: *<size>:<command>:<address>
 */

/* Pins */
#define RXD           0         // Recieve pin
#define TXD           1         // Transmit pin
#define LOCK_PIN      6         // Lock pin
#define UNLOCK_PIN    7         // Unlock pin

/* Logic levels */
#define PIN_LOW       0         // The low value to be recieved over serial
#define PIN_HIGH      1         // The high value to be recieved over serial

/* Commands */
#define CMD_LOCK      0xEF93    // The value of a lock command
#define CMD_UNLOCK    0x081D    // The value of an unlock command
#define CMD_SET       0xDEAD    // The value of the set command

/* Statuses */
#define LOCK_SUCCESS            "SUCCESS: Lock"
#define UNLOCK_SUCCESS          "SUCCESS: Unlock"
#define FAILURE_MODE            "FAILURE: Entering failure mode"
#define CMD_FAILURE             "FAILURE: Cannot find command"
#define UID_FAILURE             "FAILURE: Invalid user ID"
#define UID_REQUEST             "REQUEST: User ID"
#define UID_SUCCESS             "SUCCESS: Saved User ID"
#define CONNECT_SUCCESS         "SUCCESS: Smart Lock Technology 0.20 (2016)"
#define NO_UID_FAILURE           "FAILURE: User ID has not been set"

/* Special Constants */
#define START_CMD_CHAR    '*'   // The character that signals a command
#define MAC_BUFFER_SIZE   30    // Holds the size of the mac address buffer
#define EMPTY_MAC         "?"   // Holds the default value for the MAC address

/* Fields */
int failed_attempt_count;
char device_id[30] = EMPTY_MAC;

/**
 * Runs during initial setup
 */
void setup() {

  // Wait for device to establish a connection
  delay(2000);

  // Setup serial baud rate and send a success string
  Serial.begin(9600);
  Serial.println(CONNECT_SUCCESS);
  Serial.flush();

  // Initialize transmit and receive pins
  pinMode(RXD,INPUT);
  pinMode(TXD,OUTPUT);
}

/**
 * Constantly loops
 */
void loop() {

  // Reruns loop until there is data to read
  if (Serial.available() < 1 || failed_attempt_count >= 3) {
    return; 
  }

  // Reruns loop if the start command character is wrong
  if (!is_command()) {
    return;
  }

  // Run the command
  parse_string_and_run_command();
}

/**
 * Reads as follows:
 * Parses the first integer as the command
 * Parses the second integer as the address size
 * Ignores the separator
 * Reads the address
 * Runs the command
 */
void parse_string_and_run_command() {
  // Read in the command and size of string
  int command = Serial.parseInt();
  int in_data_size = Serial.parseInt();

  // Toss the separator
  Serial.read();

  // Read MAC address
  char in_data[in_data_size + 1];
  read_string(in_data_size, in_data);

  // If the MAC address is correct
  if (authenticate(command, in_data)) {
    // Run that command
    run_command(command, in_data);
  }
}

/**
 * Determines if the character to be read next is
 * the command symbol or not
 */
boolean is_command() {
  return Serial.read() == START_CMD_CHAR ? true : false; 
}

/**
 * A generic method for reading strings
 */
void read_string(int in_data_size, char in_data[]) {

  // Allocate some space for the incoming characters
  char in_char;
  int index = 0;

  // Busy loop until the arduino has received all the data
  while (Serial.available() < in_data_size) {}

  // Read the incoming string
  while (index < in_data_size) {
    in_char = Serial.read();   // Read a character
    in_data[index] = in_char;   // Store it
    index++;                  // Increment where to write next
    in_data[index] = '\0';     // Null terminate the string
  }
}

/**
 * Returns true if the incoming address matches
 * the stored address
 */
boolean authenticate(int command, char* in_data) {

  boolean mac_not_setup = strcmp(device_id, EMPTY_MAC);
  boolean cmd_set = command == CMD_SET;
  boolean mac_match = strcmp(in_data, device_id);
  
  if (strcmp(in_data, device_id) == 0) {
    return true;
  }
  else if (strcmp(device_id, EMPTY_MAC) == 0 && command == CMD_SET) {
    return true;
  }
  else {
    Serial.println(UID_FAILURE);
    return false;
  }
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
  Serial.println(LOCK_SUCCESS);
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
  set_digitalwrite(UNLOCK_PIN, LOW);
  Serial.println(UNLOCK_SUCCESS);
}

/**
 * Sends a request message for the user ID
 * Stores the user id in the device id field
 */
void set_address(char* in_data) {
  // If device ID is set, don't listen to this command
  if (strcmp(device_id, EMPTY_MAC) != 0) {
    return;
  }
  // Otherwise, set the address
  else {
    Serial.println(UID_SUCCESS);
    strcpy(device_id,in_data);
  }
}

/**
 * Takes an integer command and attempts to run it
 */
void run_command(int command, char* in_data) {
  switch (command) {
    case CMD_LOCK:
      lock();
      break;
    case CMD_UNLOCK:
      unlock();
      break;
    case CMD_SET:
      set_address(in_data);
      break;
    default:
      failed_attempt_count++;
      Serial.println(failed_attempt_count >= 3 ? FAILURE_MODE : CMD_FAILURE);
      Serial.flush();
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
  
