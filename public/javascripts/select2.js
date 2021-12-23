$(document).ready(function() {
//$('.form-control-select2').select2();
var schoolId;

    $('#schoolId').on('click', function() {

        var departmentOptions = [];
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

            var courseOptions = [];
            var departmentId = $("#departmentId option:selected").val();

            fetch('/schools/' + schoolId + '/departments/' + departmentId + '/courses')
                .then(response => response.json())
                .then(data =>
                {
                    $('#courseId').empty();
                    for(var i=0; i < data.length; i++)
                    {
                        $('#courseId').append(new Option(data[i].code + " " + data[i].name, data[i].id));
                    }
                }
            );
        })
});