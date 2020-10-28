create table if not exists vermont_services.sp01_pessoa
(
    id            uuid                       not null,
    nome          varchar(200)               not null,
    cpf           varchar(15)                not null,
    dt_nascimento date                       not null,
    email         varchar(100) default null,
    naturalidade  varchar(200) default null,
    nacionalidade varchar(200) default null,
    tipo_sexo     char         default null,
    dt_cadastro   timestamp    default now() not null,
    ativo         bool                       not null,
    primary key (id)
);

create unique index uq_sp01_pessoa on vermont_services.sp01_pessoa (id);

alter table vermont_services.sp01_pessoa
    owner to postgres;
