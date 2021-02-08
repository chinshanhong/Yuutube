# Yuutube
My first programming project
Welcome to Yuutube project!


Things that provided at the submited zip/rar file
- Yuutube (Netbean Project Folder)
- WIX1002 Managerial Report Group 1 OCC5 (Managerial Report)
- WIX1002 Technical Report Group 1 OCC5 (Technical Report)
- yuutube.sql (SQL File)
- videofile(the file that use to store all the videos)
- Assignment Evaluation (Marking file)
-----------------------------------------------------------------------------------------------
Things to install before running this program
- Netbean IDE 12.1
- mysql-connector-java-8.0.22.jar
- JDK 15
- XAMPP latest version

Things to do after installing everything
videofile
-put it somewhere (recommend to put at desktop)
----------------------------------------------------------------------------------------------
netbean
-open project using netbean(Yuutube project)
-import library to library file(after import into yuutube project) JDK 15 and mysql connector
------------------------------------------------------------------------------------------------
database setup
- Start xampp (apache and mysql)
- press admin at MySQL and direct u to localhost
- create a new table name yuutube
- import the database(yuutube.sql) into it
- go to netbean database
- set new connection
- select mysql connector/j driver
- select from the path (mysql connector that you download)
- Config
Database:yuutube
- press next (if failure means you didnt do the xampp installation correctly
- Congratulation! you setup your database correctly!
------------------------------------------------------------------------------------
If your JDK not version 15...
-Go to download the JDK 15
-Install at somewhere else
-Open your notepad and run as administrator
-go to your netbean folder
Path : C:\Program Files\NetBeans-12.1\netbeans\etc
-Edit the netbeans.conf file
- Find and replace the file path with the the JDK 15 u download.
netbeans_jdkhome="C:\Program Files\RedHat\java-11-openjdk-11.0.8-2"
- Restart netbean and you will have JDK 15.

----------------------------------------------------------------------------
If you have issues when playing the video...
Go to the database and change the file path from (for all 40 videos)

C:\Users\User\Desktop\videofile\Film & Animation\Avengers Assemble.mp4

to 

C:\Users\(Ur file path that you put your video file at)\Desktop\videofile\(category)\(the video you wanna play).mp4

This will solve your problem when you try to play video!
THE END!
The yuutube project will work now!

This readme file is written by TEH KAI KEAT U2005485
If cannot work feel free to whatsapp me (TEH 014-6693582)

People who involve this project
- Teh
- Patricia
- Shan Hong
- Alif
- Ee Wern
