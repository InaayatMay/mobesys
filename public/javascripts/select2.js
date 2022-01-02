$(document).ready(function() {
$('.form-control-select2').select2();
var schoolId;
var departmentId;
var programme;

    $('#schoolId').on('click', function() {

        schoolId = $("#schoolId option:selected").val();

        fetch('/schools/' + schoolId + '/departments')
            .then(response => response.json())
            .then(data =>
            {
                $('#departmentId').empty();
                for(var i=0; i < data.length; i++)
                {
                    $('#departmentId').append(new Option(data[i].name, data[i].id));
                }
            }
        );
    })

    $('#departmentId').on('click', function() {

        departmentId = $("#departmentId option:selected").val();

        fetch('/schools/' + schoolId + '/departments/' + departmentId + '/programmes')
            .then(response => response.json())
            .then(data =>
            {
                $('#programme').empty();
                for(var i=0; i < data.length; i++)
                {
                    $('#programme').append(new Option(data[i].value, data[i].value));
                }
            }
        );
    })

    $('#programme').on('click', function() {

        var courseOptions = [];
        programme = $("#programme option:selected").val();

        fetch('/schools/' + schoolId + '/departments/' + departmentId + '/programmes/' + programme + '/courses')
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