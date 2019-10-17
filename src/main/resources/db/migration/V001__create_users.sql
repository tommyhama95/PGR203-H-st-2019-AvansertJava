CREATE TABLE users (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(150) not null,
    email VARCHAR(150)
);

/*
INSERT INTO users (name, email)
VALUES ('Mickey Mouse','moumic28@legacy.old'),
       ('Aragorn','middleage@helmsdeep.fort'),
       ('Hatsune Miku','sutegi@suki.ya');
*/