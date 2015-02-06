-- name: select-league-season
SELECT
l.id,
l.name as league,
s.year,
s.season,
s.id as seasonId
FROM leagues l
inner join seasons s on s.league_id = l.id
WHERE lower(l.name) = :name
AND s.year = :year
AND s.season = :season;

-- name: insert-league<!
INSERT INTO leagues (name)
VALUES (:name);

-- name: insert-season<!
INSERT INTO seasons (year, season, league_id)
VALUES (:year, :season, :league_id);

-- name: insert-team<!
INSERT INTO teams (name)
VALUES (:name);

-- name: insert-league-team<!
INSERT INTO teams_leagues (team_id, league_id)
VALUES (:teamId, :leagueId );

-- name: insert-season-team<!
INSERT INTO teams_seasons (team_id, season_id, division)
VALUES (:teamId, :seasonId, :division);

-- name: insert-game<!
INSERT INTO games (home_team_id, home_team_score, away_team_id, away_team_score, start_time, field, season_id)
VALUES (:home_team_id, :home_team_score, :away_team_id, :away_team_score, :start_time, :field, :seasonId);

-- name: select-season-teams
select
t.id,
t.name,
s.id as seasonId,
ts.division
from teams t
inner join teams_leagues tl on tl.team_id = t.id
inner join leagues l on tl.league_id = l.id
inner join teams_seasons ts on ts.team_id = t.id
inner join seasons s on s.id = ts.season_id
where ts.season_id = :seasonId

-- name: select-season-players
SELECT p.*, t.name as team
FROM persons p
inner join seasons_persons sp on p.id = sp.person_id
inner join teams t on t.id = sp.team_id
WHERE sp.season_id = :seasonId

-- name: select-season-games
SELECT g.*
FROM games g
WHERE g.season_id = :seasonId

-- name: select-games
SELECT
home_team_id,
home_team_score as ht_score,
away_team_id,
away_team_score as at_score
FROM games
WHERE home_team_id = :team_id or away_team_id = :team_id
AND season_id = :seasonId
