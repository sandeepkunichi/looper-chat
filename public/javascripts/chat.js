var app = angular.module('chatApp', ['ngMaterial']);

app.controller('chatController', function ($scope, $sce) {

  $scope.messages = [];

  $scope.sendMessage = function () {
    var settings = {
      "async": true,
      "crossDomain": true,
      "url": "/sendMessage",
      "method": "POST",
      "headers": {
        "content-type": "application/json"
      },
      "processData": false,
      "data": "{\"id\": 1,\"text\": \"" + $scope.userMessage + "\",\"userId\": 1,\"channelId\": 1, \"username\": \"sandeep\"}"
    };
    $.ajax(settings).done(function (response) {
      console.log(response);
    });

    $scope.userMessage = "";
  };

  var  chatSocket = new WebSocket("ws://192.168.10.94:9000/chatSocket/random/sandeepkunichi");
  chatSocket.onmessage = function(event) {
    var jsonData = JSON.parse(event.data);
    jsonData.time = new Date().toLocaleTimeString();
    $scope.messages.push(jsonData);
    $scope.$apply();
    console.log(event.data);
  };

  $scope.trust = $sce.trustAsHtml;

});