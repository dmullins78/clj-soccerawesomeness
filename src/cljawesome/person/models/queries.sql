-- name: insert-person<!
insert into persons (email, name) values (:email, :name)

-- name: insert-person-season<!
insert into seasons_persons (person_id, season_id, team_id) values (:personId, :seasonId, :teamId)

-- name: select-people-by-email
select * from persons
where email in (:emails)
