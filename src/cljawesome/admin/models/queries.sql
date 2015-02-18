-- name: insert-admin<!
insert into admins (email, password, league_id, role) values (:email, :password, :leagueId, 'teamadmin')

-- name: delete-league-admin<!
delete from admins where id = :adminId

-- name: update-league-admin<!
update admins set email = :email, password = :password where id = :adminId

-- name: get-league-admin
select * from admins where id = :adminId

-- name: get-league-admins
select * from admins where league_id = :leagueId
and role = 'teamadmin'
order by email
