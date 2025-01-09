$(function() {
    $("#category_button").click(function() {
        if ($("#slide").hasClass('on')) {
            $("#burgur").removeClass('on');
            $("#slide").removeClass('on');
        } else {
            $("#burgur").addClass('on');
            $("#slide").addClass('on');
        }
    });
})
//test