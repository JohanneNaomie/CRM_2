package site.easy.to.build.crm;
// // Johanne debut Modif
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class CrmApplication {

// 	public static void main(String[] args) {
// 		SpringApplication.run(CrmApplication.class, args);
// 	}

// }
// //Johanne fin modif

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CrmApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CrmApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(CrmApplication.class, args);
	}
}
