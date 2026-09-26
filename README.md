# MVP Entregadores — Monitoramento de Tempo Parado em Roteiros

Projeto desenvolvido para a disciplina de **Engenharia de Software II — PUC Minas. Ministrado pelo Professor Sandro Laudares**, com base na especificação do MVP de monitoramento de tempo parado em roteiros.

## Sobre o projeto

O sistema tem como objetivo registrar os roteiros realizados por motoristas/motoboys e identificar quanto tempo o profissional permanece parado em cada ponto do trajeto.

O domínio do projeto contempla:

- clientes e pedidos;
- motoristas/motoboys;
- gerentes/coordenadores;
- pontos de entrega e seus horários;
- roteiros diários com pontos em ordem sequencial;
- parâmetros de jornada e custos;
- cálculo do tempo parado por ponto e do tempo total do roteiro;
- cálculo de custos do trajeto;
- histórico e indicadores/dashboard;
- relatórios.

A especificação define, entre outros pontos, que o ponto de partida não contabiliza tempo parado (RN01), que o tempo de um ponto é calculado pela diferença entre saída e chegada (RN02) e que o tempo total é a soma dos pontos, exceto a partida (RN03). Também são previstos parâmetros de jornada e custo (RN04 e RN07) e a associação ordenada de pontos a um roteiro (RN05 e RN06).

## Estrutura do repositório

```text
MVP-Entregadores/
│
├── README.md                 # Visão geral do projeto
│
└── roteiro-backend/          # Backend REST em Java/Spring Boot
    ├── src/
    ├── docs/
    ├── pom.xml
    ├── docker-compose.yml
    └── README.md             # Requisitos e como executar o backend
```

## Backend

O backend está localizado em [`roteiro-backend/`](./roteiro-backend/).

Ele é responsável pela API REST, regras de negócio, persistência em PostgreSQL e processamento dos roteiros.

### Tecnologias

- Java 17
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA / Hibernate
- Spring Validation
- PostgreSQL
- Docker / Docker Compose
- Maven
- Lombok

### Organização do backend

```text
roteiro-backend/
└── src/main/java/com/pucminas/roteiro/
    ├── controller/       # Endpoints HTTP
    ├── dto/              # Objetos de entrada e saída da API
    ├── model/            # Entidades JPA
    ├── repository/       # Persistência com Spring Data JPA
    ├── service/          # Regras de negócio
    └── exception/        # Tratamento de erros da API
```

## Escopo do MVP

A implementação está sendo construída por etapas. O backend atual já possui a base de cadastros e montagem/registro de roteiro, enquanto os demais módulos previstos no MVP podem ser adicionados sobre essa estrutura.

Entre as funcionalidades previstas no produto completo estão os cadastros, montagem de roteiro, coleta de chegada/saída, cálculo de tempo parado, histórico, dashboard, parametrização de custos e jornada, cálculo do custo estimado e exportação de relatórios.

## Documentação

- [Backend — requisitos e execução](./roteiro-backend/README.md)
- [Modelo físico do banco](./roteiro-backend/docs/modelo-fisico-dados.sql)

## Relação com a especificação

O projeto segue a especificação de requisitos do trabalho e utiliza o backend como base para a implementação do MVP. A persistência e o histórico dos roteiros fazem parte do escopo do sistema, assim como os cálculos relacionados ao tempo parado e aos parâmetros definidos para o produto.

## Equipe
[Diego Henrique Xavier dos Santos](https://github.com/Diego-Hxs)
[Eduardo Henrique Aniceto Teixeira](https://github.com/eduhatcs)
[Marcos Vinicíus Nunes Reis](https://github.com/marcosware)

Projeto acadêmico — Engenharia de Software II — PUC Minas.
