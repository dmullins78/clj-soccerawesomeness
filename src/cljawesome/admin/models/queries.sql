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
select p.id, p.name, p.email, a.role from Admins a
inner join persons p on p.id = a.person_id
where p.email = :email and a.password = :password

-- name: admin-roles
select p.id, p.name, p.email, a.role, a.id as adminid
FROM Admins a
inner join persons p on p.id = a.person_id
where p.email = :email
--and a.password = :password

-- name: reset-season-roster<!
delete from seasons_persons where season_id = :seasonId

-- name: admin-roles-teams
select t.team_id as id
FROM Admin_teams t
where t.admin_id = :adminId

-- name: active-season
select
   lower(l.name) as league,
   lower(s.season) as season,
   s.year
FROM
   seasons s
INNER join
   seasons_persons sp
      ON s.id = sp.season_id
INNER Join
   leagues l
      On l.id = s.league_id
WHERE
   sp.person_id = :personId
ORDER BY
   s.created DESC limit 1
