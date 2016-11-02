# MusicRec
Acticity Based Music Recommender and Player

Author: Rajat Shah (rshah6)

## Overview
The choice of music most of the times depends on your current activity. If a music player can detect a user's activity and recommend/play song based on it, then user engagement is reduced. MusicRec is a context-aware Android application which determines if a user is driving, working-out, relaxing, etc and modifies the user's playlist accordingly.

## Installation Steps & Requirements
The project was developed in latest version of Android Studio on Mac.

Minimum requirements:
- Adroid API level 23
- JDK version: jdk1.8.0_91.jdk
- Gradle version: 2.14.1

Dependencies and External Libraries:
- Google Play Services: 9.0.2
- Android Kitat: 4.4

In order to run the application locally, download the source code and import it in Android Studio. Then Compile and run it either in an emulator or export it as an APK to Android device.

## Implemented Approach and TODO
- Activity Recognition:
 - Driving: The background service of the application continuously keeps track of the user location. If the **change in location coordinates** in past 10 seconds is substantial, it's a clear indication of user driving.
 - Jogging: Similar to driving, except the **change in location coordinates** is less.
 - Working out: Assuming the user keeps the phone in pocket while working out, a large (oscillating?) variance in accelerometer reading in past 10 seconds indicates movement of the user.
 - Studying/Relaxing: If the phone is stable for a while (no change in location and accelerometer reading) and possibly located at Library/Home, then user can be safely assumed to be engaged in either studying or relaxing.
    
- Music Player:

## Sample Screenshots
<p align="center">
  <img src="https://media.giphy.com/media/26ufcVxET21XK5HUI/giphy.gif" style="margin-right:30px" />
  <img src="https://media.giphy.com/media/26ufdf0kAOt3Gniko/giphy.gif" />
</p>
