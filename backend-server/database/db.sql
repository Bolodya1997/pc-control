##################################################
###		SET EMPTY DATABASE
##################################################

CREATE DATABASE IF NOT EXISTS parental_control;
USE parental_control;

DROP TABLE IF EXISTS log;
DROP TABLE IF EXISTS log_type;
DROP TABLE IF EXISTS child;
DROP TABLE IF EXISTS policy;
DROP TABLE IF EXISTS subscription;
DROP TABLE IF EXISTS subscription_type;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS user;

##################################################
###		CREATE TABLES
##################################################

CREATE TABLE user (
	user_id		INT		PRIMARY KEY	AUTO_INCREMENT,
	email		VARCHAR(45)	NOT NULL	UNIQUE,
        password	VARCHAR(45)	NOT NULL
);

CREATE TABLE admin (
	user_id		INT		PRIMARY KEY,

	CONSTRAINT fk_admin_user_id
		FOREIGN KEY (user_id)
		REFERENCES user(user_id)
		ON DELETE CASCADE
);

CREATE TABLE subscription_type (
	type_id		INT		PRIMARY KEY,
	type_name	VARCHAR(45)	NOT NULL	UNIQUE,
	description	JSON		NOT NULL
);

CREATE TABLE subscription (
	subscription_id	INT		PRIMARY KEY	AUTO_INCREMENT,
	type_id		INT		NOT NULL,
	user_id		INT		NOT NULL,
	starts		DATETIME	NOT NULL,
	expires		DATETIME	NOT NULL,

	CONSTRAINT uq_subscription_type_id_user_id
		UNIQUE (type_id, user_id),

	CONSTRAINT fk_subscription_type_id
		FOREIGN KEY (type_id)
		REFERENCES subscription_type(type_id)
		ON DELETE RESTRICT,

	CONSTRAINT fk_subscription_user_id
		FOREIGN KEY (user_id)
		REFERENCES user(user_id)
		ON DELETE CASCADE
);

CREATE TABLE policy (
	policy_id	INT		PRIMARY KEY	AUTO_INCREMENT,
        user_id		INT		NOT NULL,
        last_modified	DATETIME	NOT NULL,
        sites		JSON		NOT NULL,
        applications	JSON		NOT NULL,
        schedule	JSON		NOT NULL,

	CONSTRAINT fk_policy_user_id
		FOREIGN KEY (user_id)
		REFERENCES user(user_id)
		ON DELETE CASCADE
);

CREATE TABLE child (
	child_id	INT		PRIMARY KEY	AUTO_INCREMENT,
	name		VARCHAR(45)	NOT NULL,
	user_id		INT		NOT NULL,
	policy_id	INT		NOT NULL,

	CONSTRAINT fk_child_user_id
		FOREIGN KEY (user_id)
		REFERENCES user(user_id)
		ON DELETE CASCADE,

	CONSTRAINT fk_child_policy_id
		FOREIGN KEY (policy_id)
		REFERENCES policy(policy_id)
		ON DELETE RESTRICT
);

CREATE TABLE log_type (
	type_id		INT		PRIMARY KEY,
	type_name	VARCHAR(45)	NOT NULL	UNIQUE
);

CREATE TABLE log (
	log_id		INT		PRIMARY KEY	AUTO_INCREMENT,
	user_id		INT		NOT NULL,
	child_id	INT		NOT NULL,
	time		DATETIME	NOT NULL,
	type_id		INT		NOT NULL,
	info		JSON		NOT NULL,

	CONSTRAINT fk_log_user_id_child_id
		FOREIGN KEY (user_id, child_id)
		REFERENCES child(user_id, child_id)
		ON DELETE CASCADE,

	CONSTRAINT fk_log_type_id
		FOREIGN KEY (type_id)
		REFERENCES log_type(type_id)
		ON DELETE RESTRICT
);

##################################################
###		INSERT VALUES
##################################################

INSERT INTO subscription_type (type_id, type_name, description)
	VALUES (0, 'default', '{}');

INSERT INTO log_type (type_id, type_name)
	VALUES (0, "apps_log"), (1, "system_log");

##################################################
###		CREATE USER
##################################################

DROP USER IF EXISTS spring_boot;	# revoke all priveleges
CREATE USER spring_boot;

GRANT SELECT, INSERT, UPDATE, DELETE
	ON user
	TO spring_boot;

GRANT SELECT
	ON admin
	TO spring_boot;

GRANT SELECT
	ON subscription_type
	TO spring_boot;

GRANT SELECT, INSERT
	ON subscription
	TO spring_boot;

GRANT SELECT, INSERT, UPDATE, DELETE
	ON policy
	TO spring_boot;

GRANT SELECT, INSERT, UPDATE, DELETE
	ON child
	TO spring_boot;

GRANT SELECT
	ON log_type
	TO spring_boot;

GRANT SELECT, INSERT
	ON log
	TO spring_boot;

##################################################
###		TRIGGERS
##################################################

DELIMITER //

CREATE TRIGGER after_user_insert AFTER INSERT ON user
        FOR EACH ROW
BEGIN
        INSERT INTO subscription (
                type_id,
                user_id,
                starts,
                expires
        ) VALUES (
                0,
                NEW.user_id,
                NOW(),
                STR_TO_DATE('9999-12-31', '%Y-%m-%d')
        );
END;//

DELIMITER ;
