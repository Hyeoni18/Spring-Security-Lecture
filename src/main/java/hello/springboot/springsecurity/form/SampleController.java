package hello.springboot.springsecurity.form;

import hello.springboot.springsecurity.account.Account;
import hello.springboot.springsecurity.account.AccountContext;
import hello.springboot.springsecurity.account.AccountRepository;
import hello.springboot.springsecurity.account.UserAccount;
import hello.springboot.springsecurity.common.CurrentUser;
import hello.springboot.springsecurity.common.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.concurrent.Callable;

@Controller
public class SampleController {

    @Autowired SampleService sampleService;

    @Autowired AccountRepository accountRepository;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserAccount userAccount) { //@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account //Account로 받아올 수도 있음. 근데 너무 길어.
        if(userAccount == null) {
            model.addAttribute("message", "Spring Security");
        } else {
            model.addAttribute("message", "Hello, Welcome "+userAccount.getUsername());
        }
        return "index"; //view name
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("message", "Info");
        return "info"; //view name
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @CurrentUser Account account) {
        model.addAttribute("message", "Hello "+account.getUsername());
        AccountContext.setAccount(accountRepository.findByUsername(account.getUsername())); //우리가 넣긴 했지만 SecurityContextHolder에는 인증된 객체서 알아서 들어감.
        sampleService.dashboard();
        return "dashboard"; //view name
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("message", "Hello Admin, "+principal.getName());
        return "admin"; //view name
    }

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        model.addAttribute("message", "Hello User, "+principal.getName());
        return "user"; //view name
    }

    @GetMapping("/async-handler")
    @ResponseBody
    public Callable<String> asyncHandler() {
        //Callable을 사용해서 return을 하면 Callable 안에 있는 call()을 처리하기 전에 리퀘스트를 처리하고 있던 쓰레드를 반환함. 릴리즈 하고, 그다음에 콜러블 안에서 하는 일이 완료됐을 때쯤 응답을 그제서야 보냄.
        SecurityLogger.log("MVC");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                SecurityLogger.log("Callable");
                return "Async Handler";
            }
        };
    }

    @GetMapping("/async-service")
    @ResponseBody
    public String asyncService() {
        SecurityLogger.log("MVC, before async service");
        sampleService.asyncService();
        SecurityLogger.log("MVC, after async service"); //위의 서비스와 상관없이 호출될거야.
        return "Async Service";
    }

}
