DROP TABLE IF EXISTS client;
CREATE TABLE client(
  id serial PRIMARY KEY,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL
);

DROP TABLE IF EXISTS payment;
CREATE TABLE payment(
  id serial PRIMARY KEY,
  clientId INTEGER NOT NULL FOREIGN KEY REFERENCES client(id),
  amount NUMERIC NOT NULL,
  date BIGINT NOT NULL
);

DROP TABLE IF EXISTS session;
CREATE TABLE session(
  id serial PRIMARY KEY,
  clientId INTEGER FOREIGN KEY REFERENCES client(id),
  startTime BIGINT,
  endTime BIGINT,
  status VARCHAR(100)
);