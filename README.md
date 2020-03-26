# covid-delivery-service
This was created as a backend to an app to order things from various shops and a timeslot will be assigned,during which the user can go and collect

DB
===================================================================================
Database type : POSTGRESS

create table users(
created_at timestamp,
updated_at timestamp,
created_by varchar(100),
updated_by varchar(100),
id serial primary key,
mobile_no varchar(20),
name varchar(100),
address varchar(255),
user_type varchar(100),
lat double precision,
lon double precision,
token varchar(255)
)

create table store(
created_at timestamp,
updated_at timestamp,
created_by varchar(100),
updated_by varchar(100),
id serial primary key,
mobile_no varchar(20),
name varchar(100),
address varchar(255),
lat double precision,
lon double precision,
image_url varchar(255),
types varchar(255),
user_id int,
operational_start_time timestamp,
operational_end_time timestamp,
foreign key (user_id) references users (id)
)


create table orders(
id serial primary key,
created_at timestamp,
updated_at timestamp,
created_by varchar(100),
updated_by varchar(100),
user_id int,
store_id int,
status varchar(100),
start_time timestamp,
end_time timestamp,
foreign key (user_id) references users (id),
foreign key (store_id) references store (id)
)


create table order_items(
created_at timestamp,
updated_at timestamp,
created_by varchar(100),
updated_by varchar(100),
id serial primary key,
order_id int,
item_name varchar(255),
quantity double precision,
unit varchar(100),
availability_status boolean,
image_url varchar(255),
foreign key (order_id) references orders (id)
)

alter table store add COLUMN slots VARCHAR(100);

==============================================================

**Postman Collection**

https://www.getpostman.com/collections/0e1e00ec150f94626a19
