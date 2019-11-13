CREATE TABLE project_members(
    project_id int REFERENCES  projects (id),
    user_id int REFERENCES  users (id),
        PRIMARY KEY (project_id, user_id)
);
