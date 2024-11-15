package cho.sw.websocketchat.services;


import cho.sw.websocketchat.dtos.MemberDto;
import cho.sw.websocketchat.entities.FriendMapping;
import cho.sw.websocketchat.entities.Member;
import cho.sw.websocketchat.repositories.FriendMappingRepository;
import cho.sw.websocketchat.repositories.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MyPageService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMe(Long memberId) {
        Optional<Member> me = memberRepository.findById(memberId);

        return me.orElse(null);
    }

    public Member updateMe(Member member) {
        return memberRepository.save(member);
    }

    public Boolean deleteFriend(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if (!memberRepository.existsById(memberId)) {
            log.info("존재하지 않는 사용자입니다.");
            return false;
        }

        memberRepository.deleteById(memberId);

        return true;
    }
}
