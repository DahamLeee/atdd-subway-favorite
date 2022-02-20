package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.member.application.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.subway.unit.AuthTarget.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

  @Mock
  private UserDetailsService userDetailsService;

  private AuthenticationInterceptor authenticationInterceptor;

  @BeforeEach
  void setup() {
    authenticationInterceptor = new AuthenticationInterceptor(userDetailsService) {
      @Override
      public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return AUTH_TOKEN;
      }

      @Override
      public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
      }
    };
  }

  @Test
  void preHandle() throws IOException {
    // given
    createMockLoginMember(userDetailsService);
    MockHttpServletRequest mockRequest = createTokenMockRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    // when
    boolean result = authenticationInterceptor.preHandle(mockRequest, mockResponse, new Object());

    // then
    assertThat(result).isFalse();
  }


  @Test
  void authenticate() throws IOException {
    // given
    createMockLoginMember(userDetailsService);

    // when
    Authentication authentication = authenticationInterceptor.authenticate(AUTH_TOKEN);

    // then
    assertThat(authentication.getPrincipal()).isEqualTo(LOGIN_MEMBER);
  }

  @Test
  void invalidAuthenticate() throws IOException {
    // given
    createMockLoginMember(userDetailsService);

    // when & then
    assertThatThrownBy(
      () -> authenticationInterceptor.authenticate(InvalidAuthTarget.AUTH_TOKEN)
    ).isInstanceOf(AuthenticationException.class);
  }

  @Test
  void nonExistAuthenticate() throws IOException {
    // when & then
    assertThatThrownBy(
      () -> authenticationInterceptor.authenticate(new AuthenticationToken(InvalidAuthTarget.EMAIL, InvalidAuthTarget.PASSWORD))
    ).isInstanceOf(AuthenticationException.class);
  }

}
