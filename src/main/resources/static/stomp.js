// STOMP 클라이언트 생성, 연결할 웹소켓 서버의 URL 설정
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/stomp/chats'    // 웹소켓 연결을 위한 서버 URL
});

// STOMP 서버와의 연결 성공 시 호출되는 함수
stompClient.onConnect = (frame) => {
    setConnected(true);     // 연결 상태 표시를 위한 UI 업데이트
    showChatrooms();                // 현재 생성된 채팅방 목록을 화면에 표시
    console.log('Connected: ' + frame); // 연결 성공 메시지 로그
};

// 웹소켓 에러가 발생했을 때 실행되는 함수
stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);   // 에러 메시지 출력
};

// STOMP 에러가 발생했을 때 실행되는 함수
stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);    // 에러 메시지 출력
    console.error('Additional details: ' + frame.body);                     // 추가 정보 출력
};

// UI 연결 상태를 설정하는 함수, 버튼 활성화 및 대화창 표시 조정
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#create").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#messages").html("");    // 메시지 창 초기화
}

// STOMP 클라이언트 활성화, 서버와 연결 시작
function connect() {
    stompClient.activate();
}

// STOMP 클라이언트 비활성화, 서버와 연결 종료
function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

// 메시지를 특정 채팅방으로 발송하는 함수
function sendMessage() {
    let chatroomId = $("#chatroom-id").val();
    stompClient.publish({
        destination: "/pub/chats/" + chatroomId,    // 메시지 보낼 대상 채팅방
        body: JSON.stringify(
            {'message': $("#message").val()})   // 메시지 본문
    });
    $("#message").val("")   // 메시지 입력란 초기화
}

// 새로운 채팅방을 생성하는 함수, 서버에 HTTP POST 요청
function createChatroom() {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/chats?title=' + $("#chatroom-title").val(),  // 방 제목 전달
        success: function (data) {
            console.log('data', data);
            showChatrooms();                        // 새로 생성된 방 목록 갱신
            enterChatroom(data.id, true);   // 생성된 방에 자동 입장
        },
        error: function (request, status, error) {
            console.log('request: ', request);
            console.log('error: ', error);
        }
    })
}

// 현재 존재하는 채팅방 목록을 가져와 화면에 표시
function showChatrooms() {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: '/chats',
        success: function (data) {
            console.log('data', data);
            renderChatrooms(data);  // 채팅방 목록을 렌더링
        },
        error: function (request, status, error) {
            console.log('request: ', request);
            console.log('error: ', error);
        }
    })
}

// 채팅방 목록을 HTML 요소로 렌더링
function renderChatrooms(chatrooms) {
    $("#chatroom-list").html("");
    for (let i = 0; i < chatrooms.length; i++) {
        $("#chatroom-list").append(
            "<tr onclick='joinChatroom(" + chatrooms[i].id + ")'><td>"
            + chatrooms[i].id + "</td><td>" + chatrooms[i].title + "</td><td>"
            + chatrooms[i].memberCount + "</td><td>" + chatrooms[i].createdAt
            + "</td></tr>>"
        );
    }
}







let subscription;

// 채팅방 입장 및 구독 설정
function enterChatroom(chatroomId, newMember) {
    $("#chatroom-id").val(chatroomId);      // 현재 입장한 채팅방 ID 설정
    $("#messages").html("");            // 메시지 창 초기화
    showMessages(chatroomId);               // 채팅방 메시지 불러오기
    $("#conversation").show();
    $("#send").prop("disabled", false);
    $("#leave").prop("disabled", false);

    if (subscription) {
        subscription.unsubscribe();     // 기존 구독 해제
        console.log("기존 구독 해제");
    }

    // 새로운 구독 설정
    subscription = stompClient.subscribe('/sub/chats/' + chatroomId,
        (chatMessage) => {
            showMessage(JSON.parse(chatMessage.body));      // 메시지 수신 시 출력
        });
    if (newMember) {
        stompClient.publish({
            destination: "/pub/chats/" + chatroomId,
            body: JSON.stringify(
                {'message': "님이 방에 들어왓습니다."})
        })
    }
}

// 특정 채팅방의 과거 메시지들을 불러옴
function showMessages(chatroomId) {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: '/chats/' + chatroomId + '/messages',
        success: function (data) {
            console.log('data', data);
            for (let i = 0; data.length; i++) {
                showMessage(data[i]);   // 각 메시지를 화면에 출력
            }
        },
        error: function (request, status, error) {
            console.log('request: ', request);
            console.log('error: ', error);
        }
    })
}

// 채팅방 메시지를 화면에 추가
function showMessage(chatMessage) {
    $("#messages").append(
        "<tr><td>" + chatMessage.sender + " : " + chatMessage.message
        + "</td></tr>");
}

// 채팅방에 참여 요청, 서버에 HTTP POST 요청
function joinChatroom(chatroomId) {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/chats/' + chatroomId,
        success: function (data) {
            console.log('data', data);
            enterChatroom(chatroomId, data);
        },
        error: function (request, status, error) {
            console.log('request: ', request);
            console.log('error: ', error);
        }
    })
}

// 채팅방을 나가는 요청을 서버에 전송, 서버에 HTTP DELETE 요청
function leaveChatroom() {
    let chatroomId = $("#chatroom-id").val();
    $.ajax({
        type: 'DELETE',
        dataType: 'json',
        url: '/chats/' + chatroomId,
        success: function (data) {
            console.log('data', data);
            showChatrooms();          // 방 목록 갱신
            exitChatroom(chatroomId);  // 채팅방 퇴장 처리
        },
        error: function (request, status, error) {
            console.log('request: ', request);
            console.log('error: ', error);
        }
    })
}

// 채팅방 퇴장 처리 및 UI 업데이트
function exitChatroom(chatroomId) {
    $("#chatroom-id").val("");                  // 현재 채팅방 ID 제거
    $("#conversation").hide();                      // 대화 UI 숨김
    $("#send").prop("disabled", true);   // 전송 버튼 비활성화
    $("#leave").prop("disabled", true);  // 퇴장 버튼 비활성화
}

// 문서가 로드되면 버튼의 클릭 이벤트를 설정
$(function () {
    $("form").on('submit', (e) => e.preventDefault()); // 폼 제출 막기
    $("#connect").click(() => connect());       // 연결 버튼
    $("#disconnect").click(() => disconnect());  // 연결 해제 버튼
    $("#create").click(() => createChatroom());  // 채팅방 생성 버튼
    $("#leave").click(() => leaveChatroom());    // 채팅방 나가기 버튼
    // $("#send").click(() => sendMessage());       // 메시지 전송 버튼
    // 메시지 전송 버튼에 한 번만 이벤트 바인딩되도록 수정
    $("#send").off('click').on('click', () => sendMessage());
});