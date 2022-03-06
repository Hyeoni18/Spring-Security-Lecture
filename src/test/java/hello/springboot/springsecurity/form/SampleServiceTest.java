package hello.springboot.springsecurity.form;

import hello.springboot.springsecurity.account.Account;
import hello.springboot.springsecurity.account.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SampleServiceTest {

    @Autowired
    SampleService sampleService;

    @Autowired
    AccountService accountService;

    @Autowired
    AuthenticationManager authenticationManager; //인증

    @Test
    //@WithMockUser //테스트가 목적이라면 어노테이션을 통해 밑의 코드 없이 실행 가능해.
    public void dashboard() {
        //Secured가 없었으면 메소드엔 들어가서 null 에러가 났을거야.
        //근데 Secured로 인해 인가 에러가 뜸. CglibAopProxy를 보면 aop가 적용된 걸 볼 수 있어.

        //데스크탑 애플리케이션은 authenticationManager를 직접 사용하면 됨.
        Account account = new Account();
        account.setRole("ADMIN");
        account.setUsername("spring");
        account.setPassword("123");
        accountService.createNew(account);

        UserDetails userDetails = accountService.loadUserByUsername("spring");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "123");
        Authentication authenticate = authenticationManager.authenticate(token); //인증된 authenticate

        SecurityContextHolder.getContext().setAuthentication(authenticate); //코드로 인증하는 방법.

        sampleService.dashboard();
    }

}