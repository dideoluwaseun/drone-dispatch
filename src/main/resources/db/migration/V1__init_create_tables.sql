CREATE TABLE IF NOT EXISTS drone (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       serial_number VARCHAR(100) NOT NULL UNIQUE,
                       model VARCHAR(255) NOT NULL,
                       battery_capacity INT NOT NULL,
                       state VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS medication (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            weight DOUBLE NOT NULL,
                            code VARCHAR(255) NOT NULL UNIQUE
                            image VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS drone_medication (
                                  drone_id BIGINT,
                                  medication_id BIGINT,
                                  PRIMARY KEY (drone_id, medication_id),
                                  FOREIGN KEY (drone_id) REFERENCES drone (id),
                                  FOREIGN KEY (medication_id) REFERENCES medication (id)
);

CREATE TABLE IF NOT EXISTS audit_event_log (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               drone_id BIGINT,
                               time TIMESTAMP,
                               battery_capacity INT NOT NULL,
                               FOREIGN KEY (drone_id) REFERENCES drone (id)
);
