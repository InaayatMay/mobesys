$(document).ready(function() {
//$('.form-control-select2').select2();
var schoolId;

    $('#schoolId').on('change', function() {

        var departmentOptions = [];
        schoolId = $("#schoolId option:selected").val();

        fetch('/schools/' + schoolId + '/departments')
            .then(response => response.json())
            .then(data =>
            {
                /*for(var i=0; i < data.length; i++) {
                    var obj = {};
                    obj.id = data[i].id;
                    obj.text = data[i].name;
                    departmentOptions.push(obj);
                }
                console.log(departmentOptions);

                $('#departmentId').select2().empty();
                $('#departmentId').select2({
                    placeholder: "Empty Department!",
                    data: departmentOptions
                });*/

                $('#departmentId').empty();
                for(var i=0; i < data.length; i++)
                {
                    $('#departmentId').append(new Option(data[i].name, data[i].id));
                }
            }
        );
    })

    $('#departmentId').on('change', function() {

            var courseOptions = [];
            var departmentId = $("#departmentId option:selected").val();

            fetch('/schools/' + schoolId + '/departments/' + departmentId + '/courses')
                .then(response => response.json())
                .then(data =>
                {
                    /*for(var i=0; i < data.length; i++) {
                        var obj = {};
                        obj.id = data[i].id;
                        obj.text = data[i].code + " " +data[i].name;
                        courseOptions.push(obj);
                    }
                    console.log(courseOptions);

                    $('#courseId').select2().empty();
                    $('#courseId').select2({
                        placeholder: "Empty Course!",
                        data: courseOptions
                    });*/

                    $('#courseId').empty();
                    for(var i=0; i < data.length; i++)
                    {
                        $('#courseId').append(new Option(data[i].code + " " + data[i].name, data[i].id));
                    }
                }
            );
        })
});