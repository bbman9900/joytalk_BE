package cho.sw.websocketchat.repositories;


import cho.sw.websocketchat.entities.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

}
