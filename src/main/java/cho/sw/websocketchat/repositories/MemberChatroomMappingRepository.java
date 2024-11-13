package cho.sw.websocketchat.repositories;


import cho.sw.websocketchat.entities.MemberChatroomMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChatroomMappingRepository extends
        JpaRepository<MemberChatroomMapping, Long> {

    Boolean existsByMemberIdAndChatroomId(Long memberId, Long chatroomId);

    void deleteByMemberIdAndChatroomId(Long memberId, Long chatroomId);

    List<MemberChatroomMapping> findAllByMemberId(Long memberId);

}
