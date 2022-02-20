package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {
  public static final String USERNAME_FIELD = "username";
  public static final String PASSWORD_FIELD = "password";

  public SessionAuthenticationInterceptor(UserDetailsService userDetailsService) {
    super(userDetailsService);
  }

  public AuthenticationToken convert(HttpServletRequest request) {
    Map<String, String[]> paramMap = request.getParameterMap();
    String principal = paramMap.get(USERNAME_FIELD)[0];
    String credentials = paramMap.get(PASSWORD_FIELD)[0];

    return new AuthenticationToken(principal, credentials);
  }

  @Override
  public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    HttpSession httpSession = request.getSession();
    httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
    response.setStatus(HttpServletResponse.SC_OK);
  }
}
