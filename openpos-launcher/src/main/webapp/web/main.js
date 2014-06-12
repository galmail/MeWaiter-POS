/**
 * main.js
 */

$(document).ready(function() {

    $(".post_action").on("click", function(){
      $.post($(this).attr("href"), function(data){});
      return false;
    });



});
