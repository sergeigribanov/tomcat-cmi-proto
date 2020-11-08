CREATE TABLE experiments (
       id SERIAL,
       etag TEXT UNIQUE NOT NULL
);

CREATE TABLE epoints (
       id SERIAL,
       etag TEXT NOT NULL references experiments(etag),
       ptag TEXT UNIQUE NOT NULL,
       ebeam REAL,
       ebeam_err REAL,
       mfield REAL
);

INSERT INTO experiments (etag) VALUES
('HIGH2011'),
('HIGH2012'),
('HIGH2017'),
('HIGH2019');
