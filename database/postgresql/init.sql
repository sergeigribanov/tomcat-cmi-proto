CREATE TABLE experiments (
       id SERIAL,
       etag TEXT UNIQUE NOT NULL
);

CREATE TABLE users (
       id SERIAL,
       username TEXT UNIQUE NOT NULL,
       userrole TEXT NOT NULL,
       hash TEXT NOT NULL
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

INSERT INTO users (username, userrole, hash) VALUES
('sergei', 'ADMIN', '$2a$12$/aw2PFksCDGbT6kiSQiLbu3RQgn2MoDoB6e5xDnx5dcstKfDo57iy');
