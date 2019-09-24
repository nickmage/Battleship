function validation() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    document.getElementById("usernameError").hidden = true;
    document.getElementById("passwordError").hidden = true;
    if (username === ''){
        document.getElementById("usernameError").innerHTML = 'Username is empty';
        document.getElementById("usernameError").hidden = false;
    } else if (username.length < 3){
        document.getElementById("usernameError").innerHTML = 'Username is too short';
        document.getElementById("usernameError").hidden = false;
    } else if (password === ''){
        document.getElementById("passwordError").innerHTML = 'Password is empty';
        document.getElementById("passwordError").hidden = false;
    } else if (password.length < 5){
        document.getElementById("passwordError").innerHTML = 'Password is too short';
        document.getElementById("passwordError").hidden = false;
    } else {
        $.ajax({
            type: 'POST',
            url: '/auth',
            data: "username=" + username + "&password=" + password,
            success: function (data, status, header) {
                var token = header.getResponseHeader('authorization');
                if (token !== null){
                    alert(token);
                    localStorage.setItem('token', token);
                    console.log(parseJwt(token));
                    window.location.href = '#/menu';
                    location.reload();
                } else {
                    alert("User not found");
                }
            }
        });
    }
}
function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
};