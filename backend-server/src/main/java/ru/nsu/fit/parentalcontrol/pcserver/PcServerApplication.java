package ru.nsu.fit.parentalcontrol.pcserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.nsu.fit.parentalcontrol.pcserver.security.SecurityConfig;

@SpringBootApplication
public class PcServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PcServerApplication.class, args);
	}
}
