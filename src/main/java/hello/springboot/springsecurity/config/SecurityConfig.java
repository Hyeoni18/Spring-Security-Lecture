package hello.springboot.springsecurity.config;

import hello.springboot.springsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //인증 거치지 않아도 된다.는 여기서 설정
        http.authorizeRequests()
                .mvcMatchers("/","/info","/account/**").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated() //기타 등등
                ;

        http.formLogin();  //form로그인 사용할거야
        http.httpBasic();  //httpBasic도 쓸거야
    }

    //명시적으로 선언, 근데 이렇게 안해도, bean으로 등록만 되어있으면 됨.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService); //우리가 사용할 userDetailService가 이거임. 이라고 알려줌.
    }
}
