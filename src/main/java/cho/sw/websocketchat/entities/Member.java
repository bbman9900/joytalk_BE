package cho.sw.websocketchat.entities;

import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Id
    Long id;

    String email;
    String name;
    @Setter
    String nickName;
    @Setter
    String profileImage;
    String role;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<FriendMapping> friends;
//    @OneToMany(mappedBy = "member")
//    Set<MemberChatroomMapping> memberChatroomMappingSet;
}
