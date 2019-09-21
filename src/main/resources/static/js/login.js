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
                $.ajax({
                    type: 'GET',
                    url: '/get_cookie',
                    headers: {'authorization': token},
                    //data: {}, PLAYER NAME PLACE HERE
                    success: function (data, status, header) {
                        window.location.replace(data);
                    }
                });
            }
        });
    }
}