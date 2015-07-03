DROP DATABASE IF EXISTS cos_users_db;
CREATE DATABASE IF NOT EXISTS cos_users_db;

USE cos_users_db;

CREATE TABLE users1
(
	id 			bigint 			NOT NULL PRIMARY KEY AUTO_INCREMENT,
	userName 	varchar(62) 	NOT NULL
);

CREATE TABLE loginkeys1
(
	userID 		bigint 			NOT NULL PRIMARY KEY,
	userKey 	varchar(62) 	NOT NULL,
	created 	varchar(32) 	NOT NULL
);

CREATE TABLE secure1
(
	userID 		bigint 			NOT NULL PRIMARY KEY,
	passwordHash varchar(255) 	NOT NULL,
	email 		varchar(64) 	NOT NULL
);


DROP DATABASE IF EXISTS cos_stories_db;
CREATE DATABASE IF NOT EXISTS cos_stories_db;

USE cos_stories_db;

CREATE TABLE stories1
(
	id 			bigint 			NOT NULL PRIMARY KEY AUTO_INCREMENT,
	creatorID 	bigint 			NOT NULL,
	name 		varchar(32) 	NOT NULL,
	created 	varchar(32) 	NOT NULL,
	location 	varchar(32) 	NOT NULL,
	fileName 	varchar(64) 	NOT NULL,
	duration 	varchar(32) 	NOT NULL
);


DROP DATABASE IF EXISTS cos_management_db;
CREATE DATABASE IF NOT EXISTS cos_management_db;

USE cos_management_db;

CREATE TABLE tableamounts
(
	tableName 	varchar(32) 	NOT NULL PRIMARY KEY,
	latestIndex int 			NOT NULL
);