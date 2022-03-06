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

        return new UserAccount(account);
    }

    public Account createNew(Account account) {
        //account.setPassword("{noop}"+account.getPassword()); //패스워드 인코딩
        //account 안으로 넣을 수도 있음.
        account.encodePassword(passwordEncoder);
        return accountRepository.save(account);
    }
}
