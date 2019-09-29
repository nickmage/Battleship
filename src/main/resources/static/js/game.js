    var token = localStorage.getItem('token');
    var roomId = sessionStorage.getItem('roomId');
    var playerId = localStorage.getItem('playerId');
    var interval;
    var init = init();

    function init(){
        getBoards();//readycheck
        /*ShotRequest();

        document.getElementById("playerName").innerText = 'Your ships, ' + localStorage.getItem('username');
        



            interval = setInterval(function() {

            }, 2000);*/
    }

    function getBoards(){
        $.ajax({
            type: 'GET',
            url: '/game/init',
            headers: {
            'Authorization':token,
            },
            data: "roomId=" + roomId + "&playerId=" + playerId,
            success: function (data) {
                console.log(data);
                setPlayerAndEnemyInfoVisible(data.enemyName);
                showShips(data.playerShips, data.enemyShips);
                showPlayerBoard(data.playerBoard);
                showEnemyBoard(data.enemyBoard, data.myTurn);
            }
        });
    }

    function setPlayerAndEnemyInfoVisible(enemyName){
        var playerInfo = document.getElementById('playerInfo');
        var enemyInfo = document.getElementById('enemyInfo');
        playerInfo.innerHTML = 'Your ships, ' + localStorage.getItem('username');
        playerInfo.hidden = false;
        enemyInfo.innerHTML = enemyName + '\'s enemy ships';
        enemyInfo.hidden = false;
    }

    function showShips(playerShips, enemyShips){
        document.getElementById('playerShip1Quantity').innerHTML = 'x' + playerShips.oneDeckShips;
        document.getElementById('playerShip2Quantity').innerHTML = 'x' + playerShips.twoDeckShips;
        document.getElementById('playerShip3Quantity').innerHTML = 'x' + playerShips.threeDeckShips;
        document.getElementById('playerShip4Quantity').innerHTML = 'x' + playerShips.fourDeckShips;
        document.getElementById('enemyShip1Quantity').innerHTML = 'x' + enemyShips.oneDeckShips;
        document.getElementById('enemyShip2Quantity').innerHTML = 'x' + enemyShips.twoDeckShips;
        document.getElementById('enemyShip3Quantity').innerHTML = 'x' + enemyShips.threeDeckShips;
        document.getElementById('enemyShip4Quantity').innerHTML = 'x' + enemyShips.fourDeckShips;
    }

    function showPlayerBoard(playerBoard){
        for (let i = 0; i < playerBoard.length; i++){
            if (playerBoard[i].value > 0) {
                document.getElementById("p" + playerBoard[i].x + playerBoard[i].y).style.backgroundColor = "rgb(227, 227, 117)";
            }
            if (playerBoard[i].value < 0) {
                if (playerBoard[i].value >= -4 && playerBoard[i].value <= -1) {
                    document.getElementById("p" + playerBoard[i].x + playerBoard[i].y).style.backgroundColor = "rgb(255,2,0)";
                } else {
                    document.getElementById("p" + playerBoard[i].x + playerBoard[i].y).style.backgroundColor = "rgb(106,167,215)";
                }
            }
        }
    }


    function showEnemyBoard(enemyBoard, myTurn){
        if (enemyBoard !== null) {
            for (let i = 0; i < enemyBoard.length; i++){
                //UNUSED
                if (enemyBoard[i].value === 0) {
                    if (myTurn) {
                        document.getElementById("e" + enemyBoard[i].x + enemyBoard[i].y).style.backgroundColor = "rgba(36, 79, 154, 0.9)";                    
                    } else {
                        document.getElementById("e" + enemyBoard[i].x + enemyBoard[i].y).style.backgroundColor = "rgba(36, 79, 154, 0.5)";
                    }
                }
                //HIT
                if (enemyBoard[i].value > 0) {
                    if (myTurn) {
                        document.getElementById("e" + enemyBoard[i].x + enemyBoard[i].y).style.backgroundColor = "rgb(255,2,0)";                        
                    } else {
                        document.getElementById("e" + enemyBoard[i].x + enemyBoard[i].y).style.backgroundColor = "rgba(255,2,0,0.5)";
                    }
                }
                //MISS
                if (enemyBoard[i].value < 0) {
                    if (myTurn) {
                        document.getElementById("e" + enemyBoard[i].x + enemyBoard[i].y).style.backgroundColor = "rgb(106,167,215)";                        
                    } else {
                        document.getElementById("e" + enemyBoard[i].x + enemyBoard[i].y).style.backgroundColor = "rgba(106,167,215,0.5)";
                    }
                }
            }
        } else {
            if (!myTurn) {
                for (let i = 0; i < 10; i++) {
                    for (let j = 0; j < 10; j++) {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(36, 79, 154, 0.5)";
                    }
                }                                  
            }
        }       
    }






    /*function goToGame(){
        clearInterval(interval);
        window.location.replace("#/game");
    }*/







// 0 empty cell
    // 1-4 decks
    // -1 my broken ship
    // -2 miss
    // > 0 enemy board ship
    // < 0 enemy board misses
   /* var playerBoard;
    var enemyBoard;
    var token = document.getElementById("token").innerText;
    var ableToShot;
    var playerShips;
    var enemyShips;

    window.onload = function () {
        //gameOver('w');
        $.ajax({
            type: 'GET',
            url: '/game/init',
            data: "token=" + token + "&newGame=" + true,
            success: function (data) {
                console.log(data);
                playerBoard = data.playerBoard;
                enemyBoard = data.enemyBoard;
                ableToShot = data.myTurn;
                printShips(!ableToShot);
            }
        });
        shotRequest();
    };

    function shotRequest() {
        setInterval(function () {
            $.ajax({
                type: 'GET',
                url: '/game/util',
                data: "token=" + token + "&ableToShot=" + true,
                success: function (data) {
                    playerBoard = data.playerBoard;
                    enemyBoard = data.enemyBoard;
                    ableToShot = data.myTurn;
                    playerShips = data.playerShips;
                    enemyShips = data.enemyShips;
                    shipCounter();
                    printShips(!ableToShot);
                    console.log(data.winnerState);
                    if (data.winnerState === 'w' || data.winnerState === 'l') {
                        gameOver(data.winnerState);
                    }
                },
            });
        }, 500);
    }

    function makeShot(element) {
        if (ableToShot) {
            var x = parseInt(element.id.charAt(1));
            var y = parseInt(element.id.charAt(2));
            if (enemyBoard[x][y] === 0) {
                $.ajax({
                    type: 'POST',
                    url: '/game',
                    data: "token=" + token + "&x=" + x + "&y=" + y,
                    success: function (data) {
                        //console.log(data);
                        playerBoard = data.playerBoard;
                        enemyBoard = data.enemyBoard;
                        ableToShot = data.myTurn;
                        playerShips = data.playerShips;
                        enemyShips = data.enemyShips;
                        printShips(!ableToShot);
                        shipCounter();
                        //console.log(playerShips);
                        //console.log(enemyShips);
                    },
                    error: function (data) {
                        console.log(data);
                    }
                });
            }
        } else {
            alert("It's a turn of your opponent, please wait.");
        }
    }

    function shipCounter() {
        for (var i = 0; i < playerShips.length; i++) {
            document.getElementById("playerShip" + (i + 1) + "Quantity").innerText = "x" + playerShips[i];
            document.getElementById("enemyShip" + (i + 1) + "Quantity").innerText = "x" + enemyShips[i];
        }
    }

    function printShips(isBlocked) {
        for (var i = 0; i < enemyBoard.length; i++) {
            for (var j = 0; j < enemyBoard.length; j++) {
                if (playerBoard[i][j] > 0) {
                    document.getElementById("p" + i + j).style.backgroundColor = "rgb(227, 227, 117)";
                }
                if (playerBoard[i][j] < 0) {
                    if (playerBoard[i][j] >= -4 && playerBoard[i][j] <= -1) {
                        document.getElementById("p" + i + j).style.backgroundColor = "rgb(255,2,0)";
                    } else {
                        document.getElementById("p" + i + j).style.backgroundColor = "rgb(106,167,215)";
                    }
                }
                if (enemyBoard[i][j] > 0) {
                    if (isBlocked) {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(255,2,0,0.5)";
                    } else {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgb(255,2,0)";
                    }
                }
                if (enemyBoard[i][j] === -5) {
                    if (isBlocked) {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(106,167,215,0.5)";
                    } else {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgb(106,167,215)";
                    }
                }
                if (enemyBoard[i][j] === 0) {
                    if (isBlocked) {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(36, 79, 154, 0.5)";
                    } else {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(36, 79, 154, 0.9)";
                    }
                }
            }
        }
    }

    function gameOver(state) {
        document.getElementById("state").innerHTML = (state === 'w') ?
            'Congratulations, you win!': 'You lose, better luck next time!';
        $('.popup').show();
        $('.overlay_popup').show();
        $('.object').show();
    }

    function scoreboard(){
        window.location.replace("/");
    }

    function gotoMenu(){
        window.location.replace("/");
    }*/