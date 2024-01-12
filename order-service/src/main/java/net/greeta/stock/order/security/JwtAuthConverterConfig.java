package net.greeta.stock.order.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class JwtAuthConverterConfig {

    @Bean
    @Profile("!integration")
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        return jwtGrantedAuthoritiesConverter;
    }
}
