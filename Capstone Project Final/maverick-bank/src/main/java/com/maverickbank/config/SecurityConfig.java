package com.maverickbank.config;

import com.maverickbank.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService ;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/api/customer/add").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/branch/all").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/admin/add").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/auth/login").authenticated()

                        .requestMatchers(HttpMethod.POST,"/api/employee/add").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/employee/all").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/employee/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/employee/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/employee/deactivate/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/customer/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/customer/deactivate/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/admin/all").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/admin/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/branch/add").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/loan/add").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/customer/profile").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/account/add").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/account/my").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/account/my/{id}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/beneficiary/add").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/beneficiary/my").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/closure/add/{accountId}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/transaction/deposit").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/transaction/withdraw").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/transaction/transfer").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/transaction/my/{accountNumber}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/loan/apply").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/loan/application/my").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/v1/loan/application/availed").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/v2/loan/application/{applicationId}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST,"/api/loan/application/repay").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/transaction/statement/{accountNumber}").hasAuthority("CUSTOMER")

                        .requestMatchers(HttpMethod.GET,"/api/customer/all").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/customer/{id}").hasAnyAuthority("ADMIN", "EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/account/pending").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/account/details/{id}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/account/number/{accountNumber}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT,"/api/account/approve/{id}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/closure/pending").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT,"/api/closure/approve/{requestId}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT,"/api/closure/reject/{requestId}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/transaction/all").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/transaction/{accountNumber}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/transaction/summary/{accountId}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/loan/application/pending").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/loan/application/approved").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT,"/api/loan/application/review/{applicationId}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT,"/api/loan/application/disburse/{applicationId}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/loan/application/repaid").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT,"/api/loan/application/close/{applicationId}").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/report/financial").hasAuthority("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/report/regulatory").hasAuthority("EMPLOYEE")

                        .anyRequest().authenticated()
                );
        http.addFilterBefore(jwtFilter , UsernamePasswordAuthenticationFilter.class);

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationProvider getProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(getEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }
}
