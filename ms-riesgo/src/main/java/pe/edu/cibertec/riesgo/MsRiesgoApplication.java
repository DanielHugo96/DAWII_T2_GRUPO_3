package pe.edu.cibertec.riesgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsRiesgoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsRiesgoApplication.class, args);
    }
}
