CREATE TABLE users (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(150) NOT NULL DEFAULT 'Undefined',
    email VARCHAR(150) DEFAULT 'Undefined'
);

/*
INSERT INTO users (name, email)
VALUES ('Mickey Mouse','moumic28@legacy.old'),
       ('Aragorn','middleage@helmsdeep.fort'),
       ('Hatsune Miku','sutegi@suki.ya');
*/