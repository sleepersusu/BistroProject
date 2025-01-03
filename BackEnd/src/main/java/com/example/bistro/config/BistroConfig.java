package com.example.bistro.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BistroConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Bean
    public FilterRegistrationBean<LoginBackEndFilter> loggingFilter() {
        FilterRegistrationBean<LoginBackEndFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoginBackEndFilter());  // 註冊自定義的過濾器
        registrationBean.addUrlPatterns("/Bistro/*");  // 配置過濾 URL 模式
        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean<LoggingFrontEndFilter> loggingFrontEndFilter() {
        FilterRegistrationBean<LoggingFrontEndFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingFrontEndFilter());  // 註冊自定義的過濾器
        registrationBean.addUrlPatterns("/api/frontend/*");  // 配置過濾 URL 模式
        return registrationBean;
    }
}
