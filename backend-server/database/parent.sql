CREATE TABLE parent (
	parent_id	INT		PRIMARY KEY	AUTO_INCREMENT,
	email		VARCHAR(45)	NOT NULL,

	CONSTRAINT fk_parent_email
		FOREIGN KEY (email)
		REFERENCES auth(email)
);
