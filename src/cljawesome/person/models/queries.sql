-- name: insert-person<!
insert into persons (email, name) values (:email, :name)

-- name: insert-person-season<!
insert into seasons_persons (person_id, season_id, team_id) values (:personId, :seasonId, :teamId)

-- name: select-people-by-email
select * from persons
where email in (:emails)

-- name: insert-person-game-stats<!
insert into Persons_Games_Stats (season_id, person_id, game_id, card, goals, assists)
values (:seasonId, :personId, :gameId, :card, :goals, :assists)

-- name: admin-roles
select a.id, a.league_id as leagueid, a.email, a.role from Admins a
where a.email = :email and a.password = :password

-- name: reset-season-roster<!
delete from seasons_persons where season_id = :seasonId

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
