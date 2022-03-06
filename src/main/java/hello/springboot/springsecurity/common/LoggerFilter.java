package hello.springboot.springsecurity.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoggerFilter extends GenericFilterBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(((HttpServletRequest)servletRequest).getRequestURI()); //uri 이름이 task 이름이 됨.

        filterChain.doFilter(servletRequest, servletResponse); //이걸로 다음 필터를 요청해야 해. 안 하면 다음 필터로 안 넘어감.

        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());
    }
}
