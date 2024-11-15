package cho.sw.websocketchat.controllers;


import cho.sw.websocketchat.dtos.MemberDto;
import cho.sw.websocketchat.entities.Member;
import cho.sw.websocketchat.repositories.MemberRepository;
import cho.sw.websocketchat.services.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@RestController
public class MyPageController {
    
    private final MyPageService myPageService;

    @GetMapping()
    @Operation(summary = "내 정보 조회", description = "내 정보 조회")
    public MemberDto getMe(@RequestParam Long memberId) {
        Member me = myPageService.getMe(memberId);
        return MemberDto.from(me);
    }

    @PatchMapping()
    @Operation(summary = "내 정보 수정", description = "내 정보 수정")
    public ResponseEntity<?> updateMe(@RequestBody MemberDto memberDto) {
        Member preMe = myPageService.getMe(memberDto.id());

        if (preMe == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");

        if (memberDto.nickName() != null) preMe.setNickName(memberDto.nickName());
//        if (memberDto.profileImage() != null) preMe.setProfileImage(memberDto.profileImage());

        Member afterMe = myPageService.updateMe(preMe);

        if (preMe.getNickName().equals(afterMe.getNickName()) /*&& preMe.getProfileImage().equals(afterMe.getProfileImage())*/) {
            return ResponseEntity.ok("Profile updated successfully");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }

    @DeleteMapping()
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    public ResponseEntity<?> deleteFriend(@RequestParam Long memberId) {
        myPageService.deleteFriend(memberId);

        return ResponseEntity.ok("Profile updated successfully");
    }
}
