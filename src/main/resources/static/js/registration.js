    function validation(input) {
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        var confirmPassword = document.getElementById("confirmPassword").value;
        document.getElementById("usernameError").hidden = true;
        document.getElementById("passwordError").hidden = true;
        document.getElementById("confirmPasswordError").hidden = true;
        document.getElementById("message").hidden = true;
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
        } else if (confirmPassword === ''){
            document.getElementById("confirmPasswordError").innerHTML = 'Password is empty';
            document.getElementById("confirmPasswordError").hidden = false;
        } else if (confirmPassword.length < 5){
            document.getElementById("confirmPasswordError").innerHTML = 'Password is too short';
            document.getElementById("confirmPasswordError").hidden = false;
        } else if (confirmPassword !== password){
            document.getElementById("confirmPasswordError").innerHTML = 'Password does not match';
            document.getElementById("confirmPasswordError").hidden = false;
        } else {
            send(username, password, confirmPassword);
        }
    }

    function send(username, password, confirmPassword){
        $.ajax({
            type: 'POST',
            url: '/registration',
            data: "username=" + username + "&password=" + password + "&confirmPassword=" + confirmPassword,
            beforeSend: function(){
                $("#message").hide();
            },
            success: function(){
                window.location.href = '#/success';
            },
            statusCode: {
                400: function(){
                    document.getElementById("message").innerHTML = "Username or password is invalid";
                    $("#message").show();
                },
                406: function(){
                    document.getElementById("message").innerHTML = "The user is already exist";
                    $("#message").show();
                }
            }
        });
    }
