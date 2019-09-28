var token = localStorage.getItem('token');
var userFromToken = getUsername();
var user = document.getElementById("user").innerText;
var shipOrientation = ['-', 'v', 'v', 'v'];
var selectedShip = [false, false, false, false];
var ids = ["ship1", "ship2", "ship3", "ship4"];
var ships = [4, 3, 2, 1];
var board = [[0, 0, 0, 0, 0, 0, 0, 0, 0, 0],    // 0 represents free cells
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],             // <0 represents cells around a ship with placement restrictions
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],             // 1, 2, 3, 4 represent cells occupied by ships
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],             // 1 for 1 deck ship, 4 for 4 deck ship respectively
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
];
var request = [];

function getUsername(){
    let username = parseJwt(token).sub;
    document.getElementById("user").innerText = username;
    localStorage.setItem('username', username);
};

function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
};

function setStyleForSelectedShip(id, index) {
    for (var i = 0; i < ids.length; i++) {
        if (ids[i] === id) {
            document.getElementById(ids[i]).style.cssText = "border: 4px solid white;";
        } else {
            document.getElementById(ids[i]).style.cssText = "border: 0;";
        }
        selectedShip[i] = i === index;
    }
}

function changeShipOrientation(id) {
    switch (id) {
        case "ship4":
            if (!selectedShip[3]) {
                setStyleForSelectedShip("ship4", 3);
            } else {
                if (shipOrientation[3] === 'v') {
                    document.getElementById("ship4").src = "img/4dh.png";
                    document.getElementById("ship4").height = "25";
                    document.getElementById("ship4").width = "100";
                    shipOrientation[3] = 'h';
                } else {
                    document.getElementById("ship4").src = "img/4dv.png";
                    document.getElementById("ship4").height = "100";
                    document.getElementById("ship4").width = "25";
                    shipOrientation[3] = 'v';
                }
                setStyleForSelectedShip("ship4", 3);
            }
            break;
        case "ship3":
            if (!selectedShip[2]) {
                setStyleForSelectedShip("ship3", 2);
            } else {
                if (shipOrientation[2] === 'v') {
                    document.getElementById("ship3").src = "img/3dh.png";
                    document.getElementById("ship3").height = "25";
                    document.getElementById("ship3").width = "75";
                    shipOrientation[2] = 'h';
                } else {
                    document.getElementById("ship3").src = "img/3dv.png";
                    document.getElementById("ship3").height = "75";
                    document.getElementById("ship3").width = "25";
                    shipOrientation[2] = 'v';
                }
                setStyleForSelectedShip("ship3", 2);
            }
            break;
        case "ship2":
            if (!selectedShip[1]) {
                setStyleForSelectedShip("ship2", 1);
            } else {
                if (shipOrientation[1] === 'v') {
                    document.getElementById("ship2").src = "img/2dh.png";
                    document.getElementById("ship2").height = "25";
                    document.getElementById("ship2").width = "50";
                    shipOrientation[1] = 'h';
                } else {
                    document.getElementById("ship2").src = "img/2dv.png";
                    document.getElementById("ship2").height = "50";
                    document.getElementById("ship2").width = "25";
                    shipOrientation[1] = 'v';
                }
                setStyleForSelectedShip("ship2", 1);
            }
            break;
        case "ship1":
            setStyleForSelectedShip("ship1", 0);
            break;
    }
}

function indexOfSelectedShip() {
    for (var i = 0; i < selectedShip.length; i++) {
        if (selectedShip[i]) {
            return i;
        }
    }
    return -1;
}

//mouseover
document.querySelectorAll('.select').forEach(function (e) {
    e.addEventListener('mouseenter', function () {
        var index = indexOfSelectedShip();
        var i = parseInt(this.id.charAt(1));
        var j = parseInt(this.id.charAt(2));
        if (index >= 0) {
            switch (index) {
                case 0:
                    if (board[i][j] >= 0 && ships[0] > 0)
                        showOneDeckShipPlace(this);
                    break;
                case 1:
                    if (board[i][j] >= 0 && ships[1] > 0) {
                        showTwoDeckShipPlace(this);
                    }
                    break;
                case 2:
                    if (board[i][j] >= 0 && ships[2] > 0) {
                        showThreeDeckShipPlace(this);
                    }
                    break;
                case 3:
                    if (board[i][j] >= 0 && ships[3] > 0) {
                        showFourDeckShipPlace(this);
                    }
                    break;
            }
        }
    })
});

function showOneDeckShipPlace(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (board[i][j] === 0) {
        element.style.backgroundColor = "rgb(77, 117, 108)";
    }
    element.addEventListener('mouseout', function () {
        if (board[i][j] < 1) {
            element.style.backgroundColor = "";
        }
    });
}

function actionOnClick(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    switch (indexOfSelectedShip()) {
        case 0:
            if (board[i][j] === 0) {
                setOneDeckShipOnBoard(element);
            } else if (board[i][j] === 1) {
                removeOneDeckShipFromBoard(element);
            } else if (board[i][j] === 2) {
                removeTwoDeckShipFromBoard(element);
            } else if (board[i][j] === 3) {
                removeThreeDeckShipFromBoard(element);
            } else if (board[i][j] === 4) {
                removeFourDeckShipFromBoard(element);
            }
            break;
        case 1:
            if (board[i][j] === 0) {
                setTwoDeckShipOnBoard(element);
            } else if (board[i][j] === 1) {
                removeOneDeckShipFromBoard(element);
            } else if (board[i][j] === 2) {
                removeTwoDeckShipFromBoard(element);
            } else if (board[i][j] === 3) {
                removeThreeDeckShipFromBoard(element);
            } else if (board[i][j] === 4) {
                removeFourDeckShipFromBoard(element);
            }
            break;
        case 2:
            if (board[i][j] === 0) {
                setThreeDeckShipOnBoard(element);
            } else if (board[i][j] === 1) {
                removeOneDeckShipFromBoard(element);
            } else if (board[i][j] === 2) {
                removeTwoDeckShipFromBoard(element);
            } else if (board[i][j] === 3) {
                removeThreeDeckShipFromBoard(element);
            } else if (board[i][j] === 4) {
                removeFourDeckShipFromBoard(element);
            }
            break;
        case 3:
            if (board[i][j] === 0) {
                setFourDeckShipOnBoard(element);
            } else if (board[i][j] === 1) {
                removeOneDeckShipFromBoard(element);
            } else if (board[i][j] === 2) {
                removeTwoDeckShipFromBoard(element);
            } else if (board[i][j] === 3) {
                removeThreeDeckShipFromBoard(element);
            } else if (board[i][j] === 4) {
                removeFourDeckShipFromBoard(element);
            }
            break;
    }
}

function setOneDeckShipOnBoard(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (board[i][j] === 0 && ships[0] > 0) {
        element.style.backgroundColor = "rgb(227, 227, 117)";
        board[i][j] = 1;
        ships[0]--;
        shipsCounter("ship1Quantity", ships[0]);
        setAreaForOneDeckShip(i, j, -1);
        putShipToRequest(i, j, '-', 1);
    }
}

function removeOneDeckShipFromBoard(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (board[i][j] === 1) {
        element.style.backgroundColor = "";
        board[i][j] = 0;
        ships[0]++;
        shipsCounter("ship1Quantity", ships[0]);
        setAreaForOneDeckShip(i, j, 1);
        removeShipFromRequest(i, j);
    }
}

function setAreaForOneDeckShip(i, j, value) {
    if (i - 1 >= 0) {
        board[i - 1][j] += value;
        if (j - 1 >= 0) {
            board[i - 1][j - 1] += value;
        }
        if (j + 1 < board.length) {
            board[i - 1][j + 1] += value;
        }
    }//top
    if (i + 1 < board.length) {
        board[i + 1][j] += value;
        if (j + 1 < board.length) {
            board[i + 1][j + 1] += value;
        }
        if (j - 1 >= 0) {
            board[i + 1][j - 1] += value;
        }
    }//bottom
    if (j - 1 >= 0) {
        board[i][j - 1] += value;
    }//left
    if (j + 1 < board.length) {
        board[i][j + 1] += value;
    } //right
}

function showTwoDeckShipPlace(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (shipOrientation[1] === 'v' && (i + 1 < board.length)) {
        var nextVCell = element.id.charAt(0) + (i + 1) + j;
        if (board[i][j] === 0 && board[i + 1][j] === 0) {
            element.style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(nextVCell).style.backgroundColor = "rgb(77, 117, 108)";
        }
        element.addEventListener('mouseout', function () {
            if (board[i][j] < 1) {
                document.getElementById(element.id).style.backgroundColor = "";
            }
            if (board[i + 1][j] < 1) {
                document.getElementById(nextVCell).style.backgroundColor = "";
            }
        })
    } else if (shipOrientation[1] === 'h' && (j + 1 < board.length)) {
        var nextHCell = element.id.substring(0, 2) + (j + 1);
        if (board[i][j] === 0 && board[i][j + 1] === 0) {
            element.style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(nextHCell).style.backgroundColor = "rgb(77, 117, 108)";
        }
        element.addEventListener('mouseout', function () {
            if (board[i][j] < 1) {
                document.getElementById(element.id).style.backgroundColor = "";
            }
            if (board[i][j + 1] < 1) {
                document.getElementById(nextHCell).style.backgroundColor = "";
            }
        })
    }
}

function setTwoDeckShipOnBoard(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (shipOrientation[1] === 'v' && (i + 1 < board.length) && ships[1] > 0) {
        if (board[i][j] === 0 && board[i + 1][j] === 0) {
            var nextVCell = element.id.charAt(0) + (i + 1) + j;
            element.style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(nextVCell).style.backgroundColor = "rgb(227, 227, 117)";
            board[i][j] = 2;
            board[i + 1][j] = 2;
            ships[1]--;
            shipsCounter("ship2Quantity", ships[1]);
            setAreaForTwoDeckShip(i, j, i + 1, j, -1, '-');
            putShipToRequest(i, j, 'v', 2);
        }
    } else if (shipOrientation[1] === 'h' && (j + 1 < board.length) && ships[1] > 0) {
        if (board[i][j] === 0 && board[i][j + 1] === 0 && ships[1] > 0) {
            var nextHCell = element.id.substring(0, 2) + (j + 1);
            element.style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(nextHCell).style.backgroundColor = "rgb(227, 227, 117)";
            board[i][j] = 2;
            board[i][j + 1] = 2;
            ships[1]--;
            shipsCounter("ship2Quantity", ships[1]);
            setAreaForTwoDeckShip(i, j, i, j + 1, -1, '-');
            putShipToRequest(i, j, 'h', 2);
        }
    }
}

function removeTwoDeckShipFromBoard(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (i - 1 >= 0 && board[i - 1][j] === 2) {
        var nextCellAbove = element.id.charAt(0) + (i - 1) + j;
        element.style.backgroundColor = "";
        document.getElementById(nextCellAbove).style.backgroundColor = "";
        board[i][j] = 0;
        board[i - 1][j] = 0;
        ships[1]++;
        shipsCounter("ship2Quantity", ships[1]);
        setAreaForTwoDeckShip(i - 1, j, i, j, 1, 'v');
        removeShipFromRequest(i - 1, j);
    } else if (i + 1 < board.length && board[i + 1][j] === 2) {
        var nextCellDown = element.id.charAt(0) + (i + 1) + j;
        element.style.backgroundColor = "";
        document.getElementById(nextCellDown).style.backgroundColor = "";
        board[i][j] = 0;
        board[i + 1][j] = 0;
        ships[1]++;
        shipsCounter("ship2Quantity", ships[1]);
        setAreaForTwoDeckShip(i, j, i + 1, j, 1, 'v');
        removeShipFromRequest(i, j);
    } else if (j + 1 < board.length && board[i][j + 1] === 2) {
        var nextCellRights = element.id.substring(0, 2) + (j + 1);
        element.style.backgroundColor = "";
        document.getElementById(nextCellRights).style.backgroundColor = "";
        board[i][j] = 0;
        board[i][j + 1] = 0;
        ships[1]++;
        shipsCounter("ship2Quantity", ships[1]);
        setAreaForTwoDeckShip(i, j, i, j + 1, 1, 'h');
        removeShipFromRequest(i, j);
    } else if (j - 1 >= 0 && board[i][j - 1] === 2) {
        var nextCellLefts = element.id.substring(0, 2) + (j - 1);
        element.style.backgroundColor = "";
        document.getElementById(nextCellLefts).style.backgroundColor = "";
        board[i][j] = 0;
        board[i][j - 1] = 0;
        ships[1]++;
        shipsCounter("ship2Quantity", ships[1]);
        setAreaForTwoDeckShip(i, j - 1, i, j, 1, 'h');
        removeShipFromRequest(i, j - 1);
    }
}

function setAreaForTwoDeckShip(i1, j1, i2, j2, value, orientation) {
    if (orientation === '-') {
        if (shipOrientation[1] === 'v') { //i2 j2 is always bottom cell
            changeValueOfVerticalTwoDeckShipArea(i1, j1, i2, j2, value);
        } else if (shipOrientation[1] === 'h') { //i2 j2 is always right cell
            changeValueOfHorizontalTwoDeckShipArea(i1, j1, i2, j2, value);
        }
    } else if (orientation === 'v') {
        changeValueOfVerticalTwoDeckShipArea(i1, j1, i2, j2, value);
    } else if (orientation === 'h') {
        changeValueOfHorizontalTwoDeckShipArea(i1, j1, i2, j2, value);
    }
}

function changeValueOfVerticalTwoDeckShipArea(i1, j1, i2, j2, value) {
    if (i1 - 1 >= 0) {
        board[i1 - 1][j1] += value;
    }//top
    if (i2 + 1 < board.length) {
        board[i2 + 1][j2] += value;
    } //bottom
    if (j1 - 1 >= 0) {
        board[i1][j1 - 1] += value;
        board[i2][j2 - 1] += value;
        if (i1 - 1 >= 0) {
            board[i1 - 1][j1 - 1] += value;
        }
        if (i2 + 1 < board.length) {
            board[i2 + 1][j2 - 1] += value;
        }
    }//left
    if (j1 + 1 < board.length) {
        board[i1][j1 + 1] += value;
        board[i2][j2 + 1] += value;
        if (i1 - 1 >= 0) {
            board[i1 - 1][j1 + 1] += value;
        }
        if (i2 + 1 < board.length) {
            board[i2 + 1][j2 + 1] += value;
        }
    }//right
}

function changeValueOfHorizontalTwoDeckShipArea(i1, j1, i2, j2, value) {
    if (i1 - 1 >= 0) {
        board[i1 - 1][j1] += value;
        board[i2 - 1][j2] += value;
        if (j1 - 1 >= 0) {
            board[i1 - 1][j1 - 1] += value;
        }
        if (j2 + 1 < board.length) {
            board[i2 - 1][j2 + 1] += value;
        }
    }//top
    if (i1 + 1 < board.length) {
        board[i1 + 1][j1] += value;
        board[i2 + 1][j2] += value;
        if (j1 - 1 >= 0) {
            board[i1 + 1][j1 - 1] += value;
        }
        if (j2 + 1 < board.length) {
            board[i2 + 1][j2 + 1] += value;
        }
    }//bottom
    if (j1 - 1 >= 0) {
        board[i1][j1 - 1] += value;
    }//left
    if (j2 + 1 < board.length) {
        board[i2][j2 + 1] += value;
    } //right
}

function showThreeDeckShipPlace(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (shipOrientation[2] === 'v' && (i + 2 < board.length)) {
        var middleVCell = element.id.charAt(0) + (i + 1) + j;
        var bottomVCell = element.id.charAt(0) + (i + 2) + j;
        if (board[i][j] === 0 && board[i + 1][j] === 0 && board[i + 2][j] === 0) {
            element.style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(middleVCell).style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(bottomVCell).style.backgroundColor = "rgb(77, 117, 108)";
        }
        element.addEventListener('mouseout', function () {
            if (board[i][j] < 1) {
                document.getElementById(element.id).style.backgroundColor = "";
            }
            if (board[i + 1][j] < 1) {
                document.getElementById(middleVCell).style.backgroundColor = "";
            }
            if (board[i + 2][j] < 1) {
                document.getElementById(bottomVCell).style.backgroundColor = "";
            }
        })
    } else if (shipOrientation[2] === 'h' && (j + 2 < board.length)) {
        var middleHCell = element.id.substring(0, 2) + (j + 1);
        var rightHCell = element.id.substring(0, 2) + (j + 2);
        if (board[i][j] === 0 && board[i][j + 1] === 0 && board[i][j + 2] === 0) {
            element.style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(middleHCell).style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(rightHCell).style.backgroundColor = "rgb(77, 117, 108)";
        }
        element.addEventListener('mouseout', function () {
            if (board[i][j] < 1) {
                document.getElementById(element.id).style.backgroundColor = "";
            }
            if (board[i][j + 1] < 1) {
                document.getElementById(middleHCell).style.backgroundColor = "";
            }
            if (board[i][j + 2] < 1) {
                document.getElementById(rightHCell).style.backgroundColor = "";
            }
        })
    }
}

function setThreeDeckShipOnBoard(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (shipOrientation[2] === 'v' && (i + 2 < board.length) && ships[2] > 0) {
        if (board[i][j] === 0 && board[i + 1][j] === 0 && board[i + 2][j] === 0) {
            var middleVCell = element.id.charAt(0) + (i + 1) + j;
            var bottomVCell = element.id.charAt(0) + (i + 2) + j;
            element.style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(middleVCell).style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(bottomVCell).style.backgroundColor = "rgb(227, 227, 117)";
            board[i][j] = 3;
            board[i + 1][j] = 3;
            board[i + 2][j] = 3;
            ships[2]--;
            shipsCounter("ship3Quantity", ships[2]);
            setAreaForThreeDeckShip(i, j, i + 1, j, i + 2, j, -1, '-');
            putShipToRequest(i, j, 'v', 3);
        }
    } else if (shipOrientation[2] === 'h' && (j + 2 < board.length) && ships[2] > 0) {
        if (board[i][j] === 0 && board[i][j + 1] === 0 && board[i][j + 2] === 0) {
            var middleHCell = element.id.substring(0, 2) + (j + 1);
            var rightHCell = element.id.substring(0, 2) + (j + 2);
            element.style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(middleHCell).style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(rightHCell).style.backgroundColor = "rgb(227, 227, 117)";
            board[i][j] = 3;
            board[i][j + 1] = 3;
            board[i][j + 2] = 3;
            ships[2]--;
            shipsCounter("ship3Quantity", ships[2]);
            setAreaForThreeDeckShip(i, j, i, j + 1, i, j + 2, -1, '-');
            putShipToRequest(i, j, 'h', 3);
        }
    }
}

function setAreaForThreeDeckShip(i1, j1, i2, j2, i3, j3, value, orientation) {
    if (orientation === '-') {
        if (shipOrientation[2] === 'v') { //i3 j3 is always bottom cell
            changeValueOfVerticalThreeDeckShipArea(i1, j1, i2, j2, i3, j3, value);
        } else if (shipOrientation[2] === 'h') { //i3 j3 is always right cell
            changeValueOfHorizontalThreeDeckShipArea(i1, j1, i2, j2, i3, j3, value);
        }
    } else if (orientation === 'v') {
        changeValueOfVerticalThreeDeckShipArea(i1, j1, i2, j2, i3, j3, value);
    } else if (orientation === 'h') {
        changeValueOfHorizontalThreeDeckShipArea(i1, j1, i2, j2, i3, j3, value);
    }
}

function changeValueOfVerticalThreeDeckShipArea(i1, j1, i2, j2, i3, j3, value) {
    if (i1 - 1 >= 0) {
        board[i1 - 1][j1] += value;
        if (j1 - 1 >= 0) {
            board[i1 - 1][j1 - 1] += value;
        }
        if (j1 + 1 < board.length) {
            board[i1 - 1][j1 + 1] += value;
        }
    }//top
    if (i3 + 1 < board.length) {
        board[i3 + 1][j3] += value;
        if (j3 - 1 >= 0) {
            board[i3 + 1][j3 - 1] += value;
        }
        if (j3 + 1 < board.length) {
            board[i3 + 1][j3 + 1] += value;
        }
    }//bottom
    if (j1 - 1 >= 0) {
        board[i1][j1 - 1] += value;
        board[i2][j2 - 1] += value;
        board[i3][j3 - 1] += value;
    }//left
    if (j1 + 1 < board.length) {
        board[i1][j1 + 1] += value;
        board[i2][j2 + 1] += value;
        board[i3][j3 + 1] += value;
    }//right
}

function changeValueOfHorizontalThreeDeckShipArea(i1, j1, i2, j2, i3, j3, value) {
    if (i1 - 1 >= 0) {
        board[i1 - 1][j1] += value;
        board[i2 - 1][j2] += value;
        board[i3 - 1][j3] += value;
    }//top
    if (i1 + 1 < board.length) {
        board[i1 + 1][j1] += value;
        board[i2 + 1][j2] += value;
        board[i3 + 1][j3] += value;
    }//bottom

    if (j1 - 1 >= 0) {
        board[i1][j1 - 1] += value;
        if (i1 - 1 >= 0) {
            board[i1 - 1][j1 - 1] += value;
        }
        if (i1 + 1 < board.length) {
            board[i1 + 1][j1 - 1] += value;
        }
    }//left
    if (j3 + 1 < board.length) {
        board[i3][j3 + 1] += value;
        if (i3 - 1 >= 0) {
            board[i3 - 1][j3 + 1] += value;
        }
        if (i3 + 1 < board.length) {
            board[i3 + 1][j3 + 1] += value;
        }
    } //right
}

function removeThreeDeckShipFromBoard(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    var orientation = getMultiDeckShipOrientation(i, j, 3);
    if (orientation === 'v') {
        findTopCellForThreeDeckShip(i, j, element);
    } else if (orientation === 'h') {
        findLeftCellForThreeDeckShip(i, j, element);
    }
}

function getMultiDeckShipOrientation(i, j, decks) {
    if (i + 1 < board.length && board[i + 1][j] === decks) {
        return 'v';
    }
    if (i - 1 >= 0 && board[i - 1][j] === decks) {
        return 'v';
    }
    if (j + 1 < board.length && board[i][j + 1] === decks) {
        return 'h';
    }
    if (j - 1 >= 0 && board[i][j - 1] === decks) {
        return 'h';
    }
}

function findTopCellForThreeDeckShip(i, j, element) {
    var topVCell;
    var middleVCell;
    var bottomVCell;
    if (i - 2 >= 0 && board[i - 2][j] === 3) {
        topVCell = element.id.charAt(0) + (i - 2) + j;
        middleVCell = element.id.charAt(0) + (i - 1) + j;
        bottomVCell = element.id.charAt(0) + i + j;
        board[i - 2][j] = 0;
        board[i - 1][j] = 0;
        board[i][j] = 0;
        ships[2]++;
        shipsCounter("ship3Quantity", ships[2]);
        setAreaForThreeDeckShip(i - 2, j, i - 1, j, i, j, 1, 'v');
        removeShipFromRequest(i - 2, j);
    } else if (i - 1 >= 0 && board[i - 1][j] === 3) {
        topVCell = element.id.charAt(0) + (i - 1) + j;
        middleVCell = element.id.charAt(0) + i + j;
        bottomVCell = element.id.charAt(0) + (i + 1) + j;
        board[i - 1][j] = 0;
        board[i][j] = 0;
        board[i + 1][j] = 0;
        ships[2]++;
        shipsCounter("ship3Quantity", ships[2]);
        setAreaForThreeDeckShip(i - 1, j, i, j, i + 1, j, 1, 'v');
        removeShipFromRequest(i - 1, j);
    } else {
        topVCell = element.id.charAt(0) + i + j;
        middleVCell = element.id.charAt(0) + (i + 1) + j;
        bottomVCell = element.id.charAt(0) + (i + 2) + j;
        board[i][j] = 0;
        board[i + 1][j] = 0;
        board[i + 2][j] = 0;
        ships[2]++;
        shipsCounter("ship3Quantity", ships[2]);
        setAreaForThreeDeckShip(i, j, i + 1, j, i + 2, j, 1, 'v');
        removeShipFromRequest(i, j);
    }
    document.getElementById(topVCell).style.backgroundColor = "";
    document.getElementById(middleVCell).style.backgroundColor = "";
    document.getElementById(bottomVCell).style.backgroundColor = "";
}

function findLeftCellForThreeDeckShip(i, j, element) {
    var leftHCell;
    var middleHCell;
    var rightHCell;
    if (j - 2 >= 0 && board[i][j - 2] === 3) {
        leftHCell = element.id.charAt(0) + i + (j - 2);
        middleHCell = element.id.charAt(0) + i + (j - 1);
        rightHCell = element.id.charAt(0) + i + j;
        board[i][j - 2] = 0;
        board[i][j - 1] = 0;
        board[i][j] = 0;
        ships[2]++;
        shipsCounter("ship3Quantity", ships[2]);
        setAreaForThreeDeckShip(i, j - 2, i, j - 1, i, j, 1, 'h');
        removeShipFromRequest(i, j - 2);
    } else if (j - 1 >= 0 && board[i][j - 1] === 3) {
        leftHCell = element.id.charAt(0) + i + (j - 1);
        middleHCell = element.id.charAt(0) + i + j;
        rightHCell = element.id.charAt(0) + i + (j + 1);
        board[i][j - 1] = 0;
        board[i][j] = 0;
        board[i][j + 1] = 0;
        ships[2]++;
        shipsCounter("ship3Quantity", ships[2]);
        setAreaForThreeDeckShip(i, j - 1, i, j, i, j + 1, 1, 'h');
        removeShipFromRequest(i, j - 1);
    } else {
        leftHCell = element.id.charAt(0) + i + j;
        middleHCell = element.id.charAt(0) + i + (j + 1);
        rightHCell = element.id.charAt(0) + i + (j + 2);
        board[i][j] = 0;
        board[i][j + 1] = 0;
        board[i][j + 2] = 0;
        ships[2]++;
        shipsCounter("ship3Quantity", ships[2]);
        setAreaForThreeDeckShip(i, j, i, j + 1, i, j + 2, 1, 'h');
        removeShipFromRequest(i, j);
    }
    document.getElementById(leftHCell).style.backgroundColor = "";
    document.getElementById(middleHCell).style.backgroundColor = "";
    document.getElementById(rightHCell).style.backgroundColor = "";
}

function showFourDeckShipPlace(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (shipOrientation[3] === 'v' && (i + 3 < board.length)) {
        var middleTopCell = element.id.charAt(0) + (i + 1) + j;
        var middleBottomCell = element.id.charAt(0) + (i + 2) + j;
        var bottomVCell = element.id.charAt(0) + (i + 3) + j;
        if (board[i][j] === 0 && board[i + 1][j] === 0 && board[i + 2][j] === 0 && board[i + 3][j] === 0) {
            element.style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(middleTopCell).style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(middleBottomCell).style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(bottomVCell).style.backgroundColor = "rgb(77, 117, 108)";
        }
        element.addEventListener('mouseout', function () {
            if (board[i][j] < 1) {
                document.getElementById(element.id).style.backgroundColor = "";
            }
            if (board[i + 1][j] < 1) {
                document.getElementById(middleTopCell).style.backgroundColor = "";
            }
            if (board[i + 2][j] < 1) {
                document.getElementById(middleBottomCell).style.backgroundColor = "";
            }
            if (board[i + 3][j] < 1) {
                document.getElementById(bottomVCell).style.backgroundColor = "";
            }
        })
    } else if (shipOrientation[3] === 'h' && (j + 3 < board.length)) {
        var middleLeftCell = element.id.substring(0, 2) + (j + 1);
        var middleRightCell = element.id.substring(0, 2) + (j + 2);
        var rightHCell = element.id.substring(0, 2) + (j + 3);
        if (board[i][j] === 0 && board[i][j + 1] === 0 && board[i][j + 2] === 0 && board[i][j + 3] === 0) {
            element.style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(middleLeftCell).style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(middleRightCell).style.backgroundColor = "rgb(77, 117, 108)";
            document.getElementById(rightHCell).style.backgroundColor = "rgb(77, 117, 108)";
        }
        element.addEventListener('mouseout', function () {
            if (board[i][j] < 1) {
                document.getElementById(element.id).style.backgroundColor = "";
            }
            if (board[i][j + 1] < 1) {
                document.getElementById(middleLeftCell).style.backgroundColor = "";
            }
            if (board[i][j + 2] < 1) {
                document.getElementById(middleRightCell).style.backgroundColor = "";
            }
            if (board[i][j + 3] < 1) {
                document.getElementById(rightHCell).style.backgroundColor = "";
            }
        })
    }
}

function setFourDeckShipOnBoard(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    if (shipOrientation[3] === 'v' && (i + 3 < board.length) && ships[3] > 0) {
        if (board[i][j] === 0 && board[i + 1][j] === 0 && board[i + 2][j] === 0 && board[i + 3][j] === 0) {
            var middleTopCell = element.id.charAt(0) + (i + 1) + j;
            var middleBottomCell = element.id.charAt(0) + (i + 2) + j;
            var bottomVCell = element.id.charAt(0) + (i + 3) + j;
            element.style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(middleTopCell).style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(middleBottomCell).style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(bottomVCell).style.backgroundColor = "rgb(227, 227, 117)";
            board[i][j] = 4;
            board[i + 1][j] = 4;
            board[i + 2][j] = 4;
            board[i + 3][j] = 4;
            ships[3]--;
            shipsCounter("ship4Quantity", ships[3]);
            setAreaForFourDeckShip(i, j, i + 1, j, i + 2, j, i + 3, j, -1, '-');
            putShipToRequest(i, j, 'v', 4);
        }
    } else if (shipOrientation[3] === 'h' && (j + 3 < board.length) && ships[3] > 0) {
        if (board[i][j] === 0 && board[i][j + 1] === 0 && board[i][j + 2] === 0 && board[i][j + 3] === 0) {
            var middleLeftCell = element.id.substring(0, 2) + (j + 1);
            var middleRightCell = element.id.substring(0, 2) + (j + 2);
            var rightHCell = element.id.substring(0, 2) + (j + 3);
            element.style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(middleLeftCell).style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(middleRightCell).style.backgroundColor = "rgb(227, 227, 117)";
            document.getElementById(rightHCell).style.backgroundColor = "rgb(227, 227, 117)";
            board[i][j] = 4;
            board[i][j + 1] = 4;
            board[i][j + 2] = 4;
            board[i][j + 3] = 4;
            ships[3]--;
            shipsCounter("ship4Quantity", ships[3]);
            setAreaForFourDeckShip(i, j, i, j + 1, i, j + 2, i, j + 3, -1, '-');
            putShipToRequest(i, j, 'h', 4);
        }
    }
}

function setAreaForFourDeckShip(i1, j1, i2, j2, i3, j3, i4, j4, value, orientation) {
    if (orientation === '-') {
        if (shipOrientation[3] === 'v') { //i4 j4 is always bottom cell
            changeValueOfVerticalFourDeckShipArea(i1, j1, i2, j2, i3, j3, i4, j4, value);
        } else if (shipOrientation[3] === 'h') { //i4 j4 is always right cell
            changeValueOfHorizontalFourDeckShipArea(i1, j1, i2, j2, i3, j3, i4, j4, value);
        }
    } else if (orientation === 'v') {
        changeValueOfVerticalFourDeckShipArea(i1, j1, i2, j2, i3, j3, i4, j4, value);
    } else if (orientation === 'h') {
        changeValueOfHorizontalFourDeckShipArea(i1, j1, i2, j2, i3, j3, i4, j4, value);
    }
}

function changeValueOfVerticalFourDeckShipArea(i1, j1, i2, j2, i3, j3, i4, j4, value) {
    if (i1 - 1 >= 0) {
        board[i1 - 1][j1] += value;
        if (j1 - 1 >= 0) {
            board[i1 - 1][j1 - 1] += value;
        }
        if (j1 + 1 < board.length) {
            board[i1 - 1][j1 + 1] += value;
        }
    }//top
    if (i4 + 1 < board.length) {
        board[i4 + 1][j4] += value;
        if (j4 - 1 >= 0) {
            board[i4 + 1][j4 - 1] += value;
        }
        if (j4 + 1 < board.length) {
            board[i4 + 1][j4 + 1] += value;
        }
    }//bottom
    if (j1 - 1 >= 0) {
        board[i1][j1 - 1] += value;
        board[i2][j2 - 1] += value;
        board[i3][j3 - 1] += value;
        board[i4][j4 - 1] += value;
    }//left
    if (j1 + 1 < board.length) {
        board[i1][j1 + 1] += value;
        board[i2][j2 + 1] += value;
        board[i3][j3 + 1] += value;
        board[i4][j4 + 1] += value;
    }//right
}

function changeValueOfHorizontalFourDeckShipArea(i1, j1, i2, j2, i3, j3, i4, j4, value) {
    if (i1 - 1 >= 0) {
        board[i1 - 1][j1] += value;
        board[i2 - 1][j2] += value;
        board[i3 - 1][j3] += value;
        board[i4 - 1][j4] += value;
    }//top
    if (i1 + 1 < board.length) {
        board[i1 + 1][j1] += value;
        board[i2 + 1][j2] += value;
        board[i3 + 1][j3] += value;
        board[i4 + 1][j4] += value;
    }//bottom
    if (j1 - 1 >= 0) {
        board[i1][j1 - 1] += value;
        if (i1 - 1 >= 0) {
            board[i1 - 1][j1 - 1] += value;
        }
        if (i1 + 1 < board.length) {
            board[i1 + 1][j1 - 1] += value;
        }
    }//left
    if (j4 + 1 < board.length) {
        board[i4][j4 + 1] += value;
        if (i4 - 1 >= 0) {
            board[i4 - 1][j4 + 1] += value;
        }
        if (i4 + 1 < board.length) {
            board[i4 + 1][j4 + 1] += value;
        }
    } //right
}

function removeFourDeckShipFromBoard(element) {
    var i = parseInt(element.id.charAt(1));
    var j = parseInt(element.id.charAt(2));
    var orientation = getMultiDeckShipOrientation(i, j, 4);
    if (orientation === 'v') {
        findTopCellForFourDeckShip(i, j, element);
    } else if (orientation === 'h') {
        findLeftCellForFourDeckShip(i, j, element);
    }
}

function findTopCellForFourDeckShip(i, j, element) {
    var topVCell;
    var middleTopCell;
    var middleBottomCell;
    var bottomVCell;
    if (i - 3 >= 0 && board[i - 3][j] === 4) { //bottom selected
        topVCell = element.id.charAt(0) + (i - 3) + j;
        middleTopCell = element.id.charAt(0) + (i - 2) + j;
        middleBottomCell = element.id.charAt(0) + (i - 1) + j;
        bottomVCell = element.id.charAt(0) + i + j;
        board[i - 3][j] = 0;
        board[i - 2][j] = 0;
        board[i - 1][j] = 0;
        board[i][j] = 0;
        ships[3]++;
        shipsCounter("ship4Quantity", ships[3]);
        setAreaForFourDeckShip(i - 3, j, i - 2, j, i - 1, j, i, j, 1, 'v');
        removeShipFromRequest(i - 3, j);
    } else if (i - 2 >= 0 && board[i - 2][j] === 4) {
        topVCell = element.id.charAt(0) + (i - 2) + j;
        middleTopCell = element.id.charAt(0) + (i - 1) + j;
        middleBottomCell = element.id.charAt(0) + i + j;
        bottomVCell = element.id.charAt(0) + (i + 1) + j;
        board[i - 2][j] = 0;
        board[i - 1][j] = 0;
        board[i][j] = 0;
        board[i + 1][j] = 0;
        ships[3]++;
        shipsCounter("ship4Quantity", ships[3]);
        setAreaForFourDeckShip(i - 2, j, i - 1, j, i, j, i + 1, j, 1, 'v');
        removeShipFromRequest(i - 2, j);
    } else if (i - 1 >= 0 && board[i - 1][j] === 4) {
        topVCell = element.id.charAt(0) + (i - 1) + j;
        middleTopCell = element.id.charAt(0) + i + j;
        middleBottomCell = element.id.charAt(0) + (i + 1) + j;
        bottomVCell = element.id.charAt(0) + (i + 2) + j;
        board[i - 1][j] = 0;
        board[i][j] = 0;
        board[i + 1][j] = 0;
        board[i + 2][j] = 0;
        ships[3]++;
        shipsCounter("ship4Quantity", ships[3]);
        setAreaForFourDeckShip(i - 1, j, i, j, i + 1, j, i + 2, j, 1, 'v');
        removeShipFromRequest(i - 1, j);
    } else {
        topVCell = element.id.charAt(0) + i + j;
        middleTopCell = element.id.charAt(0) + (i + 1) + j;
        middleBottomCell = element.id.charAt(0) + (i + 2) + j;
        bottomVCell = element.id.charAt(0) + (i + 3) + j;
        board[i][j] = 0;
        board[i + 1][j] = 0;
        board[i + 2][j] = 0;
        board[i + 3][j] = 0;
        ships[3]++;
        shipsCounter("ship4Quantity", ships[3]);
        setAreaForFourDeckShip(i, j, i + 1, j, i + 2, j, i + 3, j, 1, 'v');
        removeShipFromRequest(i, j);
    }
    document.getElementById(topVCell).style.backgroundColor = "";
    document.getElementById(middleTopCell).style.backgroundColor = "";
    document.getElementById(middleBottomCell).style.backgroundColor = "";
    document.getElementById(bottomVCell).style.backgroundColor = "";
}

function findLeftCellForFourDeckShip(i, j, element) {
    var leftHCell;
    var middleLeftCell;
    var middleRightCell;
    var rightHCell;
    if (j - 3 >= 0 && board[i][j - 3] === 4) {
        leftHCell = element.id.charAt(0) + i + (j - 3);
        middleLeftCell = element.id.charAt(0) + i + (j - 2);
        middleRightCell = element.id.charAt(0) + i + (j - 1);
        rightHCell = element.id.charAt(0) + i + j;
        board[i][j - 3] = 0;
        board[i][j - 2] = 0;
        board[i][j - 1] = 0;
        board[i][j] = 0;
        ships[3]++;
        shipsCounter("ship4Quantity", ships[3]);
        setAreaForFourDeckShip(i, j - 3, i, j - 2, i, j - 1, i, j, 1, 'h');
        removeShipFromRequest(i, j - 3);
    } else if (j - 2 >= 0 && board[i][j - 2] === 4) {
        leftHCell = element.id.charAt(0) + i + (j - 2);
        middleLeftCell = element.id.charAt(0) + i + (j - 1);
        middleRightCell = element.id.charAt(0) + i + j;
        rightHCell = element.id.charAt(0) + i + (j + 1);
        board[i][j - 2] = 0;
        board[i][j - 1] = 0;
        board[i][j] = 0;
        board[i][j + 1] = 0;
        ships[3]++;
        shipsCounter("ship4Quantity", ships[3]);
        setAreaForFourDeckShip(i, j - 2, i, j - 1, i, j, i, j + 1, 1, 'h');
        removeShipFromRequest(i, j - 2);
    } else if (j - 1 >= 0 && board[i][j - 1] === 4) {
        leftHCell = element.id.charAt(0) + i + (j - 1);
        middleLeftCell = element.id.charAt(0) + i + j;
        middleRightCell = element.id.charAt(0) + i + (j + 1);
        rightHCell = element.id.charAt(0) + i + (j + 2);
        board[i][j - 1] = 0;
        board[i][j] = 0;
        board[i][j + 1] = 0;
        board[i][j + 2] = 0;
        ships[3]++;
        shipsCounter("ship4Quantity", ships[3]);
        setAreaForFourDeckShip(i, j - 1, i, j, i, j + 1, i, j + 2, 1, 'h');
        removeShipFromRequest(i, j - 1);
    } else {
        leftHCell = element.id.charAt(0) + i + j;
        middleLeftCell = element.id.charAt(0) + i + (j + 1);
        middleRightCell = element.id.charAt(0) + i + (j + 2);
        rightHCell = element.id.charAt(0) + i + (j + 3);
        board[i][j] = 0;
        board[i][j + 1] = 0;
        board[i][j + 2] = 0;
        board[i][j + 3] = 0;
        ships[3]++;
        shipsCounter("ship4Quantity", ships[3]);
        setAreaForFourDeckShip(i, j, i, j + 1, i, j + 2, i, j + 3, 1, 'h');
        removeShipFromRequest(i, j);
    }
    document.getElementById(leftHCell).style.backgroundColor = "";
    document.getElementById(middleLeftCell).style.backgroundColor = "";
    document.getElementById(middleRightCell).style.backgroundColor = "";
    document.getElementById(rightHCell).style.backgroundColor = "";
}

function shipsCounter(id, value) {
    document.getElementById(id).innerHTML = "x" + value;
    sendBoardButtonLock();
}

function sendBoardButtonLock() {
    document.getElementById("sendBoard").disabled = !shipsAreOver();
}

function shipsAreOver() {
    for (var i = 0; i < ships.length; i++) {
        if (ships[i] > 0) {
            return false;
        }
    }
    return true;
}

function boardValidation() {
    if (quantityCheck()) {
        sendBoard();
    } else {
        alert('Board is invalid, please reload the page and try again.');
    }
}

function quantityCheck() {
    var totalSelected = 0;
    for (var i = 0; i < board.length; i++) {
        for (var j = 0; j < board.length; j++) {
            if (board[i][j] > 0) {
                totalSelected++;
            }
        }
    }
    return totalSelected === 20;
}

function putShipToRequest(x, y, orientation, deckType){
    request.push({x:x, y:y, orientation:orientation, deckType:deckType});
}

function removeShipFromRequest(x, y){
    let length = request.length;
    for(i = 0; i < length; i++){
        if (request[i].x == x && request[i].y == y){
            request.splice(i, 1);
            break;
        }
    }
}

function sendBoard() {
    $.ajax({
        type: 'POST',
        url: '/start',
        headers: {
            'Authorization':token,
            'Username':user
        },
        contentType: 'application/json',
        data: JSON.stringify(request),
        statusCode: {
            200: function (data) {
                console.log(data);
                localStorage.setItem('playerId', data.playerId);
                sessionStorage.setItem('roomId', data.roomId);
                window.location.replace("#/lobby");
            },
            400: function (data) {
                console.log(data);
            }
        }
    });
}

function getToLobby(){
    window.location.href = "#/lobby";
}



/*function sendBoard1() {
      $.ajax({
          type: 'POST',
          url: '/start',
          headers: {
              'Authorization':token,
              'Username':user
          },
          contentType: 'application/json',
          data: JSON.stringify(request),
          statusCode: {
              200: function (data) {
                  setId(data.responseText);
                  postToMatchmaking(data.responseText);
                  //getToLobby(uuid);
              },
              400: function (data) {
                  alert(data.responseText);
                  alert("An error has occurred (the board is invalid)! Please try again and send a valid board.");
              }
          }
      });
  }

  function postToMatchmaking(id){
     $.ajax({
         type: 'POST',
         url: '/start/matchmaking',
         headers: {
             'Authorization':token,
             'Username':user
         },
         data: "playerId=" + localStorage.getItem('id'),
         statusCode: {
             200: function (data) {
             console.log(data);
                 alert(data.responseText);
             },
             400: function (data) {
                 alert(data);
             }
         }
     });
  }*/