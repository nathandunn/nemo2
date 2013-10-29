
begin work ;

drop table definitions ;

alter table conditions rename TO condition ;
alter table condition add column version int8 default 1  ;

alter table data_files rename TO data_file ;
alter table data_file add column version int8 default 1  ;
alter table data_file add column download varchar ;
alter table data_file add column xml_content text ;
alter table data_file add column inferred_sub_classes text ;
alter table data_file add column instances text ;


alter table db_onto_maps rename to db_onto_map ;
alter table db_onto_map add column version int8 default 1  ;
alter table drop_down_parent_classes rename to drop_down_parent_class ;
alter table drop_down_parent_class add column version int8 default 1  ;
alter table eeg_data_collections rename to eeg_data_collection ;
alter table eeg_data_collection add column version int8 default 1  ;
alter table erp_data_preprocessings rename to erp_data_preprocessing ;
alter table erp_data_preprocessing add column version int8 default 1  ;

alter table experiments rename to experiment ;
alter table experiment add column version int8 default 1  ;

alter table experiment add column experimental_paradigm_label varchar ;


alter table field_annotations rename to field_annotation  ;
alter table field_annotation add column version int8 default 1  ;
alter table laboratories rename to laboratory ;
alter table laboratory add column version int8 default 1  ;
alter table ontology_terms rename to ontology_term ;
alter table ontology_term add column version int8 default 1  ;
alter table publications rename to publication ;
alter table publication add column version int8 default 1  ;
alter table responses rename to response ;
alter table response add column version int8 default 1  ;
alter table roles rename to role ;
alter table role add column version int8 default 1  ;
-- alter table roles_users rename to role_user ;
-- alter table schema_migrations rename to role_user ;
alter table slugs rename to slug ;
alter table slug add column version int8 default 1  ;
alter table stimuli rename to stimulus ;
alter table stimulus add column version int8 default 1  ;
alter table subject_groups rename to subject_group ;
alter table subject_group add column version int8 default 1  ;
alter table users rename to sec_user ;
alter table sec_user add column version int8 default 1  ;


alter table only response alter condition_id  drop not null;
alter table only stimulus alter condition_id  drop not null;

update response 
set condition_id = null 
where condition_id in (2,3,8);

update stimulus 
set condition_id = null 
where condition_id in (2,3,8);

-- do earlier than later 
alter table response add constraint response_condition_id_fkey foreign key (condition_id) references condition(id) ;

update data_file
set processing = 3
where (processing is null or processing=0)
and inferred_ontology is null
;

update data_file
set processing = 0
where processing is null
and inferred_ontology is not null
;


--CREATE TABLE inferred_sub_class(
--    id integer NOT NULL primary key ,
--    version int8 default 1,
--    data_file_id integer NOT NULL,
--    super_class_url varchar NOT NULL,
--    sub_class_url varchar NOT NULL,
--    created_at timestamp without time zone,
--    updated_at timestamp without time zone
--);
--CREATE SEQUENCE inferred_sub_class_id_seq
--    START WITH 1
--    INCREMENT BY 1
--    NO MAXVALUE
--    NO MINVALUE
--    CACHE 1;
--ALTER SEQUENCE inferred_sub_class_id_seq OWNED BY inferred_sub_class.id;
--SELECT pg_catalog.setval('inferred_sub_class_id_seq', 1, true);
--ALTER TABLE inferred_sub_class ALTER COLUMN id SET DEFAULT nextval('inferred_sub_class_id_seq'::regclass);

--alter table inferred_sub_class add constraint data_file_inferred_sub_class_id_fkey foreign key (data_file_id) references data_file(id) ;

--CREATE TABLE inferred_instance(
--    id integer NOT NULL primary key ,
--    version int8 default 1,
--    inferred_sub_class_id integer NOT NULL,
--    url varchar NOT NULL,
--    created_at timestamp without time zone,
--    updated_at timestamp without time zone
--);

--CREATE SEQUENCE inferred_instance_id_seq
--    START WITH 1
--    INCREMENT BY 1
--    NO MAXVALUE
--    NO MINVALUE
--    CACHE 1;
--ALTER SEQUENCE inferred_instance_id_seq OWNED BY inferred_instance.id;
--SELECT pg_catalog.setval('inferred_instance_id_seq', 1, true);
--ALTER TABLE inferred_instance ALTER COLUMN id SET DEFAULT nextval('inferred_instance_id_seq'::regclass);

--alter table inferred_instance add constraint inferred_sub_class_inferred_instance_id_fkey foreign key (inferred_sub_class_id) references inferred_sub_class(id) ;

-- rollback work ;
commit work ;

