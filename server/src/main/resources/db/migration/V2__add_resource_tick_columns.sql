alter table town add column wood_ticks bigint not null default 1800000;

alter table town add column luxury_ticks bigint not null default 360000;

alter table player add column gold_ticks bigint not null default 1800000;
