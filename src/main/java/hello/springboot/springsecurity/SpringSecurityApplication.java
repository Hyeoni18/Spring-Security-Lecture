package hello.springboot.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
public class SpringSecurityApplication {

	// 평문으로 저장된 비밀번호가 있을수도 있는데 스프링 시큐리티 버전을 올리면 깨지겠지. 그리고 다른 알고리즘을 쓰고 싶을 수도 있잖아. 이렇게 여러가지 인코더를 지원하기 위해 {id}encodedPassword를 선택하게됨.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

}
