package cho.sw.websocketchat.dtos;

import cho.sw.websocketchat.entities.FriendMapping;
import cho.sw.websocketchat.entities.Member;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberDto(
        Long id,
        String nickName,
        String profileImage
) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getNickName(), member.getProfileImage());
    }
}
