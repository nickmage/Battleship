function validation() {
    var roomName = document.getElementById("roomName").value;
    var password = document.getElementById("password").value;
    var playerId = localStorage.getItem("playerId");
    var token = localStorage.getItem('token');
    document.getElementById("roomNameError").hidden = true;
    document.getElementById("passwordError").hidden = true;
    if (roomName === ''){
        document.getElementById("roomNameError").innerHTML = 'Room name should not be empty.';
        document.getElementById("roomNameError").hidden = false;
    } else if (password === ''){
        document.getElementById("passwordError").innerHTML = 'Password should not be empty.';
        document.getElementById("passwordError").hidden = false;
    } else {
        $.ajax({
            type: 'POST',
            url: '/room',
            headers: {
                'Authorization':token,
            },
            data: "roomName=" + roomName + "&password=" + password + "&playerId=" + playerId,
            success: function (data) {
                sessionStorage.setItem('roomId', data);
                 window.location.replace("#/start");
            },
            error: function (data) {
                console.log(data);
            }
        });
    }
}