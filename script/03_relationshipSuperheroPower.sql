CREATE TABLE superhero_power
(
    superhero_id INT NOT NULL,
    power_id     INT NOT NULL
);

ALTER TABLE superhero_power
    ADD CONSTRAINT pk_composite PRIMARY KEY (superhero_id, power_id);

ALTER TABLE superhero_power
    ADD CONSTRAINT fk_superhero FOREIGN KEY (superhero_id) REFERENCES superhero (id);

ALTER TABLE superhero_power
    ADD CONSTRAINT fk_power FOREIGN KEY (power_id) REFERENCES power (id);