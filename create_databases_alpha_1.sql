DROP DATABASE IF EXISTS cos_stories_db;
CREATE DATABASE IF NOT EXISTS cos_stories_db;

USE cos_stories_db;

CREATE TABLE stories1
(
	id 			bigint 			NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name 		varchar(32) 	NOT NULL,
	created 	varchar(32) 	NOT NULL,
	location 	varchar(32) 	NOT NULL,
	fileName 	varchar(64) 	NOT NULL
);


DROP DATABASE IF EXISTS cos_management_db;
CREATE DATABASE IF NOT EXISTS cos_management_db;

USE cos_management_db;

CREATE TABLE tableamounts
(
	tableName 	varchar(32) 	NOT NULL PRIMARY KEY,
	latestIndex int 			NOT NULL
);