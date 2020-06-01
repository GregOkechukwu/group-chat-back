package com.groupchatback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import javax.persistence.*;

@ServletComponentScan
@SpringBootApplication
public class GroupChatApplication {

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(GroupChatApplication.class, args);
	}

}
