
create table if not exists member (
                                      id bigint primary key not null auto_increment,
                                      username varchar(30) not null,
                                      password varchar(30) not null,
                                      registered_at datetime not null,
                                      modified_at datetime not null,
                                      constraint member_fk_todo foreign key (id) references todo(member_id)
);
create table if not exists todo (
                                    id bigint primary key not null auto_increment,
                                    member_id bigint unique key not null,
                                    title varchar(30) not null,
                                    content varchar(200) not null,
                                    created_at datetime not null,
                                    modified_at datetime not null
);
