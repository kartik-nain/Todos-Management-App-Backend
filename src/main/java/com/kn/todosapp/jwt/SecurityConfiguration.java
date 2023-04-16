package com.kn.todosapp.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthFilter jwtAuthFilter;
	
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity
			.csrf().disable() //Disabling csrf as no sessions will be stored
			.authorizeHttpRequests() //Configuring which requests to permit and deny
				.antMatchers("/auth/**").permitAll()  //Permitting these requests
				.antMatchers(HttpMethod.OPTIONS,"/**")
                .permitAll()
				.anyRequest().authenticated()  //Making sure all other requests are authenticated
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //Disabling sessions
			.and()
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)  //Calling the jwtAuthFilter before because we first check validation, then set the security context and then call the username password authentication filter
			.logout()
	        .logoutUrl("/auth/logout")
	        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

		return httpSecurity.build();
	}
}
