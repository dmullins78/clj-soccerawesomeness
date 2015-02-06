-- name: all-teams-by-league
select
    t.id,
    t.name
 from teams t
  inner join teams_leagues tl on tl.team_id = t.id
  inner join leagues l on tl.league_id = l.id
  where l.id = :leagueId
  order by t.name

-- name: teams-by-season
select
    t.id,
    t.name
 from teams t
  inner join teams_seasons ts on ts.team_id = t.id
  inner join seasons s on ts.season_id = s.id
  where s.id = :seasonId
  order by t.name
