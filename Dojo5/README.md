BlobifyMe
==========

This Dojo demonstrates the use of OpenCV on Android to perform simple blob detection on a captured image.  It utilizes the built in Android Camera app to capture the picture.

## Projects
* *blobifyme-ref* - A reference implementation of the application, used to prepare for the dojo and to check our work.
* *blobifyme-dojo* - The app built during the dojo.

Both projects use the same dependencies and are built using the same instructions.

## Dependencies

This project depends on the OpenCV for Android library, located at (http://sourceforge.net/projects/opencvlibrary/files/opencv-android/).  Make sure you download the library for Android; the "latest version" link will take you to the full OpenCV package, which will not work.

## Building[^1]

This guide assumes that you have Eclipse (tested on Indigo SR2) and Android SDK and ADT installed and configured.  OpenCV requires Android SDK 11 (Honeycomb), and BlobifyMe requires Android SDK 17 (Jelly Bean 4.2.2)

### Instructions:

1. Download the OpenCV for Android library, and extract on your local machine.  For the purposes of this guide, we'll use the folder ~/OpenCV4Android.

2. Import this project into Eclipse using *File -> Import -> Existing Projects into Workspace*

3. Clone the Dojo code onto your machine:  
```
cd ~/Development
https://github.com/pfunk-ml/Dojo.git
```

4. Import Dojo5/blobifyme into Eclipse using *File -> Import -> Existing Projects into Workspace*

5. Open the Android project properties for blobifyme by right clicking on the project, selecting *Properties*, and then clicking *Android* in the list on the left.

6. Add OpenCV as a referenced library by clicking the *Add* button near the bottom of the page, and selecting the OpenCV Library project.

## Running

The first time this is run, it will check if OpenCV Manager is installed.  If not, it will ask you if you want it to find OpenCV Manager, which will direct you to the Google Play store.  Complete the installation on Google Play, and then relaunch BlobifyMe.  You can also hit the back button to return to the currently launched BlobifyMe app.  It may ask you again to install OpenCV Manager, but you can press *No* in this case.

### To Operate

1. Press the *Take Picture* button to launch the camera interface
2. Take a picture, and click the check mark to accept it
3. TBD


[^1]: Build instructions adopted from (http://docs.opencv.org/doc/tutorials/introduction/android_binary_package/dev_with_OCV_on_Android.html)

