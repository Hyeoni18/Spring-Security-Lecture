package hello.springboot.springsecurity.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    // Spring Security가 요구하는 패턴. {noop}에는 알고리즘 이름이 들어감. 패스워드를 인코딩하는.
    // TODO {noop}123
    @Autowired AccountRepository accountRepository;

    @Autowired PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        //Account를 UserDetails타입으로 바뀌기 위해 스프링 시큐리티가 제공하는 User.builder.
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                //.authorities("ROLE_USER") // authorities를 쓰려면 ROLE_ prefix 필요.
                .roles(account.getRole())
                //.roles(account.getRole(), "USER") 롤을 여러 개 관리하는 방법 중 하나
                .build();
    }

    public Account createNew(Account account) {
        //account.setPassword("{noop}"+account.getPassword()); //패스워드 인코딩
        //account 안으로 넣을 수도 있음.
        account.encodePassword(passwordEncoder);
        return accountRepository.save(account);
    }
}
