package cho.sw.websocketchat.controllers;

import cho.sw.websocketchat.dtos.MemberDto;
import cho.sw.websocketchat.entities.Member;
import cho.sw.websocketchat.services.MainService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/friends")
@RestController
public class MainController {

    private final MainService mainService;

    @GetMapping("/list")
    @Operation(summary = "친구 목록 조회", description = "친구 목록 조회")
    public List<MemberDto> getFriends(@RequestParam Long memberId) {
        List<Member> friendList = mainService.getFriendList(memberId);

        return friendList.stream()
                .map(MemberDto::from)
                .toList();
    }

    @GetMapping("/search")
    @Operation(summary = "친구 검색", description = "친구 검색")
    public List<MemberDto> findMembers(@RequestParam String nickName) {
        List<Member> memberList = mainService.searchMember(nickName);

        return memberList.stream()
                .map(MemberDto::from)
                .toList();
    }

    @PostMapping("/add")
    @Operation(summary = "친구 추가", description = "친구 추가")
    public Boolean addFriend(@RequestParam Long memberId, @RequestParam Long friendId) {
        return mainService.addFriend(memberId, friendId);
    }
}
