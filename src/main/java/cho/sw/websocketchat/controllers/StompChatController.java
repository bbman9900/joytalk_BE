package cho.sw.websocketchat.controllers;


import cho.sw.websocketchat.dtos.ChatMessage;
import cho.sw.websocketchat.entities.Message;
import cho.sw.websocketchat.services.ChatService;
import cho.sw.websocketchat.vos.CustomOAuth2User;
import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompChatController {

    private final ChatService chatService;

    @MessageMapping("/chats/{chatroomId}")
    @SendTo("/sub/chats/{chatroomId}")
    public ChatMessage handleMessage(/*@AuthenticationPrincipal*/ Principal principal, @DestinationVariable Long chatroomId, @Payload Map<String, String> payload) {
        log.info("StompChatController : {} sent {} in {}", principal.getName(), payload, chatroomId);
        CustomOAuth2User user = (CustomOAuth2User) ((OAuth2AuthenticationToken) principal).getPrincipal();
        String message = payload.get("message").equals("님이 방에 들어왓습니다.") ? principal.getName() + "님이 방에 들어왓습니다." : payload.get("message");
        chatService.saveMessage(user.getMember(), chatroomId, message);

        return new ChatMessage(principal.getName(), payload.get("message"));
    }
}
