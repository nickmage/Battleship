var init = init();

function init(){
    $.ajax({
        type: 'GET',
        url: '/scoreboard',
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
        data = 'No one has played this game at all :(';
    } else {
        data = '<table><tr><td>Position</td><td>Username</td><td>Wins</td><td>Loses</td></tr>';
        for (let i = 0; i < list.length; i++){
            data += '<tr><td>' + (i + 1) + '</td><td>' + list[i].username + '</td><td>' + list[i].wins + '</td><td>' + list[i].loses + '</td></tr>'
        }
        data +='</table>';
    }
    $('#scoreboard').append(data);
}

function getBack(){
    if (localStorage.getItem('token') === null){
        window.location.href='#/';
    } else {
        window.location.href='#/menu';
    }
}
