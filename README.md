upload & download files between client and server using dropbox
============

<br />

Just a doduck prototype
---------
This project is just a quick prototype aimed to explain the command line interface in Java and how to create a dropbox application.

Please read the related article on [Dropbox App in Java CLI](http://doduck.com/en/dropbox-app-in-java-cli/)
or in French [Développer une App Dropbox en Java](http://doduck.com/fr/dropbox-app-java/) <br />


 
Tutorial
=========

[![watch on Youtube]
(/README_src/watch-on-youtube-en.png "watch on Youtube")]
(http://www.youtube.com/watch?v=h3UtTEa-tEM)

[![watch on Youtube]
(/README_src/watch-on-youtube-fr.png "regarder sur Youtube")]
(http://www.youtube.com/watch?v=FK71SR0qgBg)


After watching the youtube video you will have an executable jar call "dir2server.jar"


Upload from your client
------
In order to upload a file in your dropbox run the command:
> java -jar --upload myBigFile.zip

First time your run this command you will get this kind of message:
> Accept the app by openning your browser at this URL: <br />
> https://www.dropbox.com:443/1/oauth/authorize?oauth_token=RwoTAsNV4W5AUcvN&locale=en <br />
> THEN press any key: <br />


Open your browser at this url and accept the app.

Then press any key into your console.



**WARNING:**
- The app will ONLY be able to access dropbox/Apps/dir2server/ **it can't access any other file** <br />
- In case you failed the authentification remove the file into .client2server created in your home folder


Download from your server
------
Just before uploading the file into your dropbox. You will get a message like

>You have to run on the server (Only once): <br />
>java -jar [JAR_NAME] --accessKey RwoTAsNV4W5AUcvN --secretKey RwoTAsNV4W5AUcvN<br />
><br />
>Run the following command on the server. It will start the download the file as soon as the upload finish<br />
>java -jar [JAR_NAME] --download myBigFile.zip<br />
><br />
>You can also get notify by email when the server finish to download the file.<br />
>java -jar [JAR_NAME] --download myBigFile.zip --email 'your@email.com' --smtp 'your.smtp.com' --login 'your login' --password 'your password'<br />
><br />
><br />
>Start upload ?  Y/N :


If it's the first time you use the app on your server run this command on your server:
>java -jar [JAR_NAME] --accessKey RwoTAsNV4W5AUcvN --secretKey RwoTAsNV4W5AUcvN

It will give your server all the requiment to access your dropbox/Apps/dir2server/ folder (Where the myBigFile.zip will be uploaded)


Then you can run the download from your server using the given command given before the uplaod:
>java -jar [JAR_NAME] --download myBigFile.zip

You can run this command before, during or after your uploaded the file!
dir2server.jar will download the file as soon as it's available to download.


You can get an email back when the download is finish using this kind of command line:
>java -jar [JAR_NAME] --download myBigFile.zip --email 'myemail@gmail.com' --smtp 'smtp.gmail.com' --login 'myemail@gmail.com' --password 'myPassword'<br />


Dealing with overwriting file
------
When you upload a file who already exist you will get a message like:
>Run the following command on the server. It will start the download the file as soon as the upload finish<br />
>java -jar [JAR_NAME] --download myBigFile.zip -x f2a87eb52e <br />

When running this parameter "-x f2a87eb52e" dir2server will only download the file after it at been uploaded. 
Under the hook it just wait until the version change from f2a87eb52e into something else.



Contact
=========
Developed by Martin Magakian
dev.martin.magakian@gmail.com
for [doduck prototype](http://doduck.com/)


License
=========
MIT License (MIT)

by [Anomaly Detection](https://anomaly.io)


