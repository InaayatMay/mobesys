$(document).ready(function() {
    let lecturerId = $('#studentChartDiv').attr('data-lecturerId');

    fetch('/lecturer/' + lecturerId + '/studentCourseList')
    .then(response => response.json())
    .then(data =>
        {
            var xValues = [];
            var yValues = [];
            var barColors = [];
            for(var i=0; i < data.length; i++)
            {
                xValues.push(data[i].courseCode + '_' + data[i].courseName);
                yValues.push(data[i].numberOfStudents);
                barColors.push("grey");
            }

            new Chart("studentChart", {
                type: "bar",
                data: {
                labels: xValues,
                datasets: [{
                backgroundColor: barColors,
                data: yValues
                }]
                },
                options: {
                    legend: {display: false},
                    title: {
                        display: true,
                        padding: 25,
                        text: "Number Of Students In Each Courses"
                    },
                    scales: {
                        yAxes: [{
                        scaleLabel: {
                                display: true,
                                labelString: 'Number of Students'
                            },
                            ticks: {
                              // forces step size to be 50 units
                              stepSize: 1
                            }
                        }],
                        xAxes: [{
                        scaleLabel: {
                                display: true,
                                labelString: 'Courses'
                            }
                        }]
                    },
                    tooltips: {
                            enabled: true
                        },
                        hover: {
                            animationDuration: 1
                        },
                        animation: {
                        duration: 1,
                        onComplete: function () {
                            var chartInstance = this.chart,
                                ctx = chartInstance.ctx;
                                ctx.textAlign = 'center';
                                ctx.fillStyle = "rgba(0, 0, 0, 1)";
                                ctx.textBaseline = 'bottom';
                                this.data.datasets.forEach(function (dataset, i) {
                                    var meta = chartInstance.controller.getDatasetMeta(i);
                                    meta.data.forEach(function (bar, index) {
                                        var data = dataset.data[index];
                                        ctx.fillText(data, bar._model.x, bar._model.y - 5);
                                    });
                                });
                            }
                        }
                }
            });
        }
    );

    /*var xValues = ["A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F"];
    var yValues = [1, 2, 3, 4, 5];
    var barColors = ["grey", "grey", "grey", "grey", "grey", "grey", "grey", "grey", "grey", "grey"];

    new Chart("myChart", {
        type: "bar",
        data: {
        labels: xValues,
        datasets: [{
        backgroundColor: barColors,
        data: yValues
        }]
        },
        options: {
            legend: {display: false},
            title: {
                display: true,
                text: "Grade Distribution"
            },
            scales: {
                yAxes: [{
                scaleLabel: {
                        display: true,
                        labelString: 'Student Number'
                    }
                }],
                xAxes: [{
                scaleLabel: {
                        display: true,
                        labelString: 'Grades'
                    }
                }]
            }
        }
    });*/
});