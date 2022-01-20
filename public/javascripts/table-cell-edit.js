$(document).ready(function() {

console.log('Ready ;: ' + $('#generateReport').attr('data-ready'));
if($('#generateReport').attr('data-ready') == 'false') {
    document.getElementById('generateReport').style.display = "none";
}
else {
    document.getElementById('generateReport').style.display = "block";
}


    $('.editable').on('click', function() {
        let uniqueId = $(this).attr('data-marksId');
        let maxLimit = $(this).attr('data-max');
        let originalValue = $(this).attr('data-original-value');
        let lecturerId = $(this).attr('data-lecturerId');
        let courseId = $(this).attr('data-courseId');
        let assessmentId = $(this).attr('data-assessmentId');
        let studentId = $(this).attr('data-studentId');

        console.log('Data marks : ' + uniqueId);
        console.log('Data max : ' + maxLimit);

        $('#'+uniqueId).on('click',function(){
            if($(this).find('input').is(':focus')) return this;
                var cell = $(this);
                var content = $(this).html();
                $(this).html('<input type="number" value="' + $(this).html() + '" min="0" max="' + maxLimit + '" step="0.5"/>')
                .find('input')
                .trigger('focus')
                .on({
                    'blur': function(){
                        $(this).trigger('closeEditable');
                    },
                    'keyup':function(e){
                        if(e.which == '13'){ // enter
                            $(this).trigger('saveEditable');
                        } else if(e.which == '27'){ // escape
                            $(this).trigger('closeEditable');
                        }
                    },
                    'closeEditable':function(){
                        cell.html(content);
                    },
                    'saveEditable':function(){
                        content = $(this).val();
                        var formId = 'uniqueForm' + uniqueId;
                        var inputId = 'marksInput' + uniqueId;
                        console.log("this val : > max " + $(this).val()>maxLimit);
                        if($(this).val() > (maxLimit*1.0)) {
                            alert("Marks should not exceed maximum amount - " + maxLimit);
                            $('#'+inputId).val(originalValue);
                             $('#'+uniqueId).html(originalValue);
                        }
                        else {
                            $('#'+inputId).val(content);
                            console.log('Form id : ' + formId);
                            $(this).trigger('closeEditable');

                            var dataString = $('#'+formId).serialize();

                            console.log(dataString);

                            $.ajax({
                                type: "POST",
                                url: "/lecturer/"+ lecturerId +"/courseInformation/"+ courseId +"/details/assessmentInformation/"+ assessmentId +"/studentInformation/"+ studentId +"/marksEnty/"+ uniqueId,
                                data: dataString,
                                success: function () {
                                    if (content > 0.0) {
                                        console.log('content > 0.0');
                                        document.getElementById('generateReport').style.display = "block";
                                        document.getElementById(uniqueId).classList.remove("bg-secondary");
                                    }
                                }
                            });
                            return false;
                        }
                    }
                });
            });
    });

    $('#codeNumber').on('click', function() {
        console.log($('option:selected',this).data("first"));
        $('#firstName').val($('option:selected',this).data("first"));
        $('#lastName').val($('option:selected',this).data("last"));
        $('#gender').val($('option:selected',this).data("gender"));
        $('#email').val($('option:selected',this).data("email"));

        console.log("Selected : " + $(this).val());
        $('#studentId').val($(this).val());
    });
});

$('.show-confirm').submit(function() {
    let fileName = $(this).data("file");
    var ok = confirm(fileName+" and all related marks entry will be removed from this course permanently! Are you ok with this?");
    return ok; // true or false
});



