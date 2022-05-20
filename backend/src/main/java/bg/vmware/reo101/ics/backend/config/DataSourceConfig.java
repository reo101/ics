package bg.vmware.reo101.ics.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    // @Bean
    // public DataSource getDataSource() {
    //     DriverManagerDataSource dataSource = new DriverManagerDataSource();
    //     dataSource.setDriverClassName("org.postgresql.Driver");
    //     dataSource.setUrl("jdbc:postgresql://localhost:5432/takovata");
    //     dataSource.setUsername("reo101");
    //     dataSource.setPassword("aloda");
    //     dataSource.setSchema("takovata");
    //
    //     return dataSource;
    // }
}
