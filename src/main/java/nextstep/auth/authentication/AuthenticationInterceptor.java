package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.UserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.authentication.AuthValidator.validateAuthentication;

public abstract class AuthenticationInterceptor implements HandlerInterceptor, AuthenticationConverter {

  private final UserDetailsService userDetailsService;

  public AuthenticationInterceptor(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
    AuthenticationToken token = convert(request);
    Authentication authentication = authenticate(token);
    afterAuthentication(request, response, authentication);
    return false;
  }

  public Authentication authenticate(AuthenticationToken authenticationToken) {
    LoginMember loginMember = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

    validateAuthentication(loginMember, authenticationToken.getCredentials());
    return new Authentication(loginMember);
  }

  public abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

  public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;


}
