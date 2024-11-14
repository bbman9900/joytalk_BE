package cho.sw.websocketchat.dtos;

import cho.sw.websocketchat.entities.FriendMapping;
import cho.sw.websocketchat.entities.Member;

public record MemberDto(
        Long id,
        String nickName
        //프로필 사진 추가
) {
    public static MemberDto from(Member member) {
        return new MemberDto(member.getId(), member.getNickName());
    }
}
