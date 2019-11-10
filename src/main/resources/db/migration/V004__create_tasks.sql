CREATE TABLE tasks (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(150) NOT NULL DEFAULT 'Undefined',
    status VARCHAR(150) NOT NULL DEFAULT 'Undefined',
    project_id int REFERENCES projects (id)
);