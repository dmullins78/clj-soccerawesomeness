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
select p.id, p.name, p.email, role from  League_Admins
inner join persons p on p.id = league_admins.person_id
where p.email = :email
union
select p.id, p.name, p.email, role from  Team_Admins
inner join persons p on p.id = team_admins.person_id
where p.email = :email
