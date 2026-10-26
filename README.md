# IdleLog

Projeto desenvolvido para a disciplina de **Engenharia de Software II — PUC Minas. Ministrado pelo Professor Sandro Laudares**, com base na especificação do MVP de monitoramento de tempo parado em roteiros.

## Sobre o projeto

O sistema IdleLog tem como objetivo registrar os roteiros realizados por motoristas/motoboys e identificar quanto tempo o profissional permanece parado em cada ponto do trajeto.

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
└── roteiro-backend/          # Backend REST em Java/Spring Boot, Spring Data
    ├── src/
    ├── docs/
    ├── pom.xml
    ├── docker-compose.yml
    └── README.md             # Requisitos e como executar o backend
└── roteiro-frontend/         # Frontend em React 18, Vite, React Router, Recharts
    └── src/
    ├── .gitignore
    ├── README.md             # Requisitos e como executar o frontend
    ├── index.html
    ├── package-lock.json
    ├── package-json
    └── vite.config.js
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

## Frontend

O frontend está localizado em [`roteiro-frontend/`](./roteiro-frontend/).

Interface web responsiva do MVP de monitoramento de tempo parado em roteiros. 

### Frontend publicado (GitHub Pages)

O frontend é publicado automaticamente em **https://eduhatcs.github.io/IdleLog/** pelo workflow
[`.github/workflows/deploy-pages.yml`](./.github/workflows/deploy-pages.yml) a cada push na branch `main`.
Ao acessar essa URL, a aplicação React abre diretamente na tela de login (a primeira tela de interação com o usuário).

Configuração necessária uma única vez no GitHub:

1. Acesse **Settings > Pages**.
2. Em **Build and deployment > Source**, selecione **GitHub Actions**.

O workflow executa `npm ci` e `npm run build` em `roteiro-frontend/`, publica apenas o conteúdo de `dist/`
e gera um `404.html` para que rotas como `/IdleLog/roteiros` também abram a aplicação (fallback de SPA).

> **Importante:** o GitHub Pages hospeda somente arquivos estáticos. O backend Spring Boot e o PostgreSQL
> precisam ser publicados separadamente. Para conectar o site a esse backend, cadastre a variável
> `VITE_API_URL` em **Settings > Secrets and variables > Actions > Variables** com a URL pública da API
> (ex.: `https://idlelog-api.onrender.com`). Sem isso, a tela de login abre, mas não consegue carregar
> gerentes/motoristas. O backend também precisa liberar CORS para o domínio do GitHub Pages.

### Backend publicado (Render)

[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy?repo=https://github.com/eduhatcs/IdleLog)

O arquivo [`render.yaml`](./render.yaml) na raiz define, via **Blueprint**, a API Spring Boot
(`idlelog-api`, em Docker) e o PostgreSQL (`idlelog-db`) no Render. O botão acima (ou
**https://dashboard.render.com/blueprints** → **New Blueprint Instance**) cria os dois recursos:

1. Conecte o repositório `eduhatcs/IdleLog` e confirme os recursos `idlelog-api` e `idlelog-db`.
2. Aguarde o primeiro deploy. A API ficará em **https://idlelog-api.onrender.com**
   (se o nome do serviço mudar, atualize a variável `VITE_API_URL` no GitHub e rode o workflow novamente).

O `roteiro-backend/Dockerfile` compila o projeto com Maven e executa o JAR em Java 17. O banco é
populado com os dados de demonstração **apenas na primeira inicialização**, quando está vazio
(depois disso, os dados criados pelos usuários são preservados).

> O plano gratuito do Render coloca o serviço em suspensão após 15 minutos sem acesso (a primeira
> requisição pode levar ~1 minuto) e o PostgreSQL gratuito expira **30 dias após a criação**. Para uma
> demonstração rápida isso é suficiente; para manter o site no ar por mais tempo, use um PostgreSQL
> gratuito externo (Neon, Supabase) ou mude o banco para um plano pago.


### Tecnologias
- React 18
- Vite
- React Router
- Recharts

### Organização do frontend

```text
roteiro-frontend/
└── src
    ├── components/       
    ├── pages/              
    ├── App.jsx            
    ├── api.js       
    ├── auth.jsx          
    ├── hooks.js
    ├── main.jsx
    ├── styles.css
    └── util.js         
```

## Escopo do MVP

A implementação está sendo construída por etapas. O backend atual já possui a base de cadastros e montagem/registro de roteiro, enquanto os demais módulos previstos no MVP podem ser adicionados sobre essa estrutura.

Entre as funcionalidades previstas no produto completo estão os cadastros, montagem de roteiro, coleta de chegada/saída, cálculo de tempo parado, histórico, dashboard, parametrização de custos e jornada, cálculo do custo estimado e exportação de relatórios.

## Documentação

- [Backend — requisitos e execução](./roteiro-backend/README.md)
- [Modelo físico do banco](./roteiro-backend/docs/modelo-fisico-dados.sql)
- [Frontend](./roteiro-frontend/README.md)

## Relação com a especificação

O projeto segue a especificação de requisitos do trabalho e utiliza o backend como base para a implementação do MVP. A persistência e o histórico dos roteiros fazem parte do escopo do sistema, assim como os cálculos relacionados ao tempo parado e aos parâmetros definidos para o produto.

## Equipe
[Diego Henrique Xavier dos Santos](https://github.com/Diego-Hxs)

[Eduardo Henrique Aniceto Teixeira](https://github.com/eduhatcs)

[Marcos Vinicíus Nunes Reis](https://github.com/marcosware)

Projeto acadêmico — Engenharia de Software II — PUC Minas.
