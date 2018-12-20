-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE AMAZINGCO(
  id VARCHAR(50) PRIMARY KEY,
  parent VARCHAR(50) NOT NULL,
  root VARCHAR(50) NOT NULL,
  height smallint
);

COPY AMAZINGCO FROM '/db/data/db.csv' WITH (FORMAT csv);