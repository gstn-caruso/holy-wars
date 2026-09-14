ALTER TABLE town ADD COLUMN luxury_resource TEXT NOT NULL DEFAULT 'WINE' CHECK (luxury_resource IN ('WINE', 'MARBLE', 'CRYSTAL', 'SULFUR'));
ALTER TABLE town ADD COLUMN wood_ticks INTEGER NOT NULL DEFAULT 1800000;
ALTER TABLE town ADD COLUMN luxury_ticks INTEGER NOT NULL DEFAULT 360000;
ALTER TABLE town ADD COLUMN resources_updated_at INTEGER NOT NULL DEFAULT 0;
ALTER TABLE player ADD COLUMN gold_ticks INTEGER NOT NULL DEFAULT 0;
ALTER TABLE player ADD COLUMN gold_updated_at INTEGER NOT NULL DEFAULT 0;

UPDATE player SET gold_ticks = gold * 3600, gold_updated_at = CAST(strftime('%s', 'now') AS INTEGER) * 1000;

ALTER TABLE player DROP COLUMN gold;

UPDATE town SET
    luxury_resource = (SELECT island.luxury_resource FROM island WHERE island.id = town.island_id),
    resources_updated_at = CAST(strftime('%s', 'now') AS INTEGER) * 1000;
