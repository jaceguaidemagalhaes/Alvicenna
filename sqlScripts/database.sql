-- drop database alvicenna;
-- create database alvicenna CHARACTER SET = utf8 COLLATE = utf8_general_ci;

USE alvicenna;
-- SET GLOBAL local_infile = 'ON';
-- show variables LIKE "secure_file_priv";
-- SELECT @@secure_file_priv;

SET SQL_SAFE_UPDATES=0;


DROP TABLE IF EXISTS user;
CREATE TABLE user(
userId INT NOT NULL AUTO_INCREMENT,
username VARCHAR(50) NOT NULL,
password VARCHAR(50) NOT NULL,
userEmail VARCHAR(50) NOT NULL,
PRIMARY KEY (userId));

INSERT INTO user (username, password, userEmail)
VALUES ("jaceguai","123456","jaceguai@test.net");

DROP TABLE IF EXISTS patient;
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

drop table healthData;
-- select * from healthdata;
CREATE TABLE healthData(
healthDataId INT NOT NULL AUTO_INCREMENT,
temperature decimal(4,1),
pulse int,
readingTime Datetime not null,
observations varchar(250),
systolic Int,
diastolic Int,
oxygenLevel Decimal(3,1),
patientId Int not null,
primary key (healthDataId),
foreign key (patientId) references patient(patientId) on delete cascade);

-- insert into healthData (temperature, readingTime, oxygenLevel, patientId)
-- values (100.1,"2000-03-03", 98.2, 1);

drop table if exists drug;
CREATE TABLE drug(
drugId INT NOT NULL AUTO_INCREMENT,
drugName varchar(50) not null,
typeOfdrug varchar(15),
remarks varchar(250),
primary key (drugId));

drop table if exists prescription;
CREATE TABLE prescription(
prescriptionId INT NOT NULL AUTO_INCREMENT,
doctor varchar(50) not null,
prescriptionDate date not null,
active bool default true,
dateLastFilling Date,
quantityLastFilling Int,
patientId Int not null,
primary key (prescriptionId),
foreign key (patientId) references patient(patientId) on delete cascade);


drop table if exists prescriptiodrug;
select * from prescriptionmedicine;
CREATE TABLE prescriptionDrug(
prescriptionDrugId INT NOT NULL AUTO_INCREMENT,
prescriptionId Int Not Null,
drugId Int,
instructions varchar(250),
primary key (prescriptionDrugId),
foreign key (prescriptionId) references prescription(prescriptionId) on delete cascade,
foreign key (drugId) references drug(drugId));



