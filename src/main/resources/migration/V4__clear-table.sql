delete from public.flyway_schema_history where version = '1';
delete from public.flyway_schema_history where version = '2';
delete from public.flyway_schema_history where version = '3';

DELETE FROM coupon;
DELETE FROM address;
DELETE FROM event;
