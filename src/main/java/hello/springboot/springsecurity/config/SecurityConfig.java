package hello.springboot.springsecurity.config;

import hello.springboot.springsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired AccountService accountService;

    public SecurityExpressionHandler expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        return handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**") //@Order 보다는 antMatcher을 사용해서 매칭이 되도록 하는게 좋음. 모든 요청을 처리할건데 그 중 mvcMatchers는 이런 권한을 가지고 접근을 해야 해. 그리고 폼 로그인하고 httpBasic을 지원하겠다.
                .authorizeRequests()
                .mvcMatchers("/","/info","/account/**","/signup").permitAll()
                .mvcMatchers("/admin").hasAnyAuthority("ROLE_ADMIN") //hasAnyAuthority가 좀 더 제네릭함.
                .mvcMatchers("/user").hasRole("USER") //hasRole은 hasAnyAuthority의 하위 개념. hasRole은 profix가 없어도 됨.
                //.anyRequest().anonymous() // 익명사용자만 허용. 인증이 되면 접근할 수 없음.
                //.anyRequest().rememberMe() //rememberMe 기능으로 인증을 한 사용자만 허용
                //.anyRequest().fullyAuthenticated() // rememberMe로 인증이 된 사용자인 경우 다시 로그인을 요구. 위에선 rememberMe로 해도 괜찮았다가, 여기서 다시 요구하는거야. 그러니까 중요한 페이지에서 한 번 더 인증을 요구한다고 생각하면 됨.
                .anyRequest().authenticated()
                .expressionHandler(expressionHandler())
                ;
        http.formLogin()
                .loginPage("/login")
                .permitAll()
        ;

        http.rememberMe()
                //.rememberMeParameter("remember") //이렇게 설정하면 login.html name을 remember-me 가 아닌 remember라고 적을 수 있음.
                //.tokenValiditySeconds() //쿠키 유지 시간, 기본값은 2주.
                //.useSecureCookie(true) //https만 접근 가능하도록 하는거. https를 적용하면 true라고 설정하는게 좋음.
                //.alwaysRemember(true) //우리가 form에서 파라미터를 보내지 않더라도 remember 하는거임. 기본값은 false. true하면 기본적으로 쿠키가 남게됨.
                        .userDetailsService(accountService)
                                .key("remember-me-sample");

        http.httpBasic();

        http.logout()
  //              .logoutUrl("/logout") //로그아웃 url설정. 나중에 커스텀할 때 화면 만들고 변경하면 돼.
                .logoutSuccessUrl("/") //기본은 /login
        //        .addLogoutHandler() //로그아웃할 때 부가적인 일을 추가할 수 있음.
        //        .logoutSuccessUrl() //별도로 구현해서 넣으면 돼.
        //        .invalidateHttpSession(true) //로그아웃 한 다음 세션을 invalid 처리할거냐. 기본값은 true
        //        .deleteCookies("") //만약 쿠키 기반의 로그인을 사용했다. 그러면 쿠키의 이름을 적어줘. 나중에 새로운 쿠키를 발급받게 해.
                ;

        // TODO ExceptionTranslationFilter -> FilterSecurityInterceptor (AccessDecisionManager, AffirmativeBased 구현체로 사용해서 인가처리를 하는데 이때 두 가지 예외가 발생할 수 있음.)
        // TODO AuthenticationException, 인증 자체가 안 되어있다. -> AuthenticationEntryPoint, 해당 유저를 인증(로그인) 할 수 있게 보내줌.
        // TODO AccessDeniedException, 인증은 됐는데 권한이 충분하지 않다. -> AccessDeniedHandler, 404 페이지, 501 페이지 등
        // ExceptionTranslationFilter는 예외에 따라 다른 처리를 함.
        // 둘은 밀접한 관계임. 둘의 순서가 바뀌면 안 돼. ExceptionTranslationFilter가 FilterSecurityInterceptor를 감싸고 실행되어야 하기 때문임. try/catch로 감싸고 필터 처리를 해.

        http.exceptionHandling()
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            //별도의 클래스를 만들어 bean에 등록하여 불러와 사용해도 됨.
                            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                            String username = principal.getUsername();
                            //원래는 logger를 사용해야 하지만 편의상 print를 씀.
                            System.out.println(username + " is denied to access "+ request.getRequestURI());
                            response.sendRedirect("/access-denied");
                        });

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
