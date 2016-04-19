# EECS398
This directory contains an Android Studio project for an application that will be used to operate bluetooth locks. The lock is an Adafruit solenoid lock which is controlled by an Arduino. The Arduino is attached to an Adafruit Bluefruit circuit board which routes all of the bluetooth communication through serial.  

# Authors 
* Jeremy Griffith
* Christen Sacucci
* Miriam Crichlow

# Updates
4/18/16
* Cleaned up code considerably - added more comments and removed deprecated fields and methods
* Began building a testing framework - better late than never
* Eliminated a bug where the lock switch would change to the unlocked state even when disconnected

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
