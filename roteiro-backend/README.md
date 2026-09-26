# Roteiro Backend

Backend REST do **MVP — Sistema de Monitoramento de Tempo Parado em Roteiros**, desenvolvido em Java com Spring Boot e PostgreSQL para o projeto de Engenharia de Software II — PUC Minas.

Este diretório contém a aplicação backend. O README da pasta [`MVP-Entregadores`](../README.md) apresenta a visão geral do projeto.

## Requisitos

Antes de executar o projeto, instale os seguintes programas:

### 1. Java versão 17 ou superior

O projeto utiliza **Java para versão 17 ou superior**.

Verifique a instalação:

```bash
java -version
```

O comando deve indicar uma instalação Java versão 17 ou superior.

### 2. Maven

O projeto não possui Maven Wrapper, portanto é necessário ter o Maven instalado no sistema.

Verifique:

```bash
mvn -version
```

### 3. Docker

O banco PostgreSQL é fornecido por `docker-compose.yml`.

Verifique:

```bash
docker --version
docker compose version
```

É necessário que o Docker esteja em execução.

## Tecnologias utilizadas

- **Java**
- **Spring Boot 3.3.4**
- **Spring Web**
- **Spring Data JPA / Hibernate**
- **Spring Validation**
- **PostgreSQL 16**
- **Docker Compose**
- **Maven**
- **Lombok**

## Banco de dados

O `docker-compose.yml` cria um PostgreSQL com as seguintes configurações:

```text
Banco:       roteiro_db
Usuário:     postgres
Senha:       postgres
Host:        localhost
Porta host:  5433
Porta Docker:5432
```

A aplicação já está configurada para utilizar essas mesmas informações em:

```text
src/main/resources/application.yml
```

### Subir o PostgreSQL

Dentro da pasta `roteiro-backend`:

```bash
docker compose up -d
```

Verifique se o container está em execução:

```bash
docker compose ps
```

Para acompanhar os logs do banco:

```bash
docker compose logs -f postgres
```

Para parar o banco:

```bash
docker compose down
```

Para remover também os dados persistidos pelo volume:

```bash
docker compose down -v
```

> Atenção: `docker compose down -v` apaga o volume `roteiro_pgdata` e, consequentemente, os dados salvos no banco local.

## Como executar o backend

### Passo 1 — entrar na pasta

A partir da raiz do projeto:

```bash
cd roteiro-backend
```

### Passo 2 — subir o banco

```bash
docker compose up -d
```

### Passo 3 — iniciar a aplicação

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

Para parar a aplicação, utilize `Ctrl + C` no terminal onde o Maven está executando.

## Compilar o projeto

Para apenas compilar:

```bash
mvn clean package -DskipTests
```

Depois, o artefato JAR será gerado em `target/`.

Para executar o JAR:

```bash
java -jar target/roteiro-backend-0.0.1-SNAPSHOT.jar
```

## Configuração da aplicação

A configuração principal está em:

```text
src/main/resources/application.yml
```

Atualmente:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/roteiro_db
    username: postgres
    password: postgres
```

O Hibernate está configurado com:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

Isso significa que, ao iniciar a aplicação, o Hibernate cria/atualiza as tabelas de acordo com as entidades JPA. Portanto, **não é necessário executar manualmente o arquivo SQL para iniciar o projeto**.

## Modelo físico do banco

A documentação do modelo físico está em:

```text
docs/modelo-fisico-dados.sql
```

Esse arquivo serve para documentar o esquema relacional do projeto e também pode ser utilizado para consultar a estrutura esperada do banco.

## Estrutura do código

```text
src/main/java/com/pucminas/roteiro/
│
├── controller/
│   ├── ClienteController.java
│   ├── GerenteController.java
│   ├── MotoristaController.java
│   ├── PedidoController.java
│   └── RoteiroController.java
│
├── dto/
│   ├── ClienteRequest.java
│   ├── ClienteResponse.java
│   ├── GerenteRequest.java
│   ├── GerenteResponse.java
│   ├── MotoristaRequest.java
│   ├── MotoristaResponse.java
│   ├── PedidoRequest.java
│   ├── PedidoResponse.java
│   ├── PedidoStatusRequest.java
│   ├── PontoRequest.java
│   ├── PontoResponse.java
│   ├── PontoSaidaRequest.java
│   ├── RoteiroRequest.java
│   └── RoteiroResponse.java
│
├── model/
│   ├── Cliente.java
│   ├── Gerente.java
│   ├── Motorista.java
│   ├── Pedido.java
│   ├── Ponto.java
│   ├── Roteiro.java
│   └── StatusPedido.java
│
├── repository/
│   ├── ClienteRepository.java
│   ├── GerenteRepository.java
│   ├── MotoristaRepository.java
│   ├── PedidoRepository.java
│   ├── PontoRepository.java
│   └── RoteiroRepository.java
│
├── service/
│   ├── ClienteService.java
│   ├── GerenteService.java
│   ├── MotoristaService.java
│   ├── PedidoService.java
│   └── RoteiroService.java
│
└── exception/
    ├── ApiError.java
    ├── BusinessRuleException.java
    ├── GlobalExceptionHandler.java
    └── ResourceNotFoundException.java
```

## Endpoints disponíveis

### Motoristas

Base:

```text
/api/motoristas
```

| Método | Endpoint | Função |
|---|---|---|
| POST | `/api/motoristas` | Cadastrar motorista |
| GET | `/api/motoristas` | Listar motoristas |
| GET | `/api/motoristas/{id}` | Buscar motorista |
| PUT | `/api/motoristas/{id}` | Atualizar motorista |
| DELETE | `/api/motoristas/{id}` | Excluir motorista |

Exemplo de cadastro:

```bash
curl -X POST http://localhost:8080/api/motoristas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "telefone": "31999990000",
    "documento": "12345678900",
    "veiculo": "Honda CG 160",
    "rendimentoKmLitro": 35.0
  }'
```

### Gerentes

Base:

```text
/api/gerentes
```

| Método | Endpoint | Função |
|---|---|---|
| POST | `/api/gerentes` | Cadastrar gerente |
| GET | `/api/gerentes` | Listar gerentes |
| GET | `/api/gerentes/{id}` | Buscar gerente |
| PUT | `/api/gerentes/{id}` | Atualizar gerente |
| DELETE | `/api/gerentes/{id}` | Excluir gerente |

Exemplo:

```bash
curl -X POST http://localhost:8080/api/gerentes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Souza",
    "telefone": "31988887777",
    "email": "maria@example.com"
  }'
```

### Clientes

Base:

```text
/api/clientes
```

| Método | Endpoint | Função |
|---|---|---|
| POST | `/api/clientes` | Cadastrar cliente |
| GET | `/api/clientes` | Listar clientes |
| GET | `/api/clientes/{id}` | Buscar cliente |
| PUT | `/api/clientes/{id}` | Atualizar cliente |
| DELETE | `/api/clientes/{id}` | Excluir cliente |

Exemplo:

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Ana Pereira",
    "telefone": "31977776666",
    "email": "ana@example.com"
  }'
```

### Pedidos

Base:

```text
/api/pedidos
```

Todo pedido pertence a um cliente já cadastrado (relação "realiza" do diagrama de classes)
e nasce com status `PENDENTE`.

| Método | Endpoint | Função |
|---|---|---|
| POST | `/api/pedidos` | Cadastrar pedido |
| GET | `/api/pedidos` | Listar pedidos |
| GET | `/api/pedidos/{id}` | Buscar pedido |
| PUT | `/api/pedidos/{id}` | Atualizar pedido (cliente, data, endereço) |
| PATCH | `/api/pedidos/{id}/status` | Atualizar status (`PENDENTE`, `EM_ROTA`, `ENTREGUE`, `CANCELADO`) |
| DELETE | `/api/pedidos/{id}` | Excluir pedido |

Exemplo:

```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "data": "2026-09-26",
    "enderecoEntrega": "Rua Peru, 55"
  }'
```

### Roteiros

Base:

```text
/api/roteiros
```

| Método | Endpoint | Função |
|---|---|---|
| POST | `/api/roteiros` | Criar roteiro |
| GET | `/api/roteiros` | Listar roteiros |
| GET | `/api/roteiros/{id}` | Buscar roteiro |
| POST | `/api/roteiros/{id}/pontos` | Adicionar ponto ao roteiro |
| GET | `/api/roteiros/{id}/pontos` | Listar pontos do roteiro |
| PATCH | `/api/roteiros/{id}/pontos/{pontoId}/saida` | Registrar saída do ponto |

> `POST /api/roteiros/{id}/pontos` aceita um `pedidoId` opcional, para indicar que aquele ponto
> é o endereço de entrega de um pedido já cadastrado (relação "endereço de entrega" do diagrama
> de classes). Sem `pedidoId`, o ponto fica sem pedido associado (ex.: ponto de partida).

Exemplo de criação de roteiro:

```bash
curl -X POST http://localhost:8080/api/roteiros \
  -H "Content-Type: application/json" \
  -d '{
    "motoristaId": 1,
    "data": "2026-09-26"
  }'
```

## Exemplo completo do fluxo

### 1. Cadastrar o motorista

```bash
curl -X POST http://localhost:8080/api/motoristas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "telefone": "31999990000",
    "documento": "12345678900",
    "veiculo": "Honda CG 160",
    "rendimentoKmLitro": 35.0
  }'
```

Supondo que o ID retornado seja `1`.

### 2. Criar o roteiro

```bash
curl -X POST http://localhost:8080/api/roteiros \
  -H "Content-Type: application/json" \
  -d '{
    "motoristaId": 1,
    "data": "2026-09-26"
  }'
```

Supondo que o roteiro criado tenha ID `1`.

### 3. Adicionar o ponto de partida

O ponto de partida deve ser a **ordem 1** e não contabiliza tempo parado.

```bash
curl -X POST http://localhost:8080/api/roteiros/1/pontos \
  -H "Content-Type: application/json" \
  -d '{
    "endereco": "Seg. Família",
    "ordem": 1,
    "dataHoraChegada": "2026-09-26T08:00:00"
  }'
```

### 4. Adicionar um ponto com chegada e saída

```bash
curl -X POST http://localhost:8080/api/roteiros/1/pontos \
  -H "Content-Type: application/json" \
  -d '{
    "endereco": "Rua Peru, 55",
    "ordem": 2,
    "dataHoraChegada": "2026-09-26T08:20:00",
    "dataHoraSaida": "2026-09-26T08:35:00"
  }'
```

Nesse exemplo, o tempo parado do ponto 2 é de **15 minutos**.

### 5. Consultar o roteiro

```bash
curl http://localhost:8080/api/roteiros/1
```

O retorno mostra os pontos e o tempo total parado calculado pelo backend.

### 6. Registrar saída posteriormente

Quando a saída não estiver disponível no momento do cadastro do ponto:

```bash
curl -X PATCH http://localhost:8080/api/roteiros/1/pontos/2/saida \
  -H "Content-Type: application/json" \
  -d '{
    "dataHoraSaida": "2026-09-26T08:35:00"
  }'
```

## Regras de negócio do tempo parado

### RN01 — ponto de partida

O ponto de partida é a ordem `1` e **não contabiliza tempo parado**.

### RN02 — tempo parado por ponto

Para os demais pontos:

```text
tempo parado = data/hora de saída - data/hora de chegada
```

### RN03 — tempo total do roteiro

```text
tempo total parado = soma dos tempos parados dos pontos
                        (exceto o ponto de partida)
```

### RN05 — motorista e data

Um roteiro pertence a um motorista e a uma data.

### RN06 — ordem dos pontos

Os pontos do roteiro são inseridos em ordem sequencial:

```text
1 → 2 → 3 → 4 → ...
```

## Próximos módulos do MVP

A estrutura do projeto deve receber os próximos recursos previstos na especificação, incluindo:

- parâmetros de custo;
- jornada padrão de 8 horas;
- regras parametrizáveis para o cálculo;
- cálculo do custo estimado do roteiro;
- dashboard por dia, mês e período;
- histórico/relatórios;
- controle de acesso por perfil;
- auditoria das alterações.

## Observações

- O PostgreSQL local utiliza a porta **5433** no host para não depender da porta padrão 5432.
- O banco é persistido no volume Docker `roteiro_pgdata`.
- O arquivo `docs/modelo-fisico-dados.sql` é a documentação do modelo físico; para a execução normal, o Hibernate cria/atualiza as tabelas automaticamente.
