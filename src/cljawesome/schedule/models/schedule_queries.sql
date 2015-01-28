-- name: all-teams-by-league
SELECT t.id, t.name
 FROM teams t
  inner join seasons s on s.id = t.season_id
  inner join leagues l on l.id = s.league_id
  WHERE l.id = :league_id;

