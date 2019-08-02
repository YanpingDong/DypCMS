package com.dyp;

import com.dyp.swagger.EnableApiDoc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan({"com.dyp.modules.sys.dao"})
@EnableApiDoc
public class CMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(CMSApplication.class, args);
	}

}
