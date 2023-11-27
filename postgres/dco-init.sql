/* scenario-library-service */
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS scenario (id uuid DEFAULT uuid_generate_v4 () not null, created_at timestamp, created_by varchar(255), description varchar(255), updated_at timestamp, updated_by varchar(255), name varchar(255), status varchar(255), type varchar(255), file_id uuid, primary key (id));
CREATE TABLE IF NOT EXISTS file (id uuid DEFAULT uuid_generate_v4 () not null, checksum varchar(255), file_key varchar(255), path varchar(255), size varchar(255), updated_by varchar(255), updated_on timestamp, primary key (id));
ALTER TABLE scenario ADD CONSTRAINT fk_track_id FOREIGN KEY (file_id) REFERENCES file;

CREATE TABLE IF NOT EXISTS simulation (id uuid DEFAULT uuid_generate_v4 () not null, campaign_id uuid, created_at timestamp, description varchar(255), environment varchar(255), hardware varchar(255), platform varchar(255), scenario_type int4, created_by varchar(255), start_date timestamp, status varchar(255), name varchar(255), primary key (id));
CREATE TABLE IF NOT EXISTS simulation_scenarios (simulation_id uuid DEFAULT uuid_generate_v4 () not null, scenarios uuid);
CREATE TABLE IF NOT EXISTS simulation_tracks (simulation_id uuid DEFAULT uuid_generate_v4 () not null, tracks uuid);
ALTER TABLE simulation_scenarios ADD CONSTRAINT fk_simulation_id FOREIGN KEY (simulation_id) REFERENCES simulation;
ALTER TABLE simulation_tracks ADD CONSTRAINT fk_simulation_id FOREIGN KEY (simulation_id) REFERENCES simulation;

/* tracks-management-service */
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS track (id uuid DEFAULT uuid_generate_v4 () not null, created_at timestamp, description varchar(255), duration varchar(255), name varchar(255), state varchar(255), track_type varchar(255), primary key (id));
CREATE TABLE IF NOT EXISTS vehicle (id uuid DEFAULT uuid_generate_v4 () not null, country varchar(255), vin varchar(255), track_id uuid, primary key (id));
ALTER TABLE vehicle add constraint fk_track_id foreign key (track_id) references track;