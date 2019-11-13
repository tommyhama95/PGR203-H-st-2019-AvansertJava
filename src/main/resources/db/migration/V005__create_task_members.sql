CREATE TABLE task_members(
    task_id int REFERENCES tasks (id),
    project_id int NOT NULL,
    user_id int NOT NULL,
    FOREIGN KEY (project_id, user_id) REFERENCES  project_members (project_id, user_id),
    PRIMARY KEY (task_id, project_id, user_id)
);