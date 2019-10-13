var init = init();

function init(){
    var playerId = localStorage.getItem('playerId');
    $.ajax({
        type: 'GET',
        url: '/roomlist',
        data: "playerId=" + playerId,
        statusCode: {
            200: function (data) {
                console.log(data);
                appendTable(data);
            },
            400: function (data) {
                alert(data);
            }
        }
    });
}

function appendTable(list){
    var data;
    if (list.length == 0) {
        data = 'There is no private rooms at the moment.';
    } else {
        data = '<table><tr><td>Username</td><td>Wins</td><td>Loses</td></tr>';
        for (let i = 0; i < list.length; i++){
            data += '<tr><td>' + list[i].roomId + '</td><td>' + list[i].username + '</td><td><a href ="google.com">Join room</a></td></tr>'
        }
        data +='</table>';
    }
    $('#roomList').append(data);
}