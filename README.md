# EECS398
This directory contains an Android Studio project for an application that will be used to operate bluetooth locks. The lock is an Adafruit solenoid lock which is controlled by an Arduino. The Arduino is attached to an Adafruit Bluefruit circuit board which routes all of the bluetooth communication through serial.  

# Authors 
* Jeremy Griffith
* Christen Sacucci
* Miriam Crichlow

# Updates
#### 4/19/16
* Spent a considerable amount of time making code more readable
 * Standardized comment structure, fields, and methods
  * Added a comment to every method (excluded some code that was borrowed such as DeviceListActivity)
  * Removed extraneous imports and cut down on unused variables
 * Used primarily Javadoc comments
* Currently hunting and fixing bugs
 * Removed a bug that caused the UI to display the lock as unlocked when the lock was disconnected from the app
 * Looking to remove a freezing bug that occurs when the app fails to connect to the lock
 * Started learning Android Studio testing
  * Began basic unit testing of classes that are separate from UI
* Added proof-of-concept GPS feature
 * Currently sends streams of distances to the screen as the user gets close to the lock

#### 4/18/16
* Cleaned up code considerably - added more comments and removed deprecated fields and methods
* Began building a testing framework - better late than never
* Eliminated a bug where the lock switch would change to the unlocked state even when disconnected

#### 4/9/16
* Researched security measures as well as got creative
  * Encryption (AES)
    * Possibly too slow
  * Passwords
  * Brute force prevention
    * Lock device
* Implemented
  * Brute force protection
    * At 3 failed attempts to guess the command, the device locks up
      * This could be a major issue in an apartment. Someone could easily just load 3 random commands into the lock to keep someone from getting into their home.
  * Passwords
    * During setup, the app will send a setup command along with the MAC address as the password. Once this link is made, no other device can establish a connection until the arduino is reset.
* Implemented rounded edges on locks and popups
* Implemented outlines on locks and popups
* Implemented unlink button for deleting locks (will need to implement this fully on the arduino side as well)
* Removed save button in popup - saving is handled through hitting return
* Moved and resized lock tiles so they fill about have the screen and begin at the top of the screen

#### 3/25/16
* Wrote a bit of Arduino code that signals two pins (independently) to drive our lock
* This code allows us to send a special command for lock and unlock which are packaged to avoid pushing power on both inputs of the circuit
* Wrote some Android code to match the communication settings on the Arduino (*10 to lock, *11 to unlock)

#### 3/20/16
* Cleaned up UI - moved click feature from tile to gear symbol (search gear on Google images)
* Added a isCorrupt function to help with saving/loading issues

#### 3/3/16
* Converted ListLayout to GridLayout
* Tiles are clickable - causes random GPS coordinates to be generated

#### 2/26/16
* Managed to do some actual device testing (Thanks Ted)
* Incorporated a boolean for emulator testing (w/o bluetooth support)
* Lock screen now lets the user move to the lock list page - both are hardcoded to some extent

#### 2/18/16
* Working on getting the basics of bluetooth up and running
* Will be learning how UI works soon to incorporate Bluetooth
* Removed the tutorial project
* Added some open source bluetooth code which will be edited to fit our project
* Included models of the locks in code - not sure how these will work yet
