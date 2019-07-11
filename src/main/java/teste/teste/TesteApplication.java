package teste.teste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class TesteApplication {

    static { TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo")); }

	public static void main(String[] args) { SpringApplication.run(TesteApplication.class, args);
	}


}
