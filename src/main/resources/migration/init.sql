create table if not exists users (
    id bigint(20) not null auto_increment,
    name varchar(255),
    email varchar(255) not null,
    password varchar(255),
    image_url varchar(255),
    email_verified boolean default false,
    provider enum('LOCAL', 'GOOGLE', 'FACEBOOK', 'GITHUB') default 'LOCAL',
    provider_id varchar(255),
    primary key (id)
);