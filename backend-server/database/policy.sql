CREATE TABLE policy (
	policy_id	INT		NOT NULL	AUTO_INCREMENT,
	parent_id	INT		NOT NULL,
	last_modified	DATETIME	NOT NULL,
	sites		JSON		NOT NULL,
	applications	JSON		NOT NULL,
	schedule	JSON		NOT NULL,

	PRIMARY KEY (policy_id, parent_id),

	CONSTRAINT fk_policy_parent_id
		FOREIGN KEY (parent_id)
		REFERENCES parent(parent_id)
);
