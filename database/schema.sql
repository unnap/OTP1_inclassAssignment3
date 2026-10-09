CREATE DATABASE IF NOT EXISTS temperature_db;

USE temperature_db;

CREATE TABLE IF NOT EXISTS TemperatureUnit (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               name VARCHAR(30) NOT NULL UNIQUE,
    symbol VARCHAR(10) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS TempRecord (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          input_value DOUBLE NOT NULL,
                                          original_unit_id INT NOT NULL,
                                          output_value DOUBLE NOT NULL,
                                          target_unit_id INT NOT NULL,

                                          CONSTRAINT fk_original_unit
                                          FOREIGN KEY (original_unit_id)
    REFERENCES TemperatureUnit(id),

    CONSTRAINT fk_target_unit
    FOREIGN KEY (target_unit_id)
    REFERENCES TemperatureUnit(id)
    );

INSERT IGNORE INTO TemperatureUnit (name, symbol)
VALUES
    ('Celsius', '°C'),
    ('Fahrenheit', '°F'),
    ('Kelvin', 'K');