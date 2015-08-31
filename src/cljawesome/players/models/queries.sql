-- name: find-players-by-name
select p.id, p.name
from players p
inner join seasons_players sp on sp.player_id = p.id
where sp.season_id = :seasonId
and lower(p.name) like :query
order by name;

-- name: top-offenders
select p.name, t.name as team, ts.division,
sum(CASE WHEN (ps.card = 'Y' or ps.card = 'R') then 1 else 0 end) as cards,
sum(CASE WHEN (ps.card = 'Y') then 1 else 0 end) as yellows,
sum(CASE WHEN (ps.card = 'R') then 1 else 0 end) as reds
from players p
inner join players_games_stats ps on ps.player_id = p.id
inner join games g on g.id = ps.game_id and g.season_id = :seasonId
inner join seasons_players sp on sp.player_id = p.id and sp.season_id = :seasonId
inner join teams t on t.id = sp.team_id
inner join teams_seasons ts on ts.team_id = t.id and ts.season_id = sp.season_id
where sp.season_id = :seasonId
and ps.card != 'N'
group by p.name, t.name, ts.division
order by cards
desc limit 5;

-- name: top-offenders-by-division
select p.name, t.name as team, ts.division,
sum(CASE WHEN (ps.card = 'Y' or ps.card = 'R') then 1 else 0 end) as cards,
sum(CASE WHEN (ps.card = 'Y') then 1 else 0 end) as yellows,
sum(CASE WHEN (ps.card = 'R') then 1 else 0 end) as reds
from players p
inner join players_games_stats ps on ps.player_id = p.id
inner join games g on g.id = ps.game_id and g.season_id = :seasonId
inner join seasons_players sp on sp.player_id = p.id and sp.season_id = :seasonId
inner join teams t on t.id = sp.team_id
inner join teams_seasons ts on ts.team_id = t.id and ts.season_id = sp.season_id
where sp.season_id = :seasonId
and ts.division = :division
and ps.card != 'N'
group by p.name, t.name, ts.division
order by cards
desc limit 10;

-- name: top-scorers
select p.name, t.name as team, ts.division, sum(ps.goals + ps.assists) as value, sum(ps.goals) as goals, sum(ps.assists) as assists
from players p
inner join players_games_stats ps on ps.player_id = p.id
inner join games g on g.id = ps.game_id and g.season_id = :seasonId
inner join seasons_players sp on sp.player_id = p.id and sp.season_id = :seasonId
inner join teams t on t.id = sp.team_id
inner join teams_seasons ts on ts.team_id = t.id and ts.season_id = sp.season_id
group by p.name, t.name, ts.division
order by value desc limit 5

-- name: top-scorers-by-division
select p.name, t.name as team, ts.division, sum(ps.goals + ps.assists) as value, sum(ps.goals) as goals, sum(ps.assists) as assists
from players p
inner join players_games_stats ps on ps.player_id = p.id
inner join games g on g.id = ps.game_id and g.season_id = :seasonId
inner join seasons_players sp on sp.player_id = p.id and sp.season_id = :seasonId
inner join teams t on t.id = sp.team_id
inner join teams_seasons ts on ts.team_id = t.id and ts.season_id = sp.season_id
where sp.season_id = :seasonId
and ts.division = :division
group by p.name, t.name, ts.division
order by value desc limit 10

-- name: insert-player<!
insert into players (email, name) values (:email, :name)

-- name: insert-player-season<!
insert into seasons_players (player_id, season_id, team_id) values (:playerId, :seasonId, :teamId)

-- name: select-players-by-email
select * from players
where email in (:emails)

-- name: insert-player-game-stats<!
insert into Players_Games_Stats (season_id, player_id, game_id, card, goals, assists)
values (:seasonId, :playerId, :gameId, :card, :goals, :assists)

-- name: admin-roles
select a.id, a.league_id as leagueid, a.email, a.role from Admins a
where a.email = :email and a.password = :password

-- name: reset-season-roster<!
delete from seasons_players where season_id = :seasonId

-- name: active-season
select
   lower(l.path) as league,
   lower(s.season) as season
FROM
   seasons s
INNER Join
   leagues l
      On l.id = s.league_id
WHERE
   l.id = :leagueId
ORDER BY
   s.created DESC limit 1
