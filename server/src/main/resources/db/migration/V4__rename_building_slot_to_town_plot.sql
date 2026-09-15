alter table building_slot rename to town_plot;
alter table town_plot rename constraint uk_building_slot_town_id_position to uk_town_plot_town_id_position;
