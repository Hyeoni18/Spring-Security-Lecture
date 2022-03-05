package hello.springboot.springsecurity.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired AccountService accountService;

    @GetMapping
    public String signupForm(Model model) {
        model.addAttribute("account", new Account());
        return "signup";
    }

    @PostMapping
    public String processSignUp(@ModelAttribute Account account) {
        //form에서 넘어오는 data를 account로 받음
        //role이 없으니 추가해주자.
        account.setRole("USER");
        //user저장
        accountService.createNew(account);
        return "redirect:/";
    }
}
