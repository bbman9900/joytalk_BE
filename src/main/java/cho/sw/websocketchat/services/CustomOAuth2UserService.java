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
        OAuth2User oAuth2User = super.loadUser(userRequest);
//        log.info("oAuth2User : {}", oAuth2User);
        Map<String, Object> attributeMap = oAuth2User.getAttribute("kakao_account");
//        log.info("attributeMap : {}", attributeMap);
        String email = (String)attributeMap.get("email");
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
    }
}
