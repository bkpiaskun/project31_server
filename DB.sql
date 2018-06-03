
CREATE DATABASE IF NOT EXISTS `nisd`;
USE `nisd`;

CREATE TABLE IF NOT EXISTS `passwords` (
  `Pass_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Destination` varchar(50) NOT NULL,
  `Destination_User` varchar(50) NOT NULL DEFAULT '',
  `Hashed_Password` varchar(255) NOT NULL,
  `Owner_ID` int(10) unsigned NOT NULL,
  `Last_Modified` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`Pass_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8 COMMENT='Hasła';

CREATE TABLE IF NOT EXISTS `users` (
  `User_ID` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `Login` varchar(20) NOT NULL,
  `PassHash` varchar(255) NOT NULL,
  PRIMARY KEY (`User_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='Użytkownicy';
