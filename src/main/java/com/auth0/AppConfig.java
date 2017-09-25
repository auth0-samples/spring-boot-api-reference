package com.auth0;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity(debug = true)
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${auth0.domain}")
    private String domain;

    @Value(value = "${auth0.clientId}")
    private String clientId;

    @Value(value = "${auth0.clientSecret}")
    private String clientSecret;

    @Value(value = "${auth0.apiAudience}")
    private String apiAudience;

    public String getDomain() {
        return domain;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getApiAudience() {
        return apiAudience;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();

        final String issuer = "https://" + this.getDomain() + "/";

        JwtWebSecurityConfigurer
                .forRS256(getApiAudience(), issuer)
                .configure(http)
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/health").permitAll()
                .antMatchers(HttpMethod.GET, "/mappings").permitAll()
                .antMatchers(HttpMethod.GET, "/digital/password/update").hasAuthority("update:password")
                .anyRequest().authenticated();
    }

}
