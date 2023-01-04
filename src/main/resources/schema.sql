DROP DATABASE IF EXISTS testdb;
CREATE DATABASE testdb;

USE testdb;

DROP TABLE IF EXISTS grp;
CREATE TABLE grp (
         id INT AUTO_INCREMENT PRIMARY KEY,
         name VARCHAR(255) NOT NULL,
         course int not null)
ENGINE=INNODB;

DROP TABLE IF EXISTS student;
CREATE TABLE student (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    id_grp INT NOT NULL,
    FOREIGN KEY (id_grp) REFERENCES grp(id) ON DELETE CASCADE )
ENGINE=INNODB;

DROP TABLE IF EXISTS subject;
CREATE TABLE subject (
       id INT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       hours int not null)
 ENGINE=INNODB;

DROP TABLE IF EXISTS student_subject;
CREATE TABLE student_subject (
       id_student INT NOT NULL,
       id_subject INT NOT NULL,
       PRIMARY KEY (id_student, id_subject),
       FOREIGN KEY (id_student) REFERENCES student(id),
       FOREIGN KEY (id_subject) REFERENCES subject(id))
ENGINE=INNODB;
