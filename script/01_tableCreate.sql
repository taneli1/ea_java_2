DROP TABLE IF EXISTS superhero_power;
DROP TABLE IF EXISTS power;
DROP TABLE IF EXISTS assistant;
DROP TABLE IF EXISTS superhero;


CREATE TABLE superhero
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    alias  VARCHAR(255),
    origin VARCHAR(255)
);

CREATE TABLE assistant
(
    id           SERIAL PRIMARY KEY,
    superhero_id INT          NOT NULL,
    name         VARCHAR(255) NOT NULL
);

CREATE TABLE power
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

