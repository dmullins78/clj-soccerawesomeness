-- name: game-was-modified<!
update games set last_updated = now() where id = :gameId

-- name: select-league-by-name
SELECT l.*, s.season, s.id as seasonId
from leagues l
inner join seasons s on s.league_id = l.id
where l.path = :path
and s.season = :season

-- name: select-league-season
SELECT
l.id,
l.name,
l.path as league,
s.season,
s.id as seasonId
FROM leagues l
inner join seasons s on s.league_id = l.id
WHERE lower(l.path) = :name
AND s.season = :season;

-- name: insert-game<!
INSERT INTO games (home_team_id, home_team_score, away_team_id, away_team_score, start_time, field, season_id)
VALUES (:home_team_id, :home_team_score, :away_team_id, :away_team_score, :start_time, :field, :seasonId);

-- name: mark-game-played<!
update games set update_count = update_count + 1 where id = :id;

-- name: insert-league<!
INSERT INTO leagues (path, name, description, location)
VALUES (:path, :name, :description, :location);

-- name: insert-season<!
INSERT INTO seasons (season, league_id)
VALUES (:season, :leagueId);

-- name: insert-team<!
INSERT INTO teams (name)
VALUES (:name);

-- name: insert-league-team<!
INSERT INTO teams_leagues (team_id, league_id)
VALUES (:teamId, :leagueId );

-- name: insert-season-team<!
INSERT INTO teams_seasons (team_id, season_id, division)
VALUES (:teamId, :seasonId, :division);

-- name: update-game-scores<!
update games set
update_count = update_count + 1,
home_team_score = :home_score,
away_team_score = :away_score,
comments = :comments,
last_updated = now()
where id = :id

-- name: update-game-details<!
update games set
field = :field,
start_time = :start_time
where id = :id

-- name: delete-player-game-stats<!
delete from players_games_stats where player_id = :playerId and game_id = :gameId

-- name: get-players-for-game
select
p.id,
p.name as playerName,
ps.goals,
ps.assists,
ps.card
from players p
inner join players_games_stats ps on ps.player_id = p.id
where ps.game_id = :gameId

-- name: get-offenders-for-game
select
p.id,
p.name as playerName,
ps.goals,
ps.assists,
ps.card
from players p
inner join players_games_stats ps on ps.player_id = p.id
where ps.game_id = :gameId
and ps.card in ('Y', 'R')

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
FROM players p
inner join seasons_players sp on p.id = sp.player_id
inner join teams t on t.id = sp.team_id
WHERE sp.season_id = :seasonId

-- name: get-players
SELECT p.*, sp.season_id as seasonid
FROM players p
inner join seasons_players sp on sp.player_id = p.id
WHERE sp.team_id = :teamId
AND sp.season_id = :seasonId
order by p.name

-- name: get-player-stats
select g.start_time as card_date, card from players_games_stats ps
inner join games g on g.id = ps.game_id
where ps.player_id = :playerId
and ps.season_id = :seasonId
and card in ('R', 'Y')
order by g.start_time

-- name: get-team
SELECT t.*
FROM teams t
WHERE t.id = :teamId

-- name: select-season-games
SELECT g.*
FROM games g
WHERE g.season_id = :seasonId

-- name: select-game
SELECT
g.id,
to_char(start_time, 'mm/DD/yyyy HH:MI AM') as starts_at,
g.field,
g.update_count,
comments,
home_team_id,
home_team_score as ht_score,
away_team_id,
away_team_score as at_score,
away.name as away_team,
home.name as home_team
FROM games g
inner join teams home on home_team_id = home.id
inner join teams away on away_team_id = away.id
WHERE g.id = :gameId

-- name: select-teams-by-season
SELECT t.* from teams t
inner join teams_seasons ts on ts.team_id = t.id and
ts.season_id = :seasonId
order by t.name

-- name: select-games
SELECT
g.id,
g.field,
g.update_count,
to_char(start_time, 'mm/DD/yyyy HH:MI AM') as starts_at,
comments,
home_team_id,
home_team_score as ht_score,
away_team_id,
away_team_score as at_score,
away.name as away_team,
home.name as home_team
FROM games g
inner join teams home on home_team_id = home.id
inner join teams away on away_team_id = away.id
WHERE season_id = :seasonId
AND (home_team_id = :team_id or away_team_id = :team_id)
order by g.start_time

-- name: select-recently-updated-games
SELECT
g.id,
g.field,
g.update_count,
to_char(start_time, 'mm/DD/yyyy HH:MI AM') as starts_at,
comments,
home_team_id,
home_team_score as ht_score,
away_team_id,
away_team_score as at_score,
away.name as away_team,
home.name as home_team
FROM games g
inner join teams home on home_team_id = home.id
inner join teams away on away_team_id = away.id
WHERE season_id = :seasonId
and g.last_updated >= current_date - interval '7 days'
and g.update_count > 0
order by g.start_time
