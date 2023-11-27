CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS track (id uuid DEFAULT uuid_generate_v4 () not null, created_at timestamp, description varchar(255), duration varchar(255), name varchar(255), state varchar(255), track_type varchar(255), primary key (id));
CREATE TABLE IF NOT EXISTS vehicle (id uuid DEFAULT uuid_generate_v4 () not null, country varchar(255), vin varchar(255), track_id uuid, primary key (id));
ALTER TABLE vehicle add constraint fk_track_id foreign key (track_id) references track;
