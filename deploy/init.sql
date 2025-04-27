CREATE TABLE IF NOT EXISTS call_histories (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    endpoint VARCHAR(255),
    parameters TEXT,
    response TEXT,
    error TEXT
);
