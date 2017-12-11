CREATE TABLE subscription (
	type_id		INT		NOT NULL,
	parent_id	INT		NOT NULL,
	starts		DATETIME	NOT NULL,
	expires		DATETIME	NOT NULL,

	PRIMARY KEY (type_id, parent_id),

	CONSTRAINT fk_subscription_type_id
		FOREIGN KEY (type_id)
		REFERENCES subscription_type(type_id),

	CONSTRAINT fk+subscription_parent_id
		FOREIGN KEY (parent_id)
		REFERENCES parent(parent_id)
);
