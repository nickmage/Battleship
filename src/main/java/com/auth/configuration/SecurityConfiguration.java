package com.auth.configuration;

import com.auth.filters.JwtAuthenticationFilter;
import com.auth.filters.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    /*private UserRepo userRepo;
    private AuthProvider authProvider;*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/#/", "/",

                            "/auth", "/registration", "/success.html", "/login.html",
                            "/index.html", "/scoreboard.html", "/enter.html", "/redirectedLogin.html", "/registration.html",
                            "/style/**", "/js/**", "/img/**")
                    .permitAll()
                    .anyRequest().authenticated()//authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                /*.and()
                    .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()*/
                .and()
                    .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                    .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("password"))
                .authorities("ROLE_USER");
        auth.inMemoryAuthentication()
                .withUser("lol")
                .password(passwordEncoder().encode("kekeke"))
                .authorities("ROLE_USER");
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, id from account where username=?")
                .authoritiesByUsernameQuery("select username, role from account where username=?");
                /*.authoritiesByUsernameQuery("select u.username, ur.role from account u inner join user_role ur on u.id = ur.user_id where u.username=?");*/
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}