package hello.springboot.springsecurity.form;

import hello.springboot.springsecurity.account.AccountContext;
import hello.springboot.springsecurity.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class SampleController {

    @Autowired SampleService sampleService;

    @Autowired AccountRepository accountRepository;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if(principal == null) {
            model.addAttribute("message", "Spring Security");
        } else {
            model.addAttribute("message", "Hello, Welcome "+principal.getName());
        }
        return "index"; //view name
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("message", "Info");
        return "info"; //view name
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("message", "Hello "+principal.getName());
        AccountContext.setAccount(accountRepository.findByUsername(principal.getName())); //우리가 넣긴 했지만 SecurityContextHolder에는 인증된 객체서 알아서 들어감.
        sampleService.dashboard();
        return "dashboard"; //view name
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("message", "Hello Admin, "+principal.getName());
        return "admin"; //view name
    }

}
