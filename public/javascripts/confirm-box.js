$('.show-confirm').submit(function() {
    let fileName = $(this).data("file");
    var ok = confirm(fileName+" will be deleted permanently! Are you ok with this?");
    return ok; // true or false
});

$('#weightage').change(function () {
    var weightage = $('#weightage').val();
    var assessmentType = $("#assessmentType option:selected").val();
    var hasError = false;
    var errorMsg = assessmentType + " weightage must not be greater than ";
    if((assessmentType === 'Assignment' || assessmentType === 'Mini Project') && weightage > 30) {
        hasError = true;
        errorMsg = errorMsg + 30 + "%.";
    }
    else if((assessmentType === 'Test-1' || assessmentType === 'Test-2') && weightage > 20) {
        hasError = true;
        errorMsg = errorMsg + 20 + "%.";
    }
    else if(assessmentType === 'Quiz' && weightage > 15) {
        hasError = true;
        errorMsg = errorMsg + 15 + "%.";
    }
    else if(assessmentType === 'Final Exam' && weightage > 50) {
        hasError = true;
        errorMsg = errorMsg + 50 + "%.";
    }

    if(hasError) {

        var input = $('#weightage');
        setTimeout(function() {
            // this focus on last character if input isn't empty
            tmp = input.val(); input.focus().val("").blur().focus().val(tmp);
        }, 200);

        alert(errorMsg);
    }
});

$( "#submitBtn" ).click(function() {
  var weightage = $('#weightage').val();
      var assessmentType = $("#assessmentType option:selected").val();
      var hasError = false;
      var errorMsg = assessmentType + " weightage must not be greater than ";
      if((assessmentType === 'Assignment' || assessmentType === 'Mini Project') && weightage > 30) {
          hasError = true;
          errorMsg = errorMsg + 30 + "%.";
      }
      else if((assessmentType === 'Test-1' || assessmentType === 'Test-2') && weightage > 20) {
          hasError = true;
          errorMsg = errorMsg + 20 + "%.";
      }
      else if(assessmentType === 'Quiz' && weightage > 15) {
          hasError = true;
          errorMsg = errorMsg + 15 + "%.";
      }
      else if(assessmentType === 'Final Exam' && weightage > 50) {
          hasError = true;
          errorMsg = errorMsg + 50 + "%.";
      }

      if(hasError) {

          var input = $('#weightage');
          setTimeout(function() {
              // this focus on last character if input isn't empty
              tmp = input.val(); input.focus().val("").blur().focus().val(tmp);
          }, 200);

          console.log(errorMsg);
      }
      else {
          console.log('false');
         $('#newForm').submit();
     }
});

$("#saveStudentBtn").click(function() {
    var codeNumber = $('#codeNumber').val();
    console.log(typeof codeNumber);
    if(codeNumber.length >= 6 && codeNumber.length <= 10 && !isNaN( parseInt(codeNumber))) {
        var firstName = $('#firstName').val();
        if(firstName.length >= 3) {
            var lastName = $('#lastName').val();
            if(lastName.length >= 3) {
                var email = $('#email').val();
                if(email.includes("@scholar.miu.edu.my") || email.includes("@gmail.com") || email.includes("@yahoo.com") || email.includes("@hotmail.com")) {
                    $('#studentForm').submit();
                }
                else {
                    var input = $('#email');
                    setTimeout(function() {
                        // this focus on last character if input isn't empty
                        tmp = input.val(); input.focus().val("").blur().focus().val(tmp);
                    }, 200);

                    alert("Invalid email address!");
                }
            }
            else {
                var input = $('#lastName');
                setTimeout(function() {
                    // this focus on last character if input isn't empty
                    tmp = input.val(); input.focus().val("").blur().focus().val(tmp);
                }, 200);

                alert("Last name must contain at least 3 characters.");
            }
        }
        else {
            var input = $('#firstName');
            setTimeout(function() {
                // this focus on last character if input isn't empty
                tmp = input.val(); input.focus().val("").blur().focus().val(tmp);
            }, 200);

            alert("First name must contain at least 3 characters.");
        }
    }
    else {
        var input = $('#codeNumber');
        setTimeout(function() {
            // this focus on last character if input isn't empty
            tmp = input.val(); input.focus().val("").blur().focus().val(tmp);
        }, 200);

        alert("ID must be between 6 and 10 numbers!");
    }
});

