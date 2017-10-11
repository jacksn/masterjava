DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS users_groups;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS projects;

DROP SEQUENCE IF EXISTS city_seq;
DROP SEQUENCE IF EXISTS user_seq;
DROP SEQUENCE IF EXISTS group_seq;
DROP SEQUENCE IF EXISTS project_seq;

DROP TYPE IF EXISTS USER_FLAG;
DROP TYPE IF EXISTS GROUP_TYPE;

CREATE TYPE USER_FLAG AS ENUM ('active', 'deleted', 'superuser');
CREATE TYPE GROUP_TYPE AS ENUM ('registering', 'current', 'finished');

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE project_seq START 100000;
CREATE SEQUENCE group_seq START 100000;
CREATE SEQUENCE city_seq START 100000;

CREATE TABLE cities (
  id   INTEGER PRIMARY KEY DEFAULT nextval('city_seq'),
  code VARCHAR(3) NOT NULL,
  name TEXT       NOT NULL
);

CREATE UNIQUE INDEX code_name_idx
  ON cities (code, name);

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT      NOT NULL,
  email     TEXT      NOT NULL,
  flag      USER_FLAG NOT NULL,
  city_id   INTEGER REFERENCES cities (id)
);

CREATE UNIQUE INDEX email_idx
  ON users (email);

CREATE TABLE projects (
  id          INTEGER PRIMARY KEY DEFAULT nextval('project_seq'),
  name        TEXT NOT NULL,
  description TEXT NOT NULL
);

CREATE UNIQUE INDEX project_name_idx
  ON projects (name);

CREATE TABLE groups (
  id         INTEGER PRIMARY KEY DEFAULT nextval('group_seq'),
  name       TEXT       NOT NULL,
  type       GROUP_TYPE NOT NULL,
  project_id INTEGER REFERENCES projects (id)
);

CREATE UNIQUE INDEX group_name_idx
  ON groups (name);

CREATE TABLE users_groups (
  user_id INTEGER REFERENCES users(id),
  group_id INTEGER REFERENCES groups(id)
);
