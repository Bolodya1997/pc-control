CREATE TABLE log (
	log_id		INT		NOT NULL	AUTO_INCREMENT,
	parent_id	INT		NOT NULL,
	child_id	INT		NOT NULL,
	time		DATETIME	NOT NULL,
	type_id		INT		NOT NULL,
	info		JSON		NOT NULL,

	PRIMARY KEY (log_id, parent_id),

	CONSTRAINT fk_log_parent_id_child_id
		FOREIGN KEY (parent_id, child_id)
		REFERENCES child(parent_id, child_id),

	CONSTRAINT fk_log_type_id
		FOREIGN KEY (type_id)
		REFERENCES log_type(type_id)
);
