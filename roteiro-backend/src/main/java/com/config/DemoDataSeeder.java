package com.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

/**
 * Popula o banco com o cenário de demonstração (dados-demo.sql) apenas quando
 * ainda não existe nenhum cadastro.
 *
 * O script contém TRUNCATE, por isso não pode ser executado pelo
 * spring.sql.init a cada inicialização: em produção (Render) isso apagaria
 * todos os dados criados pelos usuários sempre que o serviço reiniciasse.
 */
@Component
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private final DataSource dataSource;
    private final boolean seedDemoData;

    public DemoDataSeeder(DataSource dataSource,
                          @Value("${app.seed-demo-data:true}") boolean seedDemoData) {
        this.dataSource = dataSource;
        this.seedDemoData = seedDemoData;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!seedDemoData) {
            log.info("Carga de dados de demonstração desativada (app.seed-demo-data=false).");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            if (possuiDados(connection)) {
                log.info("Banco já possui dados; dados de demonstração não serão inseridos.");
                return;
            }

            log.info("Banco vazio: inserindo dados de demonstração de dados-demo.sql...");
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("dados-demo.sql"));
            log.info("Dados de demonstração inseridos com sucesso.");
        }
    }

    private boolean possuiDados(Connection connection) throws SQLException {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM cliente) + "
                + "(SELECT COUNT(*) FROM motorista) + "
                + "(SELECT COUNT(*) FROM gerente) + "
                + "(SELECT COUNT(*) FROM pedido) + "
                + "(SELECT COUNT(*) FROM roteiro)";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() && resultSet.getLong(1) > 0;
        }
    }
}
