var token = localStorage.getItem('token');
var userFromToken = getUsername();

function getUsername(){
    sessionStorage.removeItem('roomId');
    if (localStorage.getItem('username') !== null){
        document.getElementById("user").innerText = localStorage.getItem('username');
    } else {
        let username = parseJwt(token).sub;
        document.getElementById("user").innerText = username;
        localStorage.setItem('username', username);
    }
}

function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
}

function logout(){
    localStorage.removeItem('token');
    if (localStorage.getItem('id') !== null){
        localStorage.removeItem('id');
    }
    if (localStorage.getItem('playerId') !== null){
        localStorage.removeItem('playerId');
    }
    if (localStorage.getItem('username') !== null){
        localStorage.removeItem('username');
    }
    location.href='../';
}

