update town
set luxury_resource = (select island.luxury_resource from island where island.id = town.island_id)
where luxury_resource is null;

update town
set resources_updated_at = now()
where resources_updated_at is null;

update player
set gold_updated_at = now()
where gold_updated_at is null;
