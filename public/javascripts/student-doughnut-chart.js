$(document).ready(function() {
    let lecturerId = $('#grade-div').attr('data-lecturerId');
    let courseId = $('#grade-div').attr('data-courseId');

    fetch('/lecturer/' + lecturerId + '/courseInformation/' + courseId + '/studentAnalysis')
    .then(response => response.json())
    .then(data =>
        {
            /*var xValues = [];
            var yValues = [];
            var barColors = [];
            for(var i=0; i < data.length; i++)
            {
                xValues.push(data[i].courseCode + '_' + data[i].courseName);
                yValues.push(data[i].numberOfStudents);
                barColors.push("#4723D9");
            }*/

            console.log("Failed " + data.failedPercentage);

            var ctx = document.getElementById("total-std-chart");
            var myChart = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: ['PASS', 'FAIL'],
                    datasets: [{
                        label: 'Analysis of Results',
                        data: [data.passedPercentage, data.failedPercentage],
                        backgroundColor: [
                            'rgba(71, 120, 100, 0.2)',
                            'rgba(255, 0, 0, 0.2)'
                        ],
                        borderColor: [
                            'rgba(127, 17, 224, 0.5)',
                            'rgba(255, 0, 0, 0.5)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                title: {
                    display: true,
                    padding: 25,
                    text: "Analysis Results of Total Students"
                },
                responsive: true,
                    maintainAspectRatio: false,
                    cutoutPercentage: 80,
                    tooltips: {
                            callbacks: {
                            label: function(tooltipItem, data) {
                                return data['labels'][tooltipItem['index']] + ': ' + data['datasets'][0]['data'][tooltipItem['index']] + '%';
                            }
                        }
                    }
                }
            });
        });
});