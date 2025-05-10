package com.ldap.ldap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LdapApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(LdapApplication.class, args);

//		 Test LDAP connection
		context.getBean(com.ldap.ldap.service.LdapService.class).testConnection();

		System.out.println("Application Started Successfully 🚀");
	}

}
