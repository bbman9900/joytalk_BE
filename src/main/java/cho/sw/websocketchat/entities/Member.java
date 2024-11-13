package cho.sw.websocketchat.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    String nickName;
    String name;
//    @Enumerated(EnumType.STRING)
//    Gender gender;
    String phoneNumber;
    LocalDate birthDay;
    String role;

//    @OneToMany(mappedBy = "member")
//    Set<MemberChatroomMapping> memberChatroomMappingSet;
}
