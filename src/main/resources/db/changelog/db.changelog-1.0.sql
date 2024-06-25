--liquibase formatted sql

--changeset averovko:1
create table public.producers (
    id          uuid not null constraint "producersPK" primary key,
    name        varchar(255),
    created_at  timestamp not null
);
--rollback DROP TABLE public.producers;

--changeset averovko:2
create table public.marketplaces (
    id          varchar(255) not null constraint "marketplacesPK" primary key,
    description varchar(255)
);
--rollback DROP TABLE public.marketplaces;

--changeset averovko:3
create table public.seller_infos (
    id uuid not null constraint "seller_infosPK" primary key,
    marketplace_id varchar(255) constraint "FKr8ekbqgwa3g0uhgbaa1tchf09" references public.marketplaces,
    name           varchar(2048) not null,
    url            varchar(2048),
    country        varchar(255),
    external_id    varchar(255),
    constraint "UK12xaxk0c1mwxr3ovycs1qxtbk" unique (marketplace_id, external_id)
);
--rollback DROP TABLE public.seller_infos;

--changeset averovko:4
create table public.sellers (
    id               uuid not null constraint "marketplace_sellersPK" primary key,
    producer_id      uuid not null constraint "FK6y70nxr3lhubusfq6ub427ien" references public.producers,
    seller_info_id   uuid constraint "FKp2fkfcqcndx9x9xkhk5va3cq4" references public.seller_infos,
    state            varchar(255) default 'REGULAR'::character varying not null
);
--rollback DROP TABLE public.sellers;
