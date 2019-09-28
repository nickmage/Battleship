    var token = localStorage.getItem('token');
    var roomId = sessionStorage.getItem('roomId');
    var interval;
    var init = init();

    function init(){
        document.getElementById("username").innerText = localStorage.getItem('username');
        interval = setInterval(function() {
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
            });
        }, 2000);
    };

    function goToGame(){
        clearInterval(interval);
        window.location.replace("#/game");
    }

