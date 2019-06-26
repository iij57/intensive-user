package com.sk.intensive;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {
	
	   @Bean
	    public Docket api() { 
	        return new Docket(DocumentationType.SWAGGER_2)  
	          .apiInfo(apiInfo()).select()
	          .apis(RequestHandlerSelectors.basePackage("com.sk.intensive"))              
	          .paths(PathSelectors.any())                          
	          .build();                                           
	    }
	   
	   private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("Intensive-USER API")
	                .description("Intensive-USER API를 사용해 봅시다.")
	                .build();
	 
	    }

}
