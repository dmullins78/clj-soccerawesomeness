CREATE TABLE Leagues (
    id        BIGSERIAL PRIMARY KEY,
    name       varchar(40) NOT NULL
);

CREATE TABLE Seasons (
    id        BIGSERIAL PRIMARY KEY,
    year      smallint NOT NULL,
    season    varchar(40) NOT NULL,
    league_id BIGSERIAL REFERENCES Leagues
);

CREATE TABLE Divisions (
    id        BIGSERIAL PRIMARY KEY,
    name    varchar(40) NOT NULL,
    season_id BIGSERIAL REFERENCES Seasons
);

CREATE TABLE Teams (
    id        BIGSERIAL PRIMARY KEY,
    name    varchar(40) NOT NULL,
    division_id BIGSERIAL REFERENCES Divisions,
    season_id BIGSERIAL REFERENCES Seasons
);

CREATE TABLE Games (
    id        BIGSERIAL PRIMARY KEY,
    home_team_id BIGSERIAL REFERENCES Teams,
    away_team_id BIGSERIAL REFERENCES Teams,
    home_team_score smallint NOT NULL,
    away_team_score smallint NOT NULL,
    start_time timestamp NOT NULL
);
