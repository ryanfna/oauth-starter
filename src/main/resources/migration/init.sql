create table if not exists users (
    id bigint(20) not null auto_increment,
    name varchar(255),
    email varchar(255) not null unique ,
    password varchar(255),
    image_url varchar(255),
    email_verified boolean default false,
    provider enum('LOCAL', 'GOOGLE', 'FACEBOOK', 'GITHUB') default 'LOCAL',
    provider_id varchar(255),
    primary key (id)
);

alter table users add role enum('USER', 'ADMIN', 'MODERATOR') default 'USER';

drop table if exists tokens;
create table tokens (
    id bigint(20) not null auto_increment,
    user_id bigint(20) not null,
    token varchar(255) not null,
    expired boolean default false,
    revoked boolean default false,
    primary key (id),
    foreign key (user_id) references users(id)
);