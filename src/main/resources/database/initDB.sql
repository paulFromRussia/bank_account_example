CREATE TABLE IF NOT EXISTS account
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    pin  INTEGER NOT NULL,
    balance DECIMAL,
    CONSTRAINT unique_name UNIQUE(name)
);