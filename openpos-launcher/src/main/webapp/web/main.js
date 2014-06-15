/**
 * main.js
 */

var mw = {
    reload_update_label: function() {
        $.get("/cli/update", function(data) {
            $("#last_update_label").html(data.result);
        });
    },
    reload_daily_cash_label: function() {
        $.get("/cli/daily_cash", function(data) {
            if (data.success) {
                $("#daily_cash_label").html("Total: " + data.total + " on " + data.payments + " payments.");
            }
        });
    },
    reload_today_label: function() {
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1;
        var yyyy = today.getFullYear();
        if (dd < 10) {
            dd = '0' + dd
        }
        if (mm < 10) {
            mm = '0' + mm
        }
        var today = dd + '/' + mm + '/' + yyyy;
        $("#today_label").html(today);
    }
};

$(document).ready(function() {

    mw.reload_update_label();
    mw.reload_daily_cash_label();
    mw.reload_today_label();
    
    $("#closeCashButton").on("click", function() {
        var btn = $(this);
        btn.addClass('disabled').prop('disabled', true);
        $.post($(this).attr("href"), function(data) {
            if(data.success){
                $("#alert").attr("class", "alert alert-success");
                $("#alertMsg").text("Cash Closed Successfully.");
            }
            else {
                $("#alert").attr("class", "alert alert-danger");
                $("#alertMsg").text("Error Closing Cash.");
            }
            $("#alert").show().delay(3000).hide(1000);
            btn.removeClass('disabled').prop('disabled', false);
        });
        return false;
    });

    $("#updateButton").on("click", function() {
        var btn = $(this);
        btn.addClass('disabled').prop('disabled', true);
        $("#alertMsg").text("Updating system, please wait...");
        $("#alert").attr("class", "alert alert-warning");
        $("#alert").show();
        $.post($(this).attr("href"), function(data) {
            if (data.success) {
                $("#alertMsg").text("System updated successfully.");
                $("#alert").removeClass("alert-warning").addClass("alert-success");
                mw.reload_update_label();
            }
            else {
                $("#alertMsg").text("Server failed when updating. Please check connection and try again.");
                $("#alert").removeClass("alert-warning").addClass("alert-danger");
            }
            $("#alert").delay(3000).hide(1000);
            btn.removeClass('disabled').prop('disabled', false);
        });
        return false;
    });

    $(".post_action").on("click", function() {
        $.post($(this).attr("href"), function(data) {
        });
        return false;
    });



});
