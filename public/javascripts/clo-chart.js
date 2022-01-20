$(document).ready(function() {
    let lecturerId = $('#cloChartDiv').attr('data-lecturerId');
    let courseId = $('#cloChartDiv').attr('data-courseId');

    fetch('/lecturer/' + lecturerId + '/courseInformation/' + courseId + '/classCloAttainment')
    .then(response => response.json())
    .then(data =>
        {
            var xValues = [];
            var yValues = [];
            var barColors = [];
            for(var i=0; i < data.length; i++)
            {
                xValues.push(data[i].cloCode + '_' + data[i].classAverage);
                yValues.push(data[i].percentageOfPassedStudent);
                barColors.push("#4723D9");
            }

            new Chart("cloChart", {
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
                        text: "Class CLO Attainment"
                    },
                    scales: {
                        yAxes: [{
                        scaleLabel: {
                                display: true,
                                labelString: '% of Students Scoring > 50% in CLO'
                            }
                        }],
                        xAxes: [{
                        scaleLabel: {
                                display: true,
                                labelString: 'CLO and Class Average'
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

let width, height, gradient;
function getGradient(ctx, chartArea) {
  const chartWidth = chartArea.right - chartArea.left;
  const chartHeight = chartArea.bottom - chartArea.top;
  if (gradient === null || width !== chartWidth || height !== chartHeight) {
    // Create the gradient because this is either the first render
    // or the size of the chart has changed
    width = chartWidth;
    height = chartHeight;
    gradient = ctx.createLinearGradient(0, chartArea.bottom, 0, chartArea.top);
    gradient.addColorStop(0, Utils.CHART_COLORS.blue);
    gradient.addColorStop(0.5, Utils.CHART_COLORS.yellow);
    gradient.addColorStop(1, Utils.CHART_COLORS.red);
  }

  return gradient;
}