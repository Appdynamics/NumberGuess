# Android NumberGuess

A simple Android game where the user guesses a number between 1 and 20. Use this as an exercise to learn how to instrument an Android application with AppDynamics Mobile Real User Monitoring (MRUM).

Play the game by opening a terminal window and launching the Python webserver (`python WebServer.py`) which serves up random numbers as plain text via HTTP. Clean, build, and launch the Android app in an emulator. The default IP address in the `WebServer.py` file and Android app (`RAND_NUM_URL` variable in `Constants` class) might need to be changed for your environment.

This version deliberately has no AppDynamics MRUM instrumentation except for the `Constants` class, where the AppDynamics key (`APPD_KEY`) is assigned a dummy value.

For more comprehensive information on AppDynamics mobile application instrumentation, see-

[AppDynamics MRUM Landing Page](https://docs.appdynamics.com/latest/en/end-user-monitoring/mobile-real-user-monitoring)

## Instrumentation Overview

You need access to an AppDynamics Controller with MRUM license entitlements and source code access to your Android application. The license key information comes from the Controller and is entered into files in Android Studio.

You need to change five files in this Android project:

1. AndroidManifest.xml
2. **Top-level** build.gradle
3. **Module** build.gradle
4. Constants.java
5. MainActivity.java

Synchronize all files, clean your application build, and build the application fresh. The sections below describe in greater detail these milestones.

### AppDynamics Controller

Login to the Controller with a user that has rights to create a new MRUM application. Consult with your organization's AppDynamics administrator for help with this step. You likely do not have access to do this by default.

- Click *Getting Started* link
- Click *Getting Started Wizard* button
- Click *Android* button
- Click *Manual* button
- Look at *Step 2* and decide if you need to use an existing key or generate a new one
- Click *Continue* button

Keep this browser tab open as you proceed to the file modification steps below. You will need to refer back to information in this tab.

### AndroidManifest.xml

Verify this file contains the following lines:

```
<uses-permission android:name="android.permission.INTERNET"> </uses-permission>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"> </uses-permission>
```

These permissions are *critical* for instrumentation to work. MRUM sends encrypted events, metrics, and other data over the Internet so it can be seen by people in your organization in the Controller. If the Android app has no Internet or network permissions, none of the data will ever leave the Android device. In that scenario, the app is "instrumented" but you will never see any evidence of it.

### Top-level build.gradle

In Android Studio's left pane, verify you selected the *Android* view in the drop down (the Android's head with antennae icon). Other file/project views may not show you the relevant files.

- If necessary, expand the *Gradle Scripts* section (elephant icon)
- Double-click the *build.gradle (Project: NumberGuess)* file to open it for editing
- Look for the `dependencies` section and insert this line within the curly braces

`classpath "com.appdynamics:appdynamics-gradle-plugin:20.7.1" // this line added for AppDynamics`

Save your changes to this file.

### Module build.gradle

- In Android Studio's left pane, double-click *build.gradle (Module: NumberGuess.app)*
- Make several edits throughout this file, starting with `plugins {...}`

`id 'adeum'`

- Then `dependencies {...}`

`implementation 'com.appdynamics:appdynamics-runtime:20.7.1' // this line added for AppDynamics`

- Lastly, add this section at the end of the file

```
adeum { // this section added for AppDynamics
     account {
         name 'CHANGE ME!'
         licenseKey 'CHANGE ME!'
     }
     proguardMappingFileUpload {
         failBuildOnUploadFailure true //should build fail if upload fails? Defaults to false.
         enabled true //enables automatic uploads. Defaults to true.
     }
 }
```
 
Switch back to your browser tab showing the Controller to obtain the **name** and **licenseKey** values (CHANGE ME! will fail).

- Save your changes to this file
- Hit *Synch Now* link (near top right in the editor window)
- Save everything one more time (*File*, *Save All*)
- Synchronize the project (*File*, *Synch Project with Gradle Files*)

We are skipping the ProGuard configuration step in this example.

### Constants.java

- In Android Studio's left pane, expand the *java* folder and then the *com.example.numberguess* folder
- Double-click *Constants* to edit and change following line to match your MRUM key from the Controller

`public static final String APPD_KEY = "CHANGE ME!";`

Save your changes to this file.

### MainActivity.java

- In Android Studio's left pane, double-click *MainActivity* to edit it
- Add two import statements near the top of the file

```
import com.appdynamics.eumagent.runtime.AgentConfiguration;
import com.appdynamics.eumagent.runtime.Instrumentation;
```
 
- In the *onCreate()* method, add below the *super()* call the following

```
Instrumentation.start(AgentConfiguration.builder()
 .withAppKey(Constants.APPD_KEY)
 .withContext(getApplicationContext())
 // Configure the MRUM Agent to report the metrics and screenshots to
 // the SaaS EUM Server in Americas.
 
 .withCollectorURL("https://col.eum-appdynamics.com")
 .withScreenshotURL("https://image.eum-appdynamics.com")
 .build());
```
 
 - Modify the collector and screenshot URLs to reflect your geographic area needs (the Controller browser tab shows different URLs based on Americas, EMEA, APAC selection)
 - Save everything one more time (*File*, *Save All*)
 - *Build*, *Clean Project*
 - *Build*, *Rebuild Project*
 
Run the application in your Android simulator and play several rounds of the game. In the browser tab, click on *User Experience*, *Mobile Apps* and look for the NumberGuess application. Double-click on the tile when it appears. Explore the various metrics in the different dashboards.

