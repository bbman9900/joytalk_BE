// 웹소켓 객체 선언
let websocket;

// 웹소켓 연결이 열렸을 때 실행되는 함수
function onOpen() {
  // 사용자 이름을 가져와 서버로 연결 메시지를 전송
  let username = document.getElementById("username");
  websocket.send(username.value + ":" + "connected");
  console.log("connected: onOpen()");
}

// 서버로부터 메시지를 수신했을 때 실행되는 함수
function onMessage(receivedMessage) {
  // 받은 메시지를 화면에 표시
  showMessage(receivedMessage.data);
  console.log("received: onMessage()");
}

// 웹소켓 연결이 닫혔을 때 실행되는 함수
function onClose() {
  console.log("disconnected: onClose()");
}

// 서버에 연결을 요청하는 함수
function connect() {
  // 웹소켓을 서버의 특정 URL과 연결
  websocket = new WebSocket("ws://localhost:8080/ws/chats");
  // 각 이벤트에 해당하는 콜백 함수 설정
  websocket.onmessage = onMessage;
  websocket.onopen = onOpen;
  websocket.onclose = onClose;

  // 연결 상태를 true로 설정
  setConnected(true);
  console.log("connected: connect()");
}

// 웹소켓 연결을 닫는 함수
function disconnect() {
  // 웹소켓 연결 종료
  websocket.close();

  // 연결 상태를 false로 설정
  setConnected(false);
  console.log("disconnected: disconnect()");
}

// 사용자가 메시지를 입력하고 서버로 전송하는 함수
function sendMessage() {
  // 사용자 이름과 입력된 메시지를 가져와 서버로 전송
  let username = document.getElementById("username");
  let message = document.getElementById("message");

  websocket.send(username.value + ":" + message.value);
  message.value = ""; // 메시지 입력 필드 초기화
  console.log("sent: send()");
}

// 메시지를 HTML 요소에 추가하여 화면에 표시하는 함수
function showMessage(message) {
  $("#messages").append("<tr><td>" + message + "</td></tr>");
}

// 연결 상태에 따라 버튼 상태와 대화창 표시를 조절하는 함수
function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#conversation").show();
  } else {
    $("#conversation").hide();
  }
  $("#messages").html("");
}

// 페이지가 로드될 때 버튼 클릭 이벤트를 설정하는 함수
$(function () {
  $("#connect").click(() => connect());
  $("#disconnect").click(() => disconnect());
  $("#send").click(() => sendMessage());
});