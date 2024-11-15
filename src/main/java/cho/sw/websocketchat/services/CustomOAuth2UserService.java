package cho.sw.websocketchat.services;

import cho.sw.websocketchat.entities.Member;
import cho.sw.websocketchat.repositories.MemberRepository;
import cho.sw.websocketchat.vos.CustomOAuth2User;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            log.debug("OAuth2UserRequest : {}", userRequest);
            OAuth2User oAuth2User = super.loadUser(userRequest);
            log.debug("OAuth2User : {}", oAuth2User);
//        log.info("oAuth2User : {}", oAuth2User);
            String email = oAuth2User.getAttribute("email");
//        log.info("attributeMap : {}", attributeMap);
//        log.info("email : {}", email);
//        Optional<Member> byEmail = memberRepository.findByEmail(email);
//        log.info("findByEmail : {}", byEmail);
            Member member = memberRepository.findByEmail(email)
                    .orElseGet(() -> {
                        log.info("member not");
                        Member newMember = MemberFactory.create(userRequest, oAuth2User);
                        return memberRepository.save(newMember);
                    });

            return new CustomOAuth2User(member, oAuth2User.getAttributes());
        } catch (Exception e) {
            log.error("OAuth2 로그인 처리 중 에러 발생", e);
            throw e;
        }
    }
}
