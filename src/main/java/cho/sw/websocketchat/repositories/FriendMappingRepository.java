package cho.sw.websocketchat.repositories;

import cho.sw.websocketchat.entities.FriendMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendMappingRepository extends JpaRepository<FriendMapping, Long> {
    List<FriendMapping> findAllByMemberId(Long memberId);
    Boolean existsByMemberIdAndFriendId(Long memberId, Long friendId);
    void deleteByMemberIdAndFriendId(Long memberId, Long friendId);
}
