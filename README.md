# library-apis
Project for developing APIs using REST,Apigee,SpringBoot and 

## DATABASE

- ERD

![ERD](https://user-images.githubusercontent.com/9671419/82833367-68b5e900-9edb-11ea-8c58-031605ae9c1f.PNG)

- LIST OF TABLES


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
