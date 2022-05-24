package bg.vmware.reo101.ics.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories("bg.vmware.reo101.ics.backend.repository.jpa")
public class Main {
    public static void main(String... args) {
        SpringApplication.run(Main.class, args);
    }
}
