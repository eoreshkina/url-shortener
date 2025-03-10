CREATE SCHEMA IF NOT EXISTS shortener;

CREATE TABLE shortener.url_mapping (
    id UUID PRIMARY KEY,
    long_url VARCHAR(2048) NOT NULL,
    short_url VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_short_url ON shortener.url_mapping(short_url);