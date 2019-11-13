CREATE TABLE task_members(
    task_id int REFERENCES tasks (id),
    user_id int REFERENCES project_members (user_id),
        PRIMARY KEY (task_id, user_id)
)