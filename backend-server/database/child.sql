CREATE TABLE child (
	child_id	INT		NOT NULL	AUTO_INCREMENT,
	name		VARCHAR(45)	NOT NULL,
	parent_id	INT		NOT NULL,
	policy_id	INT		NOT NULL,

	PRIMARY KEY (child_id, parent_id),

	CONSTRAINT fk_child_parent_id
		FOREIGN KEY (parent_id)
		REFERENCES parent(parent_id),

	CONSTRAINT fk_child_policy_id
		FOREIGN KEY (policy_id)
		REFERENCES policy(policy_id)
);
