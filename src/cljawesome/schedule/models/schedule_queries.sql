-- name: all-teams-by-league
select
    t.id,
    t.name
 from teams t
  inner join teams_leagues tl on tl.team_id = t.id
  inner join leagues l on tl.league_id = l.id
  where l.id = :leagueId
  order by t.name

-- name: import-teams-by-season
SELECT t.id, t.name from teams t
inner join teams_seasons ts on ts.team_id = t.id and
ts.season_id = :seasonId
union
SELECT a.team_id, a.alias from Team_Import_Alias a
where a.league_id = :leagueId

-- name: team-import-aliases
SELECT a.team_id as teamId, t.name as teamname, a.alias
from Team_Import_Alias a
inner join teams t on a.team_id = t.id
where a.league_id = :leagueId

-- name: teams-by-season
select
    t.id,
    t.name
 from teams t
  inner join teams_seasons ts on ts.team_id = t.id
  inner join seasons s on ts.season_id = s.id
  where s.id = :seasonId
  order by t.name

-- name: delete-season-teams<!
delete from games where season_id = :seasonId

-- name: delete-season-games<!
delete from teams_seasons where season_id = :seasonId

-- name: insert-league-alias<!
insert into team_import_alias (team_id, alias, league_id)
values (:teamId, :teamAlias, :leagueId);
