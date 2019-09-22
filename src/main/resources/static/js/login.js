function validation(input) {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    if (username === ''){
        input.setCustomValidity('Username is empty');
    } else if (username.length < 3){
        input.setCustomValidity('Password is too short');
    }else if (password === ''){
        input.setCustomValidity('Password is empty');
    } else if (password < 5){
        input.setCustomValidity('Password is too short');
    } else {
        input.setCustomValidity('');
        $.ajax({
            type: 'POST',
            url: '/auth',
            data: "username=" + username + "&password=" + password,
            success: function (data, status, header) {
                var token = header.getResponseHeader('authorization');
                alert(token);
                localStorage.setItem('token', token);
                console.log(parseJwt(token));
                alert(localStorage.getItem('token'));
                window.location.href = '#/menu';
            },
            error: function () {
                alert("User not found");
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