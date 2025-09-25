package com.project.springseurity.config;


import com.project.springseurity.exceptionhandling.CustomAccessDeniedHandler;
import com.project.springseurity.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.project.springseurity.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

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
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler =  new CsrfTokenRequestAttributeHandler();
        http.securityContext(context->context.requireExplicitSave(false)).
                sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors(corsConfig-> corsConfig.configurationSource(new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setMaxAge(3600L);
                return  corsConfiguration;}}))
//                        .sessionManagement(smc->smc.invalidSessionUrl("/invalidSession").maximumSessions(1)
//                        .maxSessionsPreventsLogin(true))
                        .requiresChannel(rcc->rcc.anyRequest().requiresInsecure()). // only http
                csrf(csrfConfig->
                csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("/myContact","/myNotification")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) ->
                        requests.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                                .requestMatchers("/myBalance").hasAuthority("VIEWBALANCE")
                               // requestMatchers("/myAccount","/myBalance","myCards","myLoans").authenticated()
                                .requestMatchers("/myContact","/myNotification","/error","/register","/invalidSession").permitAll());
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
