$(document).ready(function() {
    let lecturerId = $('#myDiv').attr('data-lecturerId');
    let courseId = $('#myDiv').attr('data-courseId');

    fetch('/lecturer/' + lecturerId + '/courseInformation/' + courseId + '/gradeDistribution')
    .then(response => response.json())
    .then(data =>
        {
            var xValues = [];
            var yValues = [];
            var barColors = [];
            for(var i=0; i < data.length; i++)
            {
                xValues.push(data[i].grade);
                yValues.push(data[i].numberOfStudent);
                barColors.push("grey");
            }

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
                        padding: 25,
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