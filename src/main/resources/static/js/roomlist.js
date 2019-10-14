    var token = localStorage.getItem('token');
    var init = init();
    var roomId;
    var username = localStorage.getItem('username');

    function init(){
        var playerName = localStorage.getItem('username');
        $.ajax({
            type: 'GET',
            url: '/roomlist',
            headers: {
                'Authorization':token,
            },
            data: "playerName=" + playerName,
            statusCode: {
                200: function (data) {
                    console.log(data);
                    appendTable(data);
                },
                400: function (data) {
                    alert(data);
                }
            }
        });
    }

    function appendTable(list){
        var data;
        if (list.length == 0) {
            data = 'There is no available private rooms at the moment.';
        } else {
            data = '<table><tr><td>Room</td><td>Room creator</td><td></td><td></td></tr>';
            for (let i = 0; i < list.length; i++){
                data += '<tr><td>' + list[i].roomName + '</td><td>' + list[i].player1Name + '</td><td><button id="' + list[i].roomId + '" onclick="showConfirmation(this)">Join</button></td></tr>';
            }
            data +='</table>';
        }
        $('#roomList').append(data);
    }

    function showConfirmation(element) {
        roomId = element.getAttribute('id');
        $('.popup').show();
        $('.overlay_popup').show();
        $('.object').show();
    }

    function enter(){
        var password = document.getElementById("password").value;
        document.getElementById("passwordError").hidden = true;
        if (password === ''){
            document.getElementById("passwordError").innerHTML = 'Password should not be empty.';
            document.getElementById("passwordError").hidden = false;
        } else {
            $.ajax({
                type: 'POST',
                url: '/roomlist',
                headers: {
                    'Authorization':token,
                },
                data: "password=" + password + "&username=" + username + "&roomId=" + roomId,
                statusCode: {
                    200: function (data) {
                        sessionStorage.setItem('roomId', roomId);
                        window.location.replace("#/start");
                    },
                    400: function (data) {
                    console.log(data);
                        document.getElementById("passwordError").innerHTML = data.responseText;
                        document.getElementById("passwordError").hidden = false;
                 }
                }
            });
        }
    }

    function closePopup(){
        $('.popup').hide();
        $('.overlay_popup').hide();
        $('.object').hide();
    }
