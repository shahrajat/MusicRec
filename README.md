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
- Activity Recognition
 - __Driving__: The background service of the application continuously keeps track of the user location. If the **change in location coordinates** in past 10 seconds is substantial, it's a clear indication of user driving.
 - __Jogging__: Similar to driving, except the **change in location coordinates** is less.
 - __Working out__: Assuming the user keeps the phone in pocket while working out, a large (oscillating?) **variance in accelerometer reading** in past 10 seconds indicates movement of the user.
 - __Studying/Relaxing__: If the **phone is stable** for a while (no change in location and accelerometer reading) and possibly located at Library/Home, then user can be safely assumed to be engaged in either studying or relaxing.
    
- Music Player
 - __Song Database__: A local XML file containing a list of song metadata is read and the UI is dynamically generated using it. In a more practical setting (or cloud based service), the XML can be read from the a remote server.
 - __Song selection__: During the initial use of the Music Player by the user, the songs might be generated randomly. However, as the user starts interacting with Music Player, the player **learns** and starts mapping genre of the manual selection to activity. For a good learner, various aforementioned features can be user, however in this implementation a simplified approach based on frequence of genre is used.

## Sample Screenshots
<p align="center">
  <img src="https://media.giphy.com/media/26ufcVxET21XK5HUI/giphy.gif" style="margin-right:30px" />
  <img src="https://media.giphy.com/media/26ufdf0kAOt3Gniko/giphy.gif" />
</p>
