ALTER TABLE assistant
    ADD CONSTRAINT fk_superhero
        FOREIGN KEY (id) REFERENCES superhero (id)