    var roomId = localStorage.getItem('roomId');
    var intervalID;

    window.onload = function(){
        console.log(localStorage.getItem('username'));
        document.getElementById("username").innerText = localStorage.getItem('username');
        intervalID = setInterval(function() {
            $.ajax({
                type: 'POST',
                url: '/lobby',
                headers: {
                    'Authorization':token,
                },
                data: "roomId=" + roomId,
                statusCode: {
                    102: function (data) {
                        console.log(data);
                    },
                    200: function (data) {
                        console.log(data);
                        goToGame();
                    }
                }
            });
        }, 2000);
    };

    function goToGame(){
        clearInterval(intervalID);
        window.location.href = "#/game";//"/game?token="+ token;
    }

