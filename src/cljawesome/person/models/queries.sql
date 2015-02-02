-- name: insert-person<!
insert into persons (email, name) values (:email, :name)

-- name: insert-person-season<!
insert into seasons_persons (person_id, season_id) values (:personId, :seasonId)
