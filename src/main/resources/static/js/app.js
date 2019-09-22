var app = angular.module('myApp', ['ngRoute']);


app.config(function($routeProvider, $httpProvider) {
    $httpProvider.interceptors.push(function($q) {
        return {
        'request': function(config) {
            var token = localStorage.getItem('token');
            config.headers['Authorization'] = token;
            return config;
        }
        };
    });

    $routeProvider

    .when('/', {
    templateUrl : 'enter.html',
    controller : 'EnterController'
    })

    .when('/start', {
    templateUrl : 'start.html',
    controller : 'StartController'
    })

    .when('/login', {
    templateUrl : 'login.html',
    controller : 'LoginController'
    })

    .when('/registration', {
    templateUrl : 'registration.html',
    controller : 'RegistrationController'
    })

    .when('/scoreboard', {
    templateUrl : 'scoreboard.html',
    controller : 'ScoreboardController'
    })

    .when('/menu', {
    templateUrl : 'menu.html',
    controller : 'MenuController'
    })

    .when('/lobby', {
    templateUrl : 'lobby.html',
    controller : 'LobbyController'
    })

    .when('/game', {
    templateUrl : 'game.html',
    controller : 'GameController'
    })

    .otherwise({redirectTo: '/'});

});

app.controller('EnterController', function($scope, $http) {

});

app.controller('StartController', function($scope, $http) {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = "js/start.js";
    document.body.appendChild(script);
});

app.controller('LoginController', function($scope, $http) {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = "js/login.js";
    document.body.appendChild(script);
});

app.controller('RegistrationController', function($scope, $http) {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = "js/registration.js";
    document.body.appendChild(script);
});

app.controller('ScoreboardController', function($scope, $http) {
    //$scope.message = 'Hello from RegistrationController';
});

app.controller('MenuController', function($scope, $http) {
    //$scope.message = 'Hello from RegistrationController';
});

app.controller('LobbyController', function($scope, $http) {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = "js/lobby.js";
    document.body.appendChild(script);
});
app.controller('GameController', function($scope, $http) {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = "js/game.js";
    document.body.appendChild(script);
});


