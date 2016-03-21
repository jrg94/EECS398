
#include <SPI.h>

int lock_pin = 6; //pinout on arduino
char password[4]; //chars arduino receives
char correct_pw[4] = {'A', 'B','C','D'}; //user defines password
int test_pw =0; //test if correct pw was used 0 = false, 1 = true


void setup() {
  pinMode(lock_pin,OUTPUT);
  Serial.begin(9600);
  digitalWrite(lock_pin,HIGH); // default lock to locked state

}

void loop() {
  while(Serial.available()){
    for(int i=0;i<4;i++) { //when data is available read it
      password[i] = Serial.read(); //read bytes to password array
    }
    for(int i=0;i<4;i++){
      if(password[i] == correct_pw [i]) {// compare received password to actual password
        test_pw = 1; //passwords match set =1
      }
      else 
      {
         test_pw = 0; //passwords do not match set =0
         break;
      }
      }
    }
    if(test_pw == 1) { //unlock the lock for 10s
      Serial.println("The door has been unlocked");
      digitalWrite(lock_pin,LOW);
      delay(10000);
      Serial.println("The door has been locked");
      test_pw = 0;
    }
    else
    {
      digitalWrite(lock_pin,HIGH); //password did not match keep door locked
    }
  }
  
