package wagi_app.wagi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "wagi_app")
public class WagiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WagiApplication.class, args);
	}

}
