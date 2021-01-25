create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
);

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    extra_fare int default 0,
    primary key(id)
);

create table if not exists SECTION
(
    id bigint auto_increment not null,
    line_id bigint not null,
    station_id bigint not null,
    up_section_id bigint,
    down_section_id bigint,
    up_distance int default 0,
    down_distance int default 0,
    primary key(id)
);
