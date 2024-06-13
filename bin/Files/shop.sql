DROP DATABASE IF EXISTS shop;
CREATE DATABASE shop;
USE shop;

CREATE TABLE employee (
    employeeId INT PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100)
);

INSERT INTO employee (employeeId, username, password) VALUES (1, 'sadmin', '1234');
INSERT INTO employee (employeeId, username, password) VALUES (2, 'admin', '1234');
INSERT INTO employee (employeeId, username, password) VALUES (3, 'user', '1234');