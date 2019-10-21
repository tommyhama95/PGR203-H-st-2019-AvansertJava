CREATE TABLE projects (
    id SERIAL NOT NULL PRIMARY KEY NOT NULL,
    name VARCHAR(150) NOT NULL DEFAULT 'Undefined'
);
/*
INSERT INTO projects (name)
VALUES ('Meta: Make JDBC Work'),
       ('Too Real: Get lots of sleep'),
       ('What: このお言葉がわからない'),
       ('Simple: Create HTTP Server');

 */