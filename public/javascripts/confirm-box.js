$('.show-confirm').submit(function() {
    let fileName = $(this).data("file");
    var ok = confirm(fileName+" will be deleted permanently! Are you ok with this?");
    return ok; // true or false
});