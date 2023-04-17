create table if not exists user (
    id bigint(20) not null auto_increment,
    name varchar(255),
    email varchar(255) not null,
    password varchar(255),
    imageUrl varchar(255),
    emailVerified boolean default false,
    provider enum('local', 'google', 'facebook', 'github') default 'local',
    providerId varchar(255),
    primary key (id)
);