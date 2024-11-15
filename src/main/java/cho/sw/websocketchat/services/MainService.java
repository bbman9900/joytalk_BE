package cho.sw.websocketchat.services;


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
public class MainService {

    private final MemberRepository memberRepository;
    private final FriendMappingRepository friendMappingRepository;

    @Transactional(readOnly = true)
    public List<Member> getFriendList(Long memberId) {
        List<FriendMapping> friendMapping = friendMappingRepository.findAllByMemberId(memberId);

        return friendMapping.stream()
                .map(FriendMapping::getFriend)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<Member> searchMember(String nickName) {

        return memberRepository.findAllByNickNameContaining(nickName);
    }

    public Boolean addFriend(Long memberId, Long friendId) {
        if (friendMappingRepository.existsByMemberIdAndFriendId(memberId, friendId)) {
            log.info("이미 추가된 친구입니다.");
            return false;
        }
        Optional<Member> memberById = memberRepository.findById(memberId);
        Optional<Member> friendById = memberRepository.findById(friendId);

        if (memberById.isEmpty() || friendById.isEmpty()) {
            log.info("해당 유저가 존재하지 않습니다.");
            return false;
        }
        FriendMapping friendMapping = FriendMapping.builder()
                .member(memberById.get())
                .friend(friendById.get())
                .build();

        friendMappingRepository.save(friendMapping);

        return true;
    }



    public Boolean deleteFriend(Long memberId, Long friendId) {
        if (!friendMappingRepository.existsByMemberIdAndFriendId(memberId, friendId)) {
            log.info("친구 관계가 아닌 사용자입니다.");
            return false;
        }

        friendMappingRepository.deleteByMemberIdAndFriendId(memberId, friendId);

        return true;
    }
}
