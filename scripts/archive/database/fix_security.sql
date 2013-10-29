begin  work ;

alter table role rename column name to authority ;
alter table sec_user rename column email to username ;

alter table sec_user add column account_expired bool not null default  false ;
alter table sec_user add column account_locked bool not null default false ;
alter table sec_user add column enabled bool not null default true ;
alter table sec_user add column password_expired bool not null default false ;

update role set authority = 'ROLE_ADMIN' where authority = 'Admin';
update role set authority = 'ROLE_VERIFIED' where authority = 'VerifiedUser';
update role set authority = 'ROLE_UNVERIFIED' where authority = 'UnverifiedUser';

delete from data_set ;
delete from data_format ;
alter table data_set drop column uri ;
alter table data_format drop column uri ;

alter table data_file drop column download ;
alter table data_file add column download varchar null ;

commit work ;
