FROM postgres
MAINTAINER ssgribanov@gmail.com
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD 1234
ENV POSTGRES_DB cmi
COPY init.sql /docker-entrypoint-initdb.d/10-init.sql
