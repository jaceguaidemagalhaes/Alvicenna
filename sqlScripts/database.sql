-- drop database alvicenna;
-- create database alvicenna CHARACTER SET = utf8 COLLATE = utf8_general_ci;

USE alvicenna;

-- DROP TABLE IF EXISTS user;
CREATE TABLE user(
userId INT NOT NULL AUTO_INCREMENT,
username VARCHAR(50) NOT NULL,
password VARCHAR(50) NOT NULL,
userEmail VARCHAR(50) NOT NULL,
PRIMARY KEY (userId));

INSERT INTO user (username, password, userEmail)
VALUES ("jaceguai","123456","jaceguai@revature.net");

-- DROP TABLE IF EXISTS patient;
CREATE TABLE patient(
patientId INT NOT NULL AUTO_INCREMENT,
firstName VARCHAR(50) NOT NULL,
lastName VARCHAR(50) NOT NULL,
birthDate DATE,
gender VARCHAR(19),
patientEmail VARCHAR(50),
userId INT NOT NULL,
PRIMARY KEY (patientId),
FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE CASCADE);







