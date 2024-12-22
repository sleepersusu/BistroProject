package com.example.bistro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 設置允許所有來源
        config.addAllowedOriginPattern("*");  // 使用 pattern 而不是具體的 origin
        
        // 允許所有方法
        config.addAllowedMethod("*");
        
        // 允許所有 headers
        config.addAllowedHeader("*");
        
        // 允許暴露所有 headers
        config.addExposedHeader("*");
        
        // 允許憑證
        config.setAllowCredentials(true);
        
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}










//@Configuration
//public class CorsConfig {
//
//    @Value("${front.end.host}")
//    private String fronthost;
//
//    @Bean
//    CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        // 允許的來源
//        config.setAllowedOrigins(Arrays.asList(fronthost));
//        // 允許的方法
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        // 允許的請求 header
//        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        // 是否允許 cookie 驗證
//        config.setAllowCredentials(true);
//        
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//    
//}
