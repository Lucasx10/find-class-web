create table aulas(

    id bigint not null auto_increment,
    professor_id bigint not null,
    aluno_id bigint not null,
    data datetime not null,

    primary key(id),
    constraint fk_aulas_professor_id foreign key(professor_id) references professores(id),
    constraint fk_aulas_aluno_id foreign key(aluno_id) references alunos(id)

);