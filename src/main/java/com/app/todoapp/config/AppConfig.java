package com.app.todoapp.config;

import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	HeadersInterceptors headersInterceptors;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/users/**").addResourceLocations("file:SeekerApp/resources/images/users/");
		registry.addResourceHandler("/images/documents/**")
				.addResourceLocations("file:SeekerApp/resources/images/documents/");
		registry.addResourceHandler("/images/chatImage/**")
				.addResourceLocations("file:SeekerApp/resources/images/chatImage/");

	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper= new ModelMapper();
//		mapper.setVisibility(
//				((Object) mapper)
////				.getSerializationConfig()
//				.getDefaultVisibilityChecker()
////				withFieldVisibility(JsonAutoDetect.Visibility.ANY)
//                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
//                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
//                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		return mapper;
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		 registry.addInterceptor(headersInterceptors);
	}
	
	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com");
		javaMailSender.setPort(587);
		javaMailSender.setPassword("holtediqjrtthrfu");
		javaMailSender.setUsername("rameshkumar13111@gmail.com");
		javaMailSender.setJavaMailProperties(getMailProperties());
		return javaMailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "25");
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.debug", "false");
		return properties;
	}
}
