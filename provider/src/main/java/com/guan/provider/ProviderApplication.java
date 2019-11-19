package com.guan.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

//@EnableDubbo	//开启基于注解的dubbo功能
//@ImportResource(locations = "classpath:spring/provider.xml")	//扫码dubbo配置
@EnableDubbo(scanBasePackages = "com.guan")
@SpringBootApplication
public class ProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}

}
