alter table user_table add column role VARCHAR(255) default 'USER';

insert into user_table(username, email, password) values ('admin','admin@email.com', '$2a$10$sAGzQD/I8c36hz884rUvcuEE7dCTR7wBE3EDkQ5.eOjOp5YvFqgPG');