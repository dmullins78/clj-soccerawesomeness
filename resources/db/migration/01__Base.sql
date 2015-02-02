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
    season_id BIGSERIAL REFERENCES Seasons,
    start_time timestamp NOT NULL
);

CREATE TABLE Persons (
    id        BIGSERIAL PRIMARY KEY,
    name    varchar(100) NOT NULL,
    email    varchar(40) NOT NULL
);

CREATE TABLE Seasons_Persons (
    season_id BIGSERIAL REFERENCES Seasons,
    person_id BIGSERIAL REFERENCES Persons,
    PRIMARY KEY(season_id, person_id)
);

