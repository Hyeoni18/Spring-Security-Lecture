package hello.springboot.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //인증 거치지 않아도 된다.는 여기서 설정
        http.authorizeRequests()
                .mvcMatchers("/","/info").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated() //기타 등등
                ;

        http.formLogin();  //form로그인 사용할거야
        http.httpBasic();  //httpBasic도 쓸거야
    }

    //내가 원하는 유저정보를 여러 개 쓸 거야.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring").password("{noop}boot").roles("USER")
                .and()
                .withUser("admin").password("{noop}123").roles("ADMIN");
    }
}
