# library-apis
Project for developing APIs using REST,Apigee,SpringBoot and 

## DATABASE

- ERD

![ERD](https://user-images.githubusercontent.com/9671419/82833367-68b5e900-9edb-11ea-8c58-031605ae9c1f.PNG)

- LIST OF TABLES


![all TABLES](https://user-images.githubusercontent.com/9671419/82833511-cba78000-9edb-11ea-926e-aeec600c0dcd.PNG)

![User Table](https://user-images.githubusercontent.com/9671419/82833535-da8e3280-9edb-11ea-973c-546d4dacd20d.PNG)

![sequence tabke](https://user-images.githubusercontent.com/9671419/82833516-cf3b0700-9edb-11ea-8474-e3bbbc944990.PNG)


![Author_Publisher_TABLE](https://user-images.githubusercontent.com/9671419/82833699-6ef89500-9edc-11ea-876f-65c4d6523a9d.PNG)

![Book Table](https://user-images.githubusercontent.com/9671419/82833706-761fa300-9edc-11ea-8149-499d95036be3.PNG)

![JOIN_TABLES](https://user-images.githubusercontent.com/9671419/82833723-7f107480-9edc-11ea-999c-6ad3deeebee5.PNG)


Start the DB

Start MySQL Workbench

Create a connection to the DB

Create a DB

	CREATE DATABASE library_db
  
Change to the newly created DB

	USE library_db;
  
Create a new user with a password. This user will be used to connect from our application

	CREATE USER 'springuser'@'%' IDENTIFIED BY 'ThePassword'; 
  
Give all the privileges to the new libraryUser on the newly created database library_db

	GRANT ALL ON library_db.* TO 'springuser'@'%'; 	

## LOGS

create log file(library-apis) under the following location :

C:\tmp\logs\library-apis
