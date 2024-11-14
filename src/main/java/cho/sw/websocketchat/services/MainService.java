package cho.sw.websocketchat.services;


import cho.sw.websocketchat.entities.Chatroom;
import cho.sw.websocketchat.entities.FriendMapping;
import cho.sw.websocketchat.entities.Member;
import cho.sw.websocketchat.entities.MemberChatroomMapping;
import cho.sw.websocketchat.repositories.FriendListRepository;
import cho.sw.websocketchat.repositories.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainService {

    private final MemberRepository memberRepository;
    private final FriendListRepository friendListRepository;

    public List<Member> getFriendList(Long memberId) {
        List<FriendMapping> friendMapping = friendListRepository.findAllByMemberId(memberId);

        return friendMapping.stream()
                .map(FriendMapping::getFriend)
                .toList();
    }
    public List<Member> searchMember(String nickName) {
        List<Member> searchedMember = memberRepository.findAllByNickName(nickName);

        return searchedMember;
    }

    public Boolean addFriend(Long memberId, Long friendId) {
        if (friendListRepository.existsByMemberIdAndFriendId(memberId, friendId)) {
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

        friendListRepository.save(friendMapping);

        return true;
    }
}
