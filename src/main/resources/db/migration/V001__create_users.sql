CREATE TABLE users (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(150) NOT NULL DEFAULT 'Undefined',
    email VARCHAR(150) DEFAULT 'Undefined'
);