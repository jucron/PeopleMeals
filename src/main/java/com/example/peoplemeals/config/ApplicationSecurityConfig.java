package com.example.peoplemeals.config;

import com.example.peoplemeals.services.CredentialsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

import static com.example.peoplemeals.domain.security.Role.ADMIN;
import static com.example.peoplemeals.domain.security.Role.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsServiceImpl credentialsService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, CredentialsServiceImpl credentialsService) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsService = credentialsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        String[] commonAPIs = {"/dishes/**", "/persons/**", "/restaurants/**", "/plannings/**"};

        http.authorizeRequests().antMatchers(GET, commonAPIs).hasAnyAuthority(USER.role, ADMIN.role);
        http.authorizeRequests().antMatchers(POST, commonAPIs).hasAnyAuthority(USER.role, ADMIN.role);
        http.authorizeRequests().antMatchers(PUT, commonAPIs).hasAnyAuthority(USER.role, ADMIN.role);
        http.authorizeRequests().antMatchers(DELETE, commonAPIs).hasAnyAuthority(ADMIN.role);
        http.authorizeRequests().antMatchers("/credentials/**").hasAnyAuthority(ADMIN.role);
        http.authorizeRequests().antMatchers("/credentials/**").hasAnyAuthority(ADMIN.role);

        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .permitAll()
//                .failureHandler(((request, response, authentication) -> {
//                    throw new SecurityException("Unauthorized: please login before proceed");
//                })) //todo: Why this is not Handled by CustomHandler?
//                .successHandler((request, response, authentication) -> {
//                    throw new SecurityException("Login successful!", new Throwable("success"));
//                })
                .defaultSuccessUrl("/success")
                .failureForwardUrl("/failure")
//                .passwordParameter("password") //only for a html template
//                .usernameParameter("username") //only for a html template
                .and()
                .rememberMe()//Default of remember-me is 2 weeks
                .tokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(30))// customizing time to expiration
                .key("somethingverysecured")// Key used to hash the remember-cookie content
                .rememberMeParameter("remember-me")
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/logout_ok")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me");

//        http.authorizeRequests().antMatchers("/", "index", "/css/*", "/js/*").permitAll() //use this if needed resources
//        http.authorizeRequests().antMatchers("/login").permitAll(); //use this in token auth
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //No security web points for use if necessary:
        web.ignoring().antMatchers(
                "/logout_ok",
                "/h2-console"
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(credentialsService);
        return provider;
    }
}
