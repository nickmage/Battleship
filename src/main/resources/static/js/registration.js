    function validation(input) {
        var username = document.getElementById("username").value;
        var password = document.getElementById("password").value;
        var confirmPassword = document.getElementById("confirmPassword").value;
        if (username === ''){
            input.setCustomValidity('Username is empty');
        } else if (username.length < 3){
            input.setCustomValidity('Password is too short');
        } else if (password === ''){
            input.setCustomValidity('Password is empty');
        } else if (password.length < 5 || confirmPassword.length < 5){
            input.setCustomValidity('Password is too short');
        } else if (confirmPassword === ''){
            input.setCustomValidity('Password is empty');
        } else if (confirmPassword !== password){
            input.setCustomValidity('Password does not match');
        } else {
            input.setCustomValidity('');
            send();
        }
    }

    function send(){
            $.ajax({
                type: 'POST',
                url: '/registration',
                data: "username=" + username + "&password=" + password + "&confirmPassword=" + confirmPassword,
                success: function () {
                    $("#form").hide();
                    show();
                }
            });
    }


    function show(){
        document.getElementById("form").hidden=true;
        document.getElementById("success").hidden=false;
    }