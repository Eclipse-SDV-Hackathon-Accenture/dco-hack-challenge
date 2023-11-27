CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS function_data (function_id uuid DEFAULT uuid_generate_v4 () not null, name varchar(255), release_id varchar(255), primary key (function_id));
CREATE TABLE IF NOT EXISTS ecu_data (id uuid DEFAULT uuid_generate_v4 () not null, component varchar(255), component_version varchar(255), ecu varchar(255), hardware_version varchar(255), last_change varchar(255), status varchar(255), function_id uuid, primary key (id));
CREATE TABLE IF NOT EXISTS release_brands (release_release_id varchar(255) not null, brands varchar(255));
CREATE TABLE IF NOT EXISTS release_countries (release_release_id varchar(255) not null, countries varchar(255));
CREATE TABLE IF NOT EXISTS release_models (release_release_id varchar(255) not null, models varchar(255));
CREATE TABLE IF NOT EXISTS release (release_id varchar(255) not null, created_date varchar(255), is_hardware_changes_allowed boolean, meta_track varchar(255), release_date varchar(255), quality_gate_status varchar(255), primary key (release_id));
CREATE TABLE IF NOT EXISTS gate_approver (id uuid DEFAULT uuid_generate_v4 () not null, is_passed boolean, quality_gate int4, qg_approval_id varchar(255), primary key (id));
CREATE TABLE IF NOT EXISTS gate_approver_approvers (gate_approver_id uuid not null, approvers varchar(255));
CREATE TABLE IF NOT EXISTS gate_approver_track_ids (gate_approver_id uuid not null, track_ids uuid);
CREATE TABLE IF NOT EXISTS gate_approver_tracks (gate_approver_id uuid not null, tracks varchar(255));
CREATE TABLE IF NOT EXISTS release_gate_approver (release_id varchar(255) not null, primary key (release_id));

ALTER TABLE function_data ADD CONSTRAINT fk_release_id foreign key (release_id) references release;
ALTER TABLE ecu_data add constraint fk_function_id foreign key (function_id) references function_data;
ALTER TABLE release_brands ADD CONSTRAINT fk_release_release_id foreign key (release_release_id) references release;
ALTER TABLE release_countries ADD CONSTRAINT fk_release_release_id foreign key (release_release_id) references release;
ALTER TABLE release_models ADD CONSTRAINT fk_release_release_id foreign key (release_release_id) references release;
ALTER TABLE gate_approver add constraint fk_qg_approval_id foreign key (qg_approval_id) references release_gate_approver;
ALTER TABLE gate_approver_approvers add constraint fk_gate_approver_id foreign key (gate_approver_id) references gate_approver;
ALTER TABLE gate_approver_track_ids add constraint fk_gate_approver_id foreign key (gate_approver_id) references gate_approver;
ALTER TABLE gate_approver_tracks add constraint fk_gate_approver_id foreign key (gate_approver_id) references gate_approver;