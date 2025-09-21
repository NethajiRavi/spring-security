package com.project.springseurity.config;


import com.project.springseurity.exceptionhandling.CustomAccessDeniedHandler;
import com.project.springseurity.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class SecurityConfig {

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//      return new JdbcUserDetailsManager(dataSource);
//    }


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());

        http.sessionManagement(smc->smc.invalidSessionUrl("/invalidSession").maximumSessions(1)
                        .maxSessionsPreventsLogin(true)).
                requiresChannel(rcc->rcc.anyRequest().requiresInsecure()). // only http

                csrf(csrfConfig-> csrfConfig.disable())
                        .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/myAccount","/myBalance","myCards","myLoans").authenticated()
                                .requestMatchers("/myContact","/myNotification","/error","/register").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(hbc -> hbc.authenticationEntryPoint( new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc-> new CustomAccessDeniedHandler()) ;
//        http.exceptionHandling(ehc->
//                ehc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())); //Global config
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


  /*  @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }*/
}
