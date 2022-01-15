$('.show-confirm').submit(function() {
    var ok = confirm("Are you sure that you want to post this request? You can't delete it once its done!");
    return ok; // true or false
});

$(document).ready(function() {
$('.form-control-select2').select2();

$('#lecturerId').one('select2:open', function(e) {
    $('input.select2-search__field').prop('placeholder', 'Search Lecturer');
});

$('#courseId').one('select2:open', function(e) {
    $('input.select2-search__field').prop('placeholder', 'Search Course');
});


fetch('/lecturer/'+ $('#firstElement').val() +'/courseInformationList')
    .then(response => response.json())
    .then(data =>
    {
        var courseOptions = [];
        for(var i=0; i < data.length; i++) {
            var obj = {};
            obj.id = data[i].id;
            obj.text = data[i].code + " " + data[i].name;
            courseOptions.push(obj);
        }
        console.log(courseOptions);

        $('#courseId').one('select2:open', function(e) {
            $('input.select2-search__field').prop('placeholder', 'Search Course');
        });

        $('#courseId').select2().empty();
        $('#courseId').select2({
        placeholder: "Empty Courses!",
            data: courseOptions
        });

        /*$('#courseId').empty();
        for(var i=0; i < data.length; i++)
        {
            $('#courseId').append(new Option(data[i].code + " " + data[i].name, data[i].id));
        }*/
    }
);


var lecturerId;
var courseId;

    $('#lecturerId').on('change', function() {

        console.log("change");
        var courseOptions = [];
        lecturerId = $("#lecturerId option:selected").val();
        console.log("lecturer id : " + lecturerId);

        fetch('/lecturer/'+ lecturerId +'/courseInformationList')
            .then(response => response.json())
            .then(data =>
            {
                for(var i=0; i < data.length; i++) {
                    var obj = {};
                    obj.id = data[i].id;
                    obj.text = data[i].code + " " + data[i].name;
                    courseOptions.push(obj);
                }
                console.log(courseOptions);

                $('#courseId').one('select2:open', function(e) {
                    $('input.select2-search__field').prop('placeholder', 'Search Course');
                });

                $('#courseId').select2().empty();
                $('#courseId').select2({
                placeholder: "Empty Courses!",
                    data: courseOptions
                });

                /*$('#courseId').empty();
                for(var i=0; i < data.length; i++)
                {
                    $('#courseId').append(new Option(data[i].code + " " + data[i].name, data[i].id));
                }*/
            }
        );
    })

});