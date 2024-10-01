create table if not exists todo (
                                    id bigint primary key not null auto_increment,
                                    title varchar(30) not null,
                                    content varchar(200) not null,
                                    username varchar(30) not null,
                                    password varchar(30) not null,
                                    created_at datetime not null,
                                    modified_at datetime not null
);