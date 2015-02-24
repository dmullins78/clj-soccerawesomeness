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
