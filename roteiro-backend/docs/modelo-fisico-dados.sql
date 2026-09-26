-- =====================================================================
-- Modelo Físico de Dados — MVP Monitoramento de Tempo Parado em Roteiros
-- Derivado do diagrama de classes conceitual (Motorista, Gerente,
-- Roteiro, Ponto) descrito na especificação de requisitos.
--
-- Este script é DOCUMENTAÇÃO do modelo físico para o entregável da
-- disciplina. Em tempo de execução, o Hibernate já cria/atualiza este
-- mesmo esquema automaticamente (spring.jpa.hibernate.ddl-auto=update),
-- então rodar este script manualmente não é obrigatório para o backend
-- funcionar — mas pode ser usado para inspecionar ou recriar o banco
-- do zero em outro ambiente.
-- =====================================================================

CREATE TABLE motorista (
    id                     BIGSERIAL PRIMARY KEY,
    nome                   VARCHAR(120) NOT NULL,
    telefone               VARCHAR(20)  NOT NULL,
    documento              VARCHAR(30)  NOT NULL,
    veiculo                VARCHAR(60)  NOT NULL,
    rendimento_km_litro    DOUBLE PRECISION NOT NULL,
    CONSTRAINT uk_motorista_documento UNIQUE (documento)
);

CREATE TABLE gerente (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(120) NOT NULL,
    telefone    VARCHAR(20)  NOT NULL,
    email       VARCHAR(120) NOT NULL,
    CONSTRAINT uk_gerente_email UNIQUE (email)
);

-- RN05: cada roteiro pertence a um único motorista e a uma única data
-- (constraint uk_roteiro_motorista_data garante 1 roteiro por motorista/dia).
CREATE TABLE roteiro (
    id                              BIGSERIAL PRIMARY KEY,
    data                            DATE NOT NULL,
    motorista_id                    BIGINT NOT NULL REFERENCES motorista (id),
    distancia_total_km              DOUBLE PRECISION,
    tempo_total_parado_minutos      BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_roteiro_motorista_data UNIQUE (motorista_id, data)
);

-- RN06: os pontos de um roteiro possuem ordem sequencial (1, 2, 3, 4...)
-- (constraint uk_ponto_roteiro_ordem garante que não há ordem repetida no mesmo roteiro).
-- RN01/RN02: tempo_parado_minutos é calculado pela aplicação (nulo para ordem = 1).
CREATE TABLE ponto (
    id                      BIGSERIAL PRIMARY KEY,
    endereco                VARCHAR(200) NOT NULL,
    latitude                DOUBLE PRECISION,
    longitude               DOUBLE PRECISION,
    ordem                   INTEGER NOT NULL,
    data_hora_chegada       TIMESTAMP NOT NULL,
    data_hora_saida         TIMESTAMP,
    tempo_parado_minutos    BIGINT,
    roteiro_id              BIGINT NOT NULL REFERENCES roteiro (id) ON DELETE CASCADE,
    CONSTRAINT uk_ponto_roteiro_ordem UNIQUE (roteiro_id, ordem)
);

CREATE INDEX ix_roteiro_motorista ON roteiro (motorista_id);
CREATE INDEX ix_ponto_roteiro ON ponto (roteiro_id);
