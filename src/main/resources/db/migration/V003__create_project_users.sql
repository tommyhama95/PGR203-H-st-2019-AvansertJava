CREATE TABLE project_users(
    project_id int REFERENCES  projects (id), user_id int REFERENCES  users (id),
    PRIMARY KEY (project_id, user_id)
);
/*
INSERT INTO project_users (project_id, user_id)
VALUES (1,2),
       (1,3)

 */