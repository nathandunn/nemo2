UPDATE data_file
SET format_id=76
WHERE identifier = 'KK-V.MEM_n=04';

-- password and other stuff is also null
delete from sec_user
where full_name is null ;

set client_min_messages='warning';

CREATE TABLE "public"."experiment_publications"
(
    experiment_id bigint NOT NULL,
	   publication_id bigint NOT NULL,
	      CONSTRAINT experiment_publications_pkey PRIMARY KEY (experiment_id,publication_id)
	)
	;
	ALTER TABLE "public"."experiment_publications"
	ADD CONSTRAINT fk75ba54c9160bc23a
	FOREIGN KEY (experiment_id)
	REFERENCES "public"."experiment"(id)
	;
	ALTER TABLE "public"."experiment_publications"
	ADD CONSTRAINT fk75ba54c9ba56b89a
	FOREIGN KEY (publication_id)
	REFERENCES "public"."publication"(id)
	;
-- 	CREATE UNIQUE INDEX experiment_publications_pkey ON "public"."experiment_publications"
-- 	(
-- 	   experiment_id,
-- 	     publication_id
-- 	)
-- 	;


INSERT INTO experiment_publications (publication_id,experiment_id)
SELECT p.id,p.experiment_id
FROM publication p
;

alter table publication drop column experiment_id ;

	CREATE TABLE "public"."sec_user_publications"
	(
	    sec_user_id bigint NOT NULL,
		   publication_id bigint NOT NULL,
		      CONSTRAINT sec_user_publications_pkey PRIMARY KEY (sec_user_id,publication_id)
	)
	;
	ALTER TABLE "public"."sec_user_publications"
	ADD CONSTRAINT fk7b6c774dba56b89a
	FOREIGN KEY (publication_id)
	REFERENCES "public"."publication"(id)
	;
	ALTER TABLE "public"."sec_user_publications"
	ADD CONSTRAINT fk7b6c774d46d7b23d
	FOREIGN KEY (sec_user_id)
	REFERENCES "public"."sec_user"(id)
	;
-- 	CREATE UNIQUE INDEX sec_user_publications_pkey ON "public"."sec_user_publications"
-- 	(
-- 	   sec_user_id,
-- 	     publication_id
-- 	)
-- 	;

-- we don't want to add this automatically





	CREATE TABLE "public"."laboratory_users"
	(
	    laboratory_id bigint NOT NULL,
		   sec_user_id bigint NOT NULL,
		      CONSTRAINT laboratory_users_pkey PRIMARY KEY (laboratory_id,sec_user_id)
	)
	;
	ALTER TABLE "public"."laboratory_users"
	ADD CONSTRAINT fkd5f58e7c46d7b23d
	FOREIGN KEY (sec_user_id)
	REFERENCES "public"."sec_user"(id)
	;
	ALTER TABLE "public"."laboratory_users"
	ADD CONSTRAINT fkd5f58e7c53b19e7a
	FOREIGN KEY (laboratory_id)
	REFERENCES "public"."laboratory"(id)
	;
-- 	CREATE UNIQUE INDEX laboratory_users_pkey ON "public"."laboratory_users"
-- 	(
-- 	   laboratory_id,
-- 	     sec_user_id
-- 	)
-- 	;


INSERT INTO laboratory_users (sec_user_id,laboratory_id)
SELECT su.id,su.laboratory_id
FROM sec_user su
WHERE su.laboratory_id IS NOT NULL
;


set client_min_messages='info';
