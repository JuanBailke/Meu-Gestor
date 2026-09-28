package br.com.juanbailke.meu_gestor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeuGestorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeuGestorApplication.class, args);
	}

}
