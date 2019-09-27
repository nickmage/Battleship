var token = localStorage.getItem('token');
    var intervalID;
    var init = init();


    function init(){
        console.log(localStorage.getItem('username'));
        document.getElementById("username").innerText = localStorage.getItem('username');
        intervalID = setInterval(function() {
            var roomId = localStorage.getItem('roomId');
            console.log('before_send');
            $.ajax({
                type: 'GET',
                url: '/lobby',
                headers: {
                    'Authorization':token,
                },
                data: "roomId=" + roomId,
                success: function (data) {
                    if (data === "yes"){
                        goToGame();
                    }
                }
                /*statusCode: {
                    102: function (data) {
                        console.log(data);
                    },
                    202: function (data) {
                        console.log(data);
                        goToGame();
                    }
                }*/
            });
        }, 2000);
    };

    function goToGame(){
        clearInterval(intervalID);
        window.location.replace("#/game");//"/game?token="+ token;
    }

