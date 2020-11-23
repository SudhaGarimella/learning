package com.example.demo;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.constants.ApplicationConstants;


import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	

	@Bean
	public Docket swaggerConfig() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.paths(PathSelectors.ant(ApplicationConstants.BASE_REQUEST_PATH + "/*"))
				.apis(RequestHandlerSelectors.basePackage(ApplicationConstants.FOLDER_STRUCTURE)).build()
				.apiInfo(apiDetails());

	}

	private ApiInfo apiDetails() {
		return new ApiInfo(ApplicationConstants.APP_NAME, ApplicationConstants.APP_DESC,
				ApplicationConstants.APP_VERSION, ApplicationConstants.LICENSE_INFO, null,
				ApplicationConstants.LICENSE_INFO, ApplicationConstants.URL, Collections.emptyList());
	}

}
