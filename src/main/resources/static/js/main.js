function setListener() {
    let isSecond = $("#isSecond").val();
    if (isSecond === "true") {
        let gameId = $("#id").val();
        stompClient.send("/app/enter", {}, gameId);
    }
}

function finish(name, checkerOnWin) {
    if (checkerOnWin)
        alert(name + " is winner");
    else
        alert("draw");
    window.location.href = '../'
}

function connect() {
    var socket = new SockJS('/tic-tac-toe');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected: " + frame);
        stompClient.subscribe('/topic/enter', function (response) {
            var data = JSON.parse(response.body);
            var username = $("#username").val();
            console.log(username);
            let element = document.getElementById("text");
            element.innerHTML = "Turn of: " + data.player1.toString();
        });
        stompClient.subscribe('/topic/changeGame', function (response) {
            var data = JSON.parse(response.body);
            console.log(data.map)
            draw(data.map, data.userTurn, data.finished, data.winner);
        });
    });
}

function draw(map, userTurn, finished, name) {
    for (let i = 0; i < map.length; i++) {
        console.log(i + " " + map[i])
        if (!document.getElementById((i + 1) + "img") && map[i] !== 0) {
            var img = document.createElement("img");
            if (map[i] === 1)
                img.src = "img/cross.png";
            else
                img.src = "img/round.png";

            img.className = "img-fluid";
            img.id = (i + 1) + "img"
            console.log("123")
            var src = document.getElementById((i + 1).toString());
            src.appendChild(img);
        }
    }
    let element = document.getElementById("text");
    element.innerHTML = "Turn of: " + userTurn.toString();

    if (finished)
        setTimeout(finish(name, name != null), 100);
}

function disconnect() {
    stompClient.disconnect();
}

function sendMessage(id, cell) {
    var obj = {
        id: id,
        cell: cell,
        username: $("#username").val()
    }
    console.log(obj.toString())
    stompClient.send("/app/changeGame", {}, JSON.stringify(obj));
}

connect()
setTimeout(setListener, 200);