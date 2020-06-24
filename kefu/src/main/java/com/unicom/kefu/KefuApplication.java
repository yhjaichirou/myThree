package com.unicom.kefu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@EnableScheduling
//@EnableCaching
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class KefuApplication {

	public static void main(String[] args) {
		SpringApplication.run(KefuApplication.class, args);
	}

}
