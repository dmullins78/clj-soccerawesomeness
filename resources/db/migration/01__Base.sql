CREATE TABLE Leagues (
  id        BIGSERIAL PRIMARY KEY,
  path       varchar(40) NOT NULL,
  name       varchar(60) NOT NULL,
  description    varchar(250) NOT NULL,
  location       varchar(60) NOT NULL
);

CREATE TABLE Seasons (
  id        BIGSERIAL PRIMARY KEY,
  season    varchar(60) NOT NULL,
  league_id BIGSERIAL REFERENCES Leagues,
  created timestamp default now()
);

CREATE TABLE Teams (
  id        BIGSERIAL PRIMARY KEY,
  name    varchar(40) NOT NULL
);

CREATE TABLE Teams_Leagues (
  team_id BIGSERIAL REFERENCES Teams,
  league_id BIGSERIAL REFERENCES Leagues,
  PRIMARY KEY(team_id, league_id)
);

CREATE TABLE Teams_Seasons (
  team_id BIGSERIAL REFERENCES Teams,
  season_id BIGSERIAL REFERENCES Seasons,
  division varchar(40),
  PRIMARY KEY(team_id, season_id)
);

CREATE TABLE Games (
  id        BIGSERIAL PRIMARY KEY,
  home_team_id BIGSERIAL REFERENCES Teams,
  away_team_id BIGSERIAL REFERENCES Teams,
  home_team_score smallint NOT NULL,
  away_team_score smallint NOT NULL,
  field varchar(60),
  comments varchar(1000),
  season_id BIGSERIAL REFERENCES Seasons,
  start_time timestamp NOT NULL,
  update_count smallint NOT NULL DEFAULT 0
);

CREATE TABLE Persons (
  id        BIGSERIAL PRIMARY KEY,
  name    varchar(100) NOT NULL,
  email    varchar(40) NOT NULL
);

CREATE TABLE Seasons_Persons (
  season_id BIGSERIAL REFERENCES Seasons,
  person_id BIGSERIAL REFERENCES Persons,
  team_id BIGSERIAL REFERENCES Teams,
  PRIMARY KEY(season_id, person_id, team_id)
);

CREATE TABLE Admins (
  id        BIGSERIAL PRIMARY KEY,
  league_id BIGSERIAL REFERENCES Leagues,
  email varchar(100) NOT NULL,
  password varchar(20) NOT NULL,
  role varchar(20)
);

CREATE TABLE Persons_Games_Stats (
  season_id BIGSERIAL REFERENCES Seasons,
  person_id BIGSERIAL REFERENCES Persons,
  game_id BIGSERIAL REFERENCES Games,
  card CHAR(1),
  goals smallint,
  assists smallint,
  PRIMARY KEY(season_id, person_id, game_id)
);
