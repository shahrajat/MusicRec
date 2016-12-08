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

In order to run the application locally, download the source code and import it in Android Studio. Then first Compile and run the middleware as shown in the [screenshot](http://shahrajat.com/img/musicrec-android-studio.png) either in an emulator or export it as an APK to Android device. Then run the app in a similar fashion.


### Screenshots
- Following screenshot shows android studio setup after cloning this repository: http://shahrajat.com/img/musicrec-android-studio.png


## Implemented Approach
- Activity Recognition
 - __Driving__: The background service of the application continuously keeps track of the user location. If the **change in location coordinates** in past 10 seconds is substantial, it's a clear indication of user driving.
 - __Jogging__: Similar to driving, except the **change in location coordinates** is less.
 - __Working out__: Assuming the user keeps the phone in pocket while working out, a large (oscillating?) **variance in accelerometer reading** in past 10 seconds indicates movement of the user.
 - __Studying/Relaxing__: If the **phone is stable** for a while (no change in location and accelerometer reading) and possibly located at Library/Home, then user can be safely assumed to be engaged in either studying or relaxing.
    Update 11/13/2016: For higher accuracy of activity detection, Android Activity Recognition API is used.
- Music Player
 - __Song Database__: A [local XML file](https://raw.githubusercontent.com/shahrajat/MusicRec/master/app/src/main/res/xml/songs.xml) containing a list of basic song metadata is read and the UI is dynamically generated using it. In a more practical setting (or cloud based service), the XML can be read from the a remote server.
 - __Song selection__: During the initial use of the Music Player by the user, the songs might be generated randomly. However, as the user starts interacting with Music Player, the player **learns** and starts mapping genre of the manual selection to activity. For a good learner, various aforementioned features can be user, however in this implementation a simplified approach based on frequence of genre is used. To allow for runtime adaptation of the application, developer makes no assumption of choice of music for a user during an activity. This means the **user history** containing Activity --> Frequent Genres is maintained.
 - __Playing song__: To focus on the key aspects of context-aware and runtime adaptation, this is feature is implemented as mock action. User can still select a song from the playlist and the recommender engine plays the next song based on user.

 Update 11/13/2016: For higher accuracy of activity detection, Android Activity Recognition API is used.

### Features implemented in Foreground App
   1. Parse [raw XML](https://raw.githubusercontent.com/shahrajat/MusicRec/master/app/src/main/res/xml/songs.xml) having songs metadata and dynamically populate the UI element.
   2. Use Android Location API to detect current location of the user.
   3. Find accelerometer reading using phone sensors.
   4. Update exisiting user genre preference whenever user manually selects a song.
   5. Store user history in a persistent storage.
   6. Identify user activity based on above factors.
   7. Dynamically update playlist based on best guess of current activity.
   8. Separate middleware from the foreground app.

### Features implemented in Background App
   1. Detect current user activity in service.
   2. API method for interaction with foreground app.
   3. API method returns string for current user activity as ecpected by it.

## Application Flow and internal details
   1. When the application is launched, default order of the songs is displayed.
   2. The top text displays the current detected activity and internally saved user preference of genre.
   3. As soon as the current activity is detected (or changed), the play list is reordered such that - top songs are genre correspoding to that activity.
   4. Whenever user manually selects a song from the list, genre preference is updated instantly. This also results in reordering of the playlist.
   5. The Snackbar at the bottom displays various event messages based on user interation with the application.

## Sample Screenshots
<p align="center">
<kbd>
<img src="https://media.giphy.com/media/3o7TKLfkvWpX3PPWQ8/giphy.gif" border="3"/>
 </kbd>
<kbd>
  <img src="https://media.giphy.com/media/3o6Ztr0zyb96WVvVMA/giphy.gif" border="3"/>
 </kbd>
</p>
