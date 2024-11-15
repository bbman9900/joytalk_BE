package cho.sw.websocketchat.handlers;

import cho.sw.websocketchat.jwt.JwtTokenProvider;
import cho.sw.websocketchat.entities.Member;
import cho.sw.websocketchat.repositories.MemberRepository;
import cho.sw.websocketchat.vos.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String REDIRECT_URI = "http://localhost:5173/Friends";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        try {
            log.debug("Authentication success - Principal: {}", authentication.getPrincipal());
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            log.debug("OAuth2User details: {}", oAuth2User);

            // Member 정보 가져오기
            Member member = memberRepository.findByEmail(oAuth2User.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // JWT 토큰 생성
            String token = jwtTokenProvider.createToken(member);

            String targetUrl = UriComponentsBuilder
                    .fromUriString(REDIRECT_URI)
                    .queryParam("token", token)
                    .build().toUriString();

            log.debug("Redirecting to: {}", targetUrl);
            response.sendRedirect(targetUrl);
        } catch (Exception e) {
            log.error("인증 성공 처리 중 에러 발생", e);
            response.sendRedirect(REDIRECT_URI + "?error=authentication_failed");
            throw e;
        }
    }
}
