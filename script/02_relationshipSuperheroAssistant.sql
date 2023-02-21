ALTER TABLE assistant
    ADD CONSTRAINT fk_superhero
        FOREIGN KEY (superhero_id) REFERENCES superhero (id)