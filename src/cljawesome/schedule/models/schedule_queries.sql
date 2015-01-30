-- name: find-league-teams
select
    t.id,
    t.name
 from teams t
  inner join teams_leagues tl on tl.team_id = t.id
  inner join leagues l on tl.league_id = l.id
  where l.id = :leagueId

-- name: all-teams-by-league
select
    t.id,
    t.name
 from teams t
  inner join teams_leagues tl on tl.team_id = t.id
  inner join leagues l on tl.league_id = l.id
  where l.id = :leagueId
