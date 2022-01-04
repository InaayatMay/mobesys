$(document).ready(function () {
       $('.nav-tabs button').on('shown.bs.tab', function (e)  {
        console.log("tab change");
        var call = $('#modal_btn').attr("data-call");
        console.log(call);
        var target = $(e.target).attr("data-courseId");
        console.log("tab change" + target);
        fetch('/courseInformation/' + target)
            .then(response => response.json())
            .then(data =>
            {
                console.log(data);
                $('#ploCode').empty();

                for(var i=0; i < data.length; i++)
                {
                    $('#ploCode').append(new Option(data[i].code + " _ " +data[i].name, data[i].code));
                }
            }
        );

       });
});
