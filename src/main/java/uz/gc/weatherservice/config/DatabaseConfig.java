package uz.gc.weatherservice.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.lang.NonNull;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Configuration
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    @Value("${database.name}")
    private String database;

    @Value("${database.host}")
    private String host;

    @Value("${database.port:5432}")
    private int port;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Bean
    @Override
    @NonNull
    public ConnectionFactory connectionFactory(){
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .username(username)
                        .host(host)
                        .port(port)
                        .password(password)
                        .database(database)
                        .build()
        );
    }


    @Bean
    public Gson gson(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        return builder.setPrettyPrinting().create();
    }

//    @Profile("test")
//    @Bean
//    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
//
//        var populator = new CompositeDatabasePopulator();
//        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("/db/mock/insert_mock_users.sql")));
//
//        var initializer = new ConnectionFactoryInitializer();
//        initializer.setConnectionFactory(connectionFactory);
//        initializer.setDatabasePopulator(populator);
//
//        return initializer;
//    }

    /**
     * Bean for define datasource while executing SQL scripts during Integration test
     * @return Datasource
     */
    @Profile("test")
    @Bean
    public DataSource postgres(){
        DriverManagerDataSource dmds = new DriverManagerDataSource();
        dmds.setDriverClassName("org.postgresql.Driver");
        dmds.setUsername(username);
        dmds.setPassword(password);
        dmds.setUrl(String.format("jdbc:postgresql://%s:%d/%s", host, port, database));
        return dmds;
    }
}
