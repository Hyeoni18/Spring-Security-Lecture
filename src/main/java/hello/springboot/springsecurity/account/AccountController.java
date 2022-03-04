package hello.springboot.springsecurity.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{role}/{username}/{password}") //예시일 뿐 이렇게 작성하면 안됨.
    public Account createAccount(@ModelAttribute Account account) {
        return accountService.createNew(account);
    }

}
