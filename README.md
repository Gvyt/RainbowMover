# RainbowMover
A pretty much useless app that plays and moves the peak of memes.
# But why?
I dunno.
I was bored :(
# Required Specs
You need a video playing capable Android device with Android 10+ (Android SDK 29+).
# Common Issues, and how to Fix them.
**App not installed.** </br>
This may be due to a bug in the APK. It is unfixable until a new version is pushed.
**App not installed as it conflicts with an existing package** </br>
Commonly happens when trying to install as an update to the APK when already installed. </br>
Uninstall the app, then try again.</br>
**Package file is invalid/There was a problem parsing the package.** </br>
Same as App not installed, and is a problem with the manifest. Create an issue to let me know. </br>
**Failed to install [name].apk on device: timeout** </br>
This means that your phone sent a timeout request because installation was taking too long. Try again to fix.</br>
**Insuffitent Storage** </br>
self explanitory.
## ADB Errors
**INSTALL_FAILED_ALREADY_EXISTS** </br>
The app exists already.</br>
**INSTALL_FAILED_INAVLID_APK** </br>
Same as App not installed. </br>
**INSTALL_FAILED_NO_MATCHING_ABIS** </br>
The java libraries on the app arent compatible with your phone/device.</br>
# FAQ
*Q: Still, why?*
A: i was bored i just said that

*Q: Why only Android though?*
A: i refuse to spend 873268763468623486999 hours trying to get a Mac to work again and plus, ipa files are very hard to make.

*Q: But still, wh-*
**A: I ALREADY TOLD YOU DANG IT**

*Q: OKAY GOD! But what are the files with "gradle" in them?**
A: Its the files to have the YML build the APK for release.
# Docs | How to make RainbowMover your own!
**Step 1**
Go to the MainActivity.java file, and change the package name from
``package com.gvyoutube.rainbowmover``
to
``package com.[NAME].[YOURAPPNAME]``.</br>
**Step 2**
Rename the folder.
Reame it from src/com/gvyoutube/rainbowmover 
to
src/com/[NAME]/[YOURAPPNAME].</br>
**Step 3, last step!**
Change the app name in MainActivity and the xml files. Its farily esay!
To change the video, make sure the link to it is a direct link, and is esaily downloadable.
