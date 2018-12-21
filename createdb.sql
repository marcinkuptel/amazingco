-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE AMAZINGCO(
  id uuid NOT NULL PRIMARY KEY,
  parent uuid NOT NULL,
  root uuid NOT NULL,
  height smallint
);

COPY AMAZINGCO FROM '/db/data/db.csv' WITH (FORMAT csv);