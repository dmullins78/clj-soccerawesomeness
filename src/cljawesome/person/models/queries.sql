-- name: insert-person<!
insert into persons (email, name) values (:email, :name)

-- name: insert-person-season<!
insert into seasons_persons (person_id, season_id, team_id) values (:personId, :seasonId, :teamId)

-- name: select-people-by-email
select * from persons
where email in (:emails)

-- name: insert-person-game-stats<!
insert into Persons_Games_Stats (season_id, person_id, game_id, yellow_card, red_card, goals, assists)
values (:seasonId, :personId, :gameId, :yellow_card, :red_card, :goals, :assists)


