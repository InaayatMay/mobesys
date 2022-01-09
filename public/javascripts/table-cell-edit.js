$(document).ready(function() {
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
                            //$('#'+formId).submit();
                            $(this).trigger('closeEditable');
                            //e.preventDefault();


                            var dataString = $('#'+formId).serialize();

                            console.log(dataString);
                            //alert(dataString);
                            //return false;

                            $.ajax({
                                type: "POST",
                                url: "/lecturer/"+ lecturerId +"/courseInformation/"+ courseId +"/details/assessmentInformation/"+ assessmentId +"/studentInformation/"+ studentId +"/marksEnty/"+ uniqueId,
                                data: dataString,
                                success: function () {
                                    if (content > 0.0) {
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

   /* $( "form" ).on( "submit", function(e) {

        var dataString = $(this).serialize();

        // alert(dataString); return false;

        $.ajax({
            type: "POST",
            url: "bin/process.php",
            data: dataString,
            success: function () {
                $("#contact_form").html("<div id='message'></div>");
                $("#message")
                .html("<h2>Contact Form Submitted!</h2>")
                .append("<p>We will be in touch soon.</p>")
                .hide()
                .fadeIn(1500, function () {
                    $("#message").append(
                        "<img id='checkmark' src='images/check.png' />"
                    );
                });
            }
        });
        return false;
        //e.preventDefault();
    });*/
});




/*$.fn.makeEditable = function() {
  $(this).on('click',function(){
    if($(this).find('input').is(':focus')) return this;
    var cell = $(this);
    var content = $(this).html();
    $(this).html('<input type="number" value="' + $(this).html() + '" step="0.01"/>')
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
          let uniqueId = $(this).attr('data-marksId');
          content = $(this).val();
          $('#marksInput').val($(this).val());
          $('#form'+uniqueId).submit();
          $(this).trigger('closeEditable');
        }
    });
  });
return this;
}*/

