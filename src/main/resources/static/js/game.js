var token = localStorage.getItem('token');
var roomId = sessionStorage.getItem('roomId');
var playerId = localStorage.getItem('playerId');
var interval;
var enemyBoardArray = [];
var UNUSED = 1;
var MISS = 0;
var HIT = -1;
var myTurn = false;
var init = init();

    function init(){
        fillEnemyBoardArrayWithUnused();
        getBoards();
    }

    function fillEnemyBoardArrayWithUnused(){
        for (let i = 0; i < 10; i++){
            enemyBoardArray[i] = [];
            for (let j = 0; j < 10; j++){
                enemyBoardArray[i][j] = UNUSED;
            }
        }
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
                myTurn = data.myTurn
                console.log(data);
                setPlayerAndEnemyInfoVisible(data.enemyName);
                showPlayerShips(data.playerShips);
                showEnemyShips(data.enemyShips);
                showPlayerBoard(data.playerBoard);
                showEnemyBoard(data.enemyBoard);
                if (!myTurn){
                    interval = setInterval(statusRequest, 1000);
                }
                if (data.winner !== 0){
                    gameOver(data.winner);
                }
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

    function showPlayerShips(playerShips){
        document.getElementById('playerShip1Quantity').innerHTML = 'x' + playerShips.oneDeckShips;
        document.getElementById('playerShip2Quantity').innerHTML = 'x' + playerShips.twoDeckShips;
        document.getElementById('playerShip3Quantity').innerHTML = 'x' + playerShips.threeDeckShips;
        document.getElementById('playerShip4Quantity').innerHTML = 'x' + playerShips.fourDeckShips;
    }

    function showEnemyShips(enemyShips){
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
            if (playerBoard[i].value <= 0) {
                if (playerBoard[i].value >= -4 && playerBoard[i].value <= -1) {
                    document.getElementById("p" + playerBoard[i].x + playerBoard[i].y).style.backgroundColor = "rgb(255,2,0)";
                } else {
                    document.getElementById("p" + playerBoard[i].x + playerBoard[i].y).style.backgroundColor = "rgb(106,167,215)";
                }
            }
        }
    }

    function showEnemyBoard(enemyBoard){
        if (enemyBoard.length !== 0) {
            fillEnemyArray(enemyBoard);
            revealCells();
        }
        else {
            if (!myTurn) {
                for (let i = 0; i < 10; i++) {
                    for (let j = 0; j < 10; j++) {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(36, 79, 154, 0.5)";
                    }
                }
            }
        }
    }

    function fillEnemyArray(enemyBoard){
        for (let i = 0; i < enemyBoard.length; i++){
            enemyBoardArray[enemyBoard[i].x][enemyBoard[i].y] = enemyBoard[i].value;
        }
    }

    function revealCells(){
        for (let i = 0; i < enemyBoardArray.length; i++){
            for (let j = 0; j < enemyBoardArray.length; j++){
                //UNUSED
                if (enemyBoardArray[i][j] === UNUSED) {
                    if (myTurn) {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(36, 79, 154, 0.9)";
                    } else {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(36, 79, 154, 0.5)";
                    }
                }
                //HIT
                if (enemyBoardArray[i][j] === HIT) {
                    if (myTurn) {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgb(255,2,0)";
                    } else {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(255,2,0,0.5)";
                    }
                }
                //MISS
                if (enemyBoardArray[i][j] === MISS) {
                    if (myTurn) {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgb(106,167,215)";
                    } else {
                        document.getElementById("e" + i + j).style.backgroundColor = "rgba(106,167,215,0.5)";
                    }
                }

            }
        }
    }

    function makeShot(element) {
        if (myTurn) {
            var x = parseInt(element.id.charAt(1));
            var y = parseInt(element.id.charAt(2));
            if (enemyBoardArray[x][y] === UNUSED) {
                $.ajax({
                    type: 'POST',
                    url: '/game/shot',
                    headers: {
                        'Authorization':token,
                    },
                    data: "roomId=" + roomId + "&playerId=" + playerId + "&x=" + x + "&y=" + y,
                    success: function (data) {
                        console.log(data);
                        myTurn = data.myTurn;
                        showEnemyShips(data.remainingEnemyShips);
                        showEnemyBoard(data.interactedCells);
                        if (!myTurn) {
                            interval = setInterval(statusRequest, 1000);
                        }
                        if (data.winner !== 0) {
                            gameOver(data.winner);
                        }
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

    function statusRequest() {
        $.ajax({
            type: 'GET',
            url: '/game/status',
            headers: {
                'Authorization':token,
            },
            data: "roomId=" + roomId + "&playerId=" + playerId,
            success: function (data) {
                console.log(data);
                myTurn = data.myTurn;
                showPlayerShips(data.playerShips);
                showPlayerBoard(data.playerBoard);
                revealCells();
                if (data.winner !== 0){
                    gameOver(data.winner);
                }
                if (myTurn){
                    clearInterval(interval);
                }
            },
        });
    }

    function gameOver(state) {
        clearInterval(interval);
        var player = 1;
        document.getElementById("state").innerHTML = (state === player) ?
            'Congratulations, you win!': 'You lose, better luck next time!';
        $('.popup').show();
        $('.overlay_popup').show();
        $('.object').show();
        sessionStorage.removeItem('roomId');
    }

    function scoreboard(){
       window.location.replace("#/scoreboard");
    }

    function gotoMenu(){
        window.location.replace("#/menu");
    }
