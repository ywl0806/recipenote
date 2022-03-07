drop table if exists affiliate CASCADE;
drop table if exists amount_of_ingredient CASCADE;
drop table if exists comment CASCADE;
drop table if exists ingredient CASCADE;
drop table if exists recipe CASCADE;
drop table if exists role CASCADE;
drop table if exists store CASCADE;
drop table if exists usr CASCADE;
drop table if exists user_role CASCADE;

create table affiliate
(
    id          serial,
    created_at  timestamp,
    updated_at  timestamp,
    description varchar(255) not null,
    name        varchar(255) not null,
    primary key (id)
);


create table amount_of_ingredient
(
    id            serial,
    amount        double precision,
    ingredient_id bigint,
    recipe_id     bigint,
    primary key (id)
);


create table comment
(
    id          serial,
    created_at  timestamp,
    updated_at  timestamp,
    description varchar(255) not null,
    recipe_id   bigint       not null,
    user_id     bigint       not null,
    primary key (id)
);


create table ingredient
(
    id         serial,
    created_at timestamp,
    updated_at timestamp,
    name       varchar(255) not null,
    price      double precision,
    primary key (id)
);


create table recipe
(
    id             serial,
    created_at     timestamp,
    updated_at     timestamp,
    affiliate_id   bigint,
    content        varchar(1000) not null,
    is_public      boolean       not null,
    name           varchar(255)  not null,
    store_id       bigint,
    thumbnail_path varchar(255),
    user_id        bigint        not null,
    primary key (id)
);


create table role
(
    id   serial,
    name varchar(255) not null,
    primary key (id)
);


create table store
(
    id           serial,
    created_at   timestamp,
    updated_at   timestamp,
    affiliate_id bigint       not null,
    description  varchar(255),
    latitude     double precision,
    longitude    double precision,
    name         varchar(255) not null,
    primary key (id)
);


create table usr
(
    user_id      serial,
    created_at   timestamp,
    updated_at   timestamp,
    affiliate_id bigint,
    avatar_url   varchar(255),
    enabled      boolean      not null,
    name         varchar(255) not null,
    password     varchar(255) not null,
    username     varchar(255) not null,
    primary key (user_id)
);


create table user_role
(
    user_id serial not null,
    role_id serial not null,
    primary key (user_id, role_id)
);


alter table affiliate
    add constraint UK_k303s6m10cc5ybjcy470pkax8 unique (name);


alter table store
    add constraint UK_d0p5ly1cv6guij7sq1mbnr8ec unique (name);


alter table usr
    add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);


alter table amount_of_ingredient
    add constraint FKeoy685ej8s4cbwv84p8vp8xmp
        foreign key (ingredient_id)
            references ingredient;


alter table amount_of_ingredient
    add constraint FK26fwlyr70x0neu9besaf68lx1
        foreign key (recipe_id)
            references recipe;


alter table comment
    add constraint FKe5i1rxybcm40jcn98fj1jmvit
        foreign key (recipe_id)
            references recipe;


alter table comment
    add constraint FK8kcum44fvpupyw6f5baccx25c
        foreign key (user_id)
            references usr;


alter table recipe
    add constraint FKf6vfkhk3a391o4sdsk0w717wn
        foreign key (affiliate_id)
            references affiliate;


alter table recipe
    add constraint FK2fhnivbwc4v39wdq9lp3ryoam
        foreign key (store_id)
            references store;


alter table recipe
    add constraint FKc8o8io8s0f7nqcd3429u6cxjs
        foreign key (user_id)
            references usr;


alter table store
    add constraint FKokmptx2g80t56urw269wcllvo
        foreign key (affiliate_id)
            references affiliate;


alter table usr
    add constraint FK28qaihpqxupq5f6pjkgi683bb
        foreign key (affiliate_id)
            references affiliate;


alter table user_role
    add constraint FKa68196081fvovjhkek5m97n3y
        foreign key (role_id)
            references role;


alter table user_role
    add constraint FK859n2jvi8ivhui0rl0esws6o
        foreign key (user_id)
            references usr;