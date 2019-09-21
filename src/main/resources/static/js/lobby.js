    var token = document.getElementById("token").innerText;
    var username = document.getElementById("user").innerText;

    window.onload = function(){ setInterval(function() {
        $.ajax({
            type: 'POST',
            url: '/lobby',
            data: "token=" +token +"&username="+username,
            success: function (data) {
                if (data === "yes"){
                    goToGame();
                    clearInterval();
                }
            }
        });
    }, 2000)};

    function goToGame(){
        window.location.href = "/game?token="+ token;
    }