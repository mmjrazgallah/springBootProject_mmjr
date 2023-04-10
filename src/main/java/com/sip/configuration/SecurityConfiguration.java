package com.sip.configuration;

import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
        import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.builders.WebSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
        import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private DataSource dataSource;
    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
              //  .antMatchers("/").permitAll() // accès pour tous users
                .antMatchers("/login").permitAll() // accès pour tous users
                .antMatchers("/registration").permitAll() // accès pour tous users
                .antMatchers("/fragments/**").permitAll()
                .antMatchers("/user/**").hasAuthority("SUPERADMIN")
                .antMatchers("/role/**").hasAuthority("SUPERADMIN")
                .antMatchers("/actuality/**").hasAnyAuthority("SUPERADMIN","ADMIN")
                .antMatchers("/provider/**").hasAnyAuthority("SUPERADMIN","ADMIN")
                .antMatchers("/article/**").hasAnyAuthority("SUPERADMIN","ADMIN","USER").anyRequest()
                .authenticated().and().csrf().disable().formLogin() // l'accès defait via unformulaire

                .loginPage("/login").failureUrl("/login?error=true") // fixer la page login

                .defaultSuccessUrl("/home") // page d'accueil après login avecsuccès
                .usernameParameter("email") // paramètres d'authentifications login et password
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // route dedeconnexionici /logut
                .logoutSuccessUrl("/login").and().exceptionHandling() // une fois deconnecté redirection vers login
                .accessDeniedPage("/403");
    }

    // laisser l'accès auxressources
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/plugins/**" , "/icons/**", "/images/user/**","/uploads/logoProviders/**","/uploads/picturesArticles/**","/uploads/actualityMedia/**");
    }


}




