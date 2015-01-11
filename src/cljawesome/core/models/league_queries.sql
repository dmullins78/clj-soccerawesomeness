-- name: select-league-season
SELECT
    l.name as league,
    s.year,
    s.season
 FROM leagues l
  inner join seasons s on s.league_id = l.id
  WHERE lower(l.name) = :name
  AND s.year = year
  AND s.season = season;

-- name: insert-league<!
INSERT INTO leagues (name)
    VALUES (:name);

-- name: insert-season<!
INSERT INTO seasons (year, season, league_id)
    VALUES (:year, :season, :league_id);

-- name: insert-division<!
INSERT INTO divisions (season_id, name)
    VALUES (:season_id, :name);

-- name: insert-team<!
INSERT INTO teams (division_id, season_id, name)
    VALUES (:division_id, :season_id, :name);

-- name: insert-game<!
INSERT INTO games (home_team_id, home_team_score, away_team_id, away_team_score, start_time)
    VALUES (:home_team_id, :home_team_score, :away_team_id, :away_team_score, now());

-- name: select-teams
SELECT
    t.id,
    t.name,
    d.name as division
 FROM teams t
  inner join divisions d on t.division_id = d.id
  inner join seasons s on t.season_id = s.id
  inner join leagues l on s.league_id = l.id
  WHERE lower(l.name) = :name
  AND s.year = :year
  AND s.season = :season

-- name: select-games
SELECT
    home_team_id,
    home_team_score as ht_score,
    away_team_id,
    away_team_score as at_score
 FROM games
 WHERE home_team_id = :team_id or away_team_id = :team_id
