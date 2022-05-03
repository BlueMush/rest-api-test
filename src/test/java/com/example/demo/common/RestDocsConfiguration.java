package com.example.demo.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;

public class RestDocsConfiguration {
	@Bean
	public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
//		return new RestDocsMockMvcConfigurationCustomizer() {
//			
//			@Override
//			public void customize(MockMvcRestDocumentationConfigurer configurer) {
//				// TODO Auto-generated method stub
//				configurer.operationPreprocessors()
//						.withRequestDefaults()
//						.withResponseDefaults();
//			}
//		};
		
		return configurer -> configurer.operationPreprocessors()
						.withRequestDefaults()
						.withResponseDefaults();
	}
}
