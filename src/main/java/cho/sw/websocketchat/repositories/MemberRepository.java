package cho.sw.websocketchat.repositories;


import cho.sw.websocketchat.entities.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    List<Member> findByEmail(String email);

    Optional<Member> findByEmail(String email);

    List<Member> findAllByNickName(String nickName);
}

