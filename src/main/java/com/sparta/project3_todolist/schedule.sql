create table if not exists todo (
    id bigint primary key not null,
    member_id bigint not null,
    content varchar(200),
    created_at datetime,
    modified_at datetime
);

create table if not exists member (
    id bigint primary key not null,
    email varchar(30) not null,
    password varchar(30) not null,
    registered_at datetime,
    modified_at datetime
);

alter table todo add constraint todo_fk_member_code foreign key(member_id) references member(id);