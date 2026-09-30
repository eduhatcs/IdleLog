-- ================================================================
-- DADOS DE EXEMPLO - MVP Monitoramento de Tempo Parado
-- Baseado no modelo-fisico-dados.sql enviado.
--
-- ATENÇÃO:
-- Este script limpa os dados atuais das tabelas e recria um cenário
-- de demonstração. Use em ambiente de desenvolvimento/teste.
-- ================================================================

TRUNCATE TABLE ponto, roteiro, pedido, gerente, motorista, cliente, parametrizacao RESTART IDENTITY CASCADE;

INSERT INTO parametrizacao (preco_combustivel, km_por_litro_padrao, jornada_maxima_horas, custo_hora_parada)
VALUES (5.89, 12.00, 8.00, 25.00);

-- ================================================================
-- 1. CLIENTE (5 exemplos)
-- ================================================================

INSERT INTO cliente (nome, telefone, email) VALUES
('Supermercado Boa Compra', '(31) 3333-1001', 'contato@boacompra.com.br'),
('Farmácia Vida Mais', '(31) 3333-1002', 'contato@vidamais.com.br'),
('Restaurante Sabor da Praça', '(31) 3333-1003', 'contato@sabordapraca.com.br'),
('Tech Minas Informática', '(31) 3333-1004', 'compras@techminas.com.br'),
('Clínica Saúde Integral', '(31) 3333-1005', 'recepcao@saudeintegral.com.br');

-- ================================================================
-- 2. MOTORISTA (5 exemplos)
-- ================================================================

INSERT INTO motorista (nome, telefone, documento, veiculo, rendimento_km_litro) VALUES
('Carlos Henrique Mendes', '(31) 98888-1001', 'MG-10.234.567', 'Honda CG 160', 38.0),
('Ana Paula Ferreira', '(31) 98888-1002', 'MG-20.345.678', 'Yamaha Factor 150', 40.0),
('Bruno Alves Costa', '(31) 98888-1003', 'MG-30.456.789', 'Fiat Fiorino', 12.5),
('Diego Martins Souza', '(31) 98888-1004', 'MG-40.567.890', 'Renault Kangoo', 11.8),
('Fernanda Oliveira Lima', '(31) 98888-1005', 'MG-50.678.901', 'Chevrolet Onix', 14.2);

-- ================================================================
-- 3. GERENTE (5 exemplos)
-- ================================================================

INSERT INTO gerente (nome, telefone, email) VALUES
('Marcos Antônio Silva', '(31) 97777-2001', 'marcos.silva@transportadora.com.br'),
('Juliana Costa Ribeiro', '(31) 97777-2002', 'juliana.ribeiro@transportadora.com.br'),
('Rafael Gomes Pereira', '(31) 97777-2003', 'rafael.pereira@transportadora.com.br'),
('Camila Rodrigues Alves', '(31) 97777-2004', 'camila.alves@transportadora.com.br'),
('Gustavo Mendes Rocha', '(31) 97777-2005', 'gustavo.rocha@transportadora.com.br');

-- ================================================================
-- 4. PEDIDO (5 exemplos)
-- ================================================================

INSERT INTO pedido (data, endereco_entrega, status, cliente_id) VALUES
('2026-09-28', 'Av. Afonso Pena, 1500 - Centro, Belo Horizonte/MG', 'PENDENTE', 1),
('2026-09-28', 'Rua dos Timbiras, 2400 - Lourdes, Belo Horizonte/MG', 'EM_ROTA', 2),
('2026-09-29', 'Rua Curitiba, 900 - Centro, Belo Horizonte/MG', 'PENDENTE', 3),
('2026-09-30', 'Av. do Contorno, 6500 - Santo Agostinho, Belo Horizonte/MG', 'ENTREGUE', 4),
('2026-10-01', 'Rua Pernambuco, 1200 - Savassi, Belo Horizonte/MG', 'PENDENTE', 5);

-- ================================================================
-- 5. ROTEIRO (5 exemplos)
-- RN05: um motorista não possui mais de um roteiro na mesma data.
-- ================================================================

INSERT INTO roteiro (data, motorista_id, distancia_total_km, tempo_total_parado_minutos) VALUES
('2026-09-28', 1, 18.5, 25),
('2026-09-29', 2, 24.8, 25),
('2026-09-30', 3, 42.3, 30),
('2026-10-01', 4, 36.7, 30),
('2026-10-02', 5, 21.4, 25);

-- ================================================================
-- 6. PONTO (15 exemplos)
-- Cada roteiro possui 3 pontos:
--   ordem 1 = partida -> não conta tempo parado (RN01)
--   ordem 2 e 3 = pontos de parada -> tempo = saída - chegada (RN02)
-- O total do roteiro corresponde à soma dos pontos 2 e 3 (RN03).
-- ================================================================

-- ROTEIRO 1 - Carlos
INSERT INTO ponto (
    endereco, latitude, longitude, ordem,
    data_hora_chegada, data_hora_saida, tempo_parado_minutos,
    roteiro_id, pedido_id
) VALUES
('Av. Amazonas, 100 - Centro, Belo Horizonte/MG', -19.922700, -43.945200, 1,
 '2026-09-28 07:30:00', NULL, NULL, 1, NULL),
('Av. Afonso Pena, 1500 - Centro, Belo Horizonte/MG', -19.924700, -43.935500, 2,
 '2026-09-28 08:05:00', '2026-09-28 08:20:00', 15, 1, 1),
('Rua dos Timbiras, 2400 - Lourdes, Belo Horizonte/MG', -19.929000, -43.940800, 3,
 '2026-09-28 08:35:00', '2026-09-28 08:45:00', 10, 1, 2);

-- ROTEIRO 2 - Ana
INSERT INTO ponto (
    endereco, latitude, longitude, ordem,
    data_hora_chegada, data_hora_saida, tempo_parado_minutos,
    roteiro_id, pedido_id
) VALUES
('Rua da Bahia, 500 - Centro, Belo Horizonte/MG', -19.920000, -43.938000, 1,
 '2026-09-29 07:45:00', NULL, NULL, 2, NULL),
('Rua Curitiba, 900 - Centro, Belo Horizonte/MG', -19.919400, -43.937100, 2,
 '2026-09-29 08:20:00', '2026-09-29 08:40:00', 20, 2, 3),
('Av. Brasil, 1200 - Funcionários, Belo Horizonte/MG', -19.934000, -43.933500, 3,
 '2026-09-29 08:55:00', '2026-09-29 09:00:00', 5, 2, NULL);

-- ROTEIRO 3 - Bruno
INSERT INTO ponto (
    endereco, latitude, longitude, ordem,
    data_hora_chegada, data_hora_saida, tempo_parado_minutos,
    roteiro_id, pedido_id
) VALUES
('Av. do Contorno, 3000 - Santa Efigênia, Belo Horizonte/MG', -19.924500, -43.923500, 1,
 '2026-09-30 07:20:00', NULL, NULL, 3, NULL),
('Av. do Contorno, 6500 - Santo Agostinho, Belo Horizonte/MG', -19.932200, -43.955500, 2,
 '2026-09-30 08:10:00', '2026-09-30 08:22:00', 12, 3, 4),
('Rua Professor Morais, 800 - Funcionários, Belo Horizonte/MG', -19.941000, -43.934500, 3,
 '2026-09-30 08:45:00', '2026-09-30 09:03:00', 18, 3, NULL);

-- ROTEIRO 4 - Diego
INSERT INTO ponto (
    endereco, latitude, longitude, ordem,
    data_hora_chegada, data_hora_saida, tempo_parado_minutos,
    roteiro_id, pedido_id
) VALUES
('Av. Cristiano Machado, 1000 - Cidade Nova, Belo Horizonte/MG', -19.882500, -43.929000, 1,
 '2026-10-01 07:15:00', NULL, NULL, 4, NULL),
('Rua Pernambuco, 1200 - Savassi, Belo Horizonte/MG', -19.935000, -43.929500, 2,
 '2026-10-01 08:00:00', '2026-10-01 08:08:00', 8, 4, 5),
('Av. Getúlio Vargas, 1500 - Funcionários, Belo Horizonte/MG', -19.935800, -43.927200, 3,
 '2026-10-01 08:25:00', '2026-10-01 08:47:00', 22, 4, NULL);

-- ROTEIRO 5 - Fernanda
INSERT INTO ponto (
    endereco, latitude, longitude, ordem,
    data_hora_chegada, data_hora_saida, tempo_parado_minutos,
    roteiro_id, pedido_id
) VALUES
('Av. Raja Gabaglia, 2000 - Estoril, Belo Horizonte/MG', -19.976000, -43.958000, 1,
 '2026-10-02 07:30:00', NULL, NULL, 5, NULL),
('Av. Nossa Senhora do Carmo, 1200 - Sion, Belo Horizonte/MG', -19.954000, -43.940000, 2,
 '2026-10-02 08:10:00', '2026-10-02 08:20:00', 10, 5, NULL),
('Rua Grão Mogol, 1000 - Carmo, Belo Horizonte/MG', -19.952500, -43.935500, 3,
 '2026-10-02 08:40:00', '2026-10-02 08:55:00', 15, 5, NULL);

-- ================================================================
-- CONSULTAS PARA CONFERIR OS DADOS
-- ================================================================

SELECT * FROM cliente ORDER BY id;
SELECT * FROM motorista ORDER BY id;
SELECT * FROM gerente ORDER BY id;
SELECT * FROM pedido ORDER BY id;
SELECT * FROM roteiro ORDER BY id;
SELECT * FROM ponto ORDER BY roteiro_id, ordem;

-- Exemplo do tempo total por roteiro calculado a partir dos pontos.
SELECT
    r.id AS roteiro_id,
    r.data,
    m.nome AS motorista,
    SUM(CASE WHEN p.ordem > 1 THEN COALESCE(p.tempo_parado_minutos, 0) ELSE 0 END) AS total_calculado_minutos,
    r.tempo_total_parado_minutos AS total_registrado_minutos
FROM roteiro r
JOIN motorista m ON m.id = r.motorista_id
JOIN ponto p ON p.roteiro_id = r.id
GROUP BY r.id, r.data, m.nome, r.tempo_total_parado_minutos
ORDER BY r.id;
