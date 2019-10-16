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
                    localStorage.setItem('token', token);
                    window.location.href = '../#/menu';
                    location.reload();
                } else {
                    alert("User not found");
                }
            }
        });
    }
}
