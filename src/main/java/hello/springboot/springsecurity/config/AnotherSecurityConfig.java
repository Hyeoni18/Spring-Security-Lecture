package hello.springboot.springsecurity.config;

import hello.springboot.springsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE -15)
public class AnotherSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/account/**")// 이 설정은 여기서 /account로 오는 요청만 이 설정으로. 여기서 설정하는 필터들의 목록으로 처리할건데, 모든 요청 다 권한 확인 없이 다 허용하면 좋겠다. 라고 선언한거야. 여기 설정에 따라 필터들의 목록이나 구체적인 설정이 달라지는거야.
                .authorizeRequests()
                .anyRequest().permitAll()
        ;
    }
}
