# EECS398
This directory contains an Android Studio project for an application that will be used to operate bluetooth locks. The lock is an Adafruit solenoid lock which is controlled by an Arduino. The Arduino is attached to an Adafruit Bluefruit circuit board which routes all of the bluetooth communication through serial.  

# Authors 
* Jeremy Griffith
* Christen Sacucci
* Miriam Crichlow

# Updates
4/19/16
* Added a comment to every method (excluded some code that was borrowed such as DeviceListActivity)
* Removed extraneous imports and cut down on unused variables
* Standardized style for all code

4/18/16
* Cleaned up code considerably - added more comments and removed deprecated fields and methods
* Began building a testing framework - better late than never
* Eliminated a bug where the lock switch would change to the unlocked state even when disconnected

4/9/16
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

3/20/16
* Cleaned up UI - moved click feature from tile to gear symbol (search gear on Google images)
* Added a isCorrupt function to help with saving/loading issues

3/3/16
* Converted ListLayout to GridLayout
* Tiles are clickable - causes random GPS coordinates to be generated

2/26/16
* Managed to do some actual device testing (Thanks Ted)
* Incorporated a boolean for emulator testing (w/o bluetooth support)
* Lock screen now lets the user move to the lock list page - both are hardcoded to some extent

2/18/16
* Working on getting the basics of bluetooth up and running
* Will be learning how UI works soon to incorporate Bluetooth
* Removed the tutorial project
* Added some open source bluetooth code which will be edited to fit our project
* Included models of the locks in code - not sure how these will work yet
