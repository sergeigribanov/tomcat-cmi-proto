CREATE TABLE experiments (
       id SERIAL,
       etag TEXT UNIQUE NOT NULL,
       CONSTRAINT etag_ct CHECK (etag ~ '^[A-Za-z0-9]+$')
);

CREATE TABLE epoints (
       id SERIAL,
       etag TEXT NOT NULL references experiments(etag),
       ptag TEXT UNIQUE NOT NULL,
       ebeam REAL,
       ebeam_err REAL,
       mfield REAL,
       CONSTRAINT ptag_ct CHECK (ptag ~ '^[0-9\.]+_\d+$')
);

CREATE TABLE userroles (
       id SERIAL,
       userrole TEXT UNIQUE NOT NULL,
       CONSTRAINT userrole CHECK (userrole ~ '^[A-Z0-9]+$')
);

CREATE TABLE users (
       id SERIAL,
       username TEXT UNIQUE NOT NULL,
       userrole TEXT NOT NULL references userroles(userrole),
       hash TEXT NOT NULL
);

INSERT INTO experiments (etag) VALUES
('HIGH2011'),
('HIGH2012'),
('HIGH2017'),
('HIGH2019');

INSERT INTO userroles (userrole) VALUES ('ADMIN');

INSERT INTO users (username, userrole, hash) VALUES
('sergei', 'ADMIN', '$2a$12$/aw2PFksCDGbT6kiSQiLbu3RQgn2MoDoB6e5xDnx5dcstKfDo57iy');
