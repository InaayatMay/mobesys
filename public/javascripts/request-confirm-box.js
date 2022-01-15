$('.approve-confirm').submit(function() {
    var ok = confirm("Are you sure that you want to approve this request? You can't undo this once its done!");
    return ok; // true or false
});

