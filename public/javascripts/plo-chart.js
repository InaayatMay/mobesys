$(document).ready(function() {
    let lecturerId = $('#ploChartDiv').attr('data-lecturerId');
    let courseId = $('#ploChartDiv').attr('data-courseId');

    fetch('/lecturer/' + lecturerId + '/courseInformation/' + courseId + '/classPloAttainment')
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
                barColors.push("grey");
            }

            new Chart("ploChart", {
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
                        text: "Class PLO Attainment"
                    },
                    scales: {
                        yAxes: [{
                        scaleLabel: {
                                display: true,
                                labelString: '% of Students Scoring > 50% in PLO'
                            }
                        }],
                        xAxes: [{
                        scaleLabel: {
                                display: true,
                                labelString: 'PLO and Class Average'
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

/*
<!-- The Modal -->
<div class="modal fade" id="myModal">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">Map CLO to PLO</h4>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <form method="post">
                    <label>Plo</label>
                    <select class="form-control" name="ploCode">
                        @if(unlinkedPloList.size() > 0) {
                            @for(plo <- unlinkedPloList) {
                                <option value="@plo.code">@plo.code _ @plo.title</option>
                            }
                        }
                    </select>

                    <label>Clo Code</label>
                    <input type="text" class="form-control" name="cloCode" required>

                    <label>Clo Title</label>
                    <input type="text" class="form-control" name="cloTitle" required>

                    <button type="submit" class="btn btn-secondary mt-3">Save Map</button>
                </form>
            </div>

            <!-- Modal footer -->
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>*/

/*<div class="card">
        <div class="card-header">
            Course Plan
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col">
                    <p>PROGRAMME</p>
                </div>
                <div class="col">
                    <p>@sub.coursePlanViewModel.programme</p>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <p>COURSE CODE</p>
                </div>
                <div class="col">
                    <p>@sub.coursePlanViewModel.courseCode</p>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <p>COURSE NAME</p>
                </div>
                <div class="col">
                    <p>@sub.coursePlanViewModel.courseName</p>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <p>SEMESTER</p>
                </div>
                <div class="col">
                    <p>@sub.coursePlanViewModel.semester</p>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <p>INTAKE BATCH</p>
                </div>
                <div class="col">
                    <p>@sub.coursePlanViewModel.intakeBatch</p>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <p>LECTURER</p>
                </div>
                <div class="col">
                    <p>@sub.coursePlanViewModel.lecturerName</p>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <p>COURSE TYPE</p>
                </div>
                <div class="col">
                    <p>@sub.coursePlanViewModel.courseType</p>
                </div>
            </div>
        </div>
    </div>
    <div class="card">
        <div class="card-header">
            <div class="row">
                <div class="col">
                    CLO to PLO mapping
                </div>
                <div class="col text-right">
                    @if(numberOfCloToPloMaps < 8) {
                        <button type="button" class="btn btn-sm btn-secondary" data-bs-toggle="modal" data-bs-target="#myModal">
                            Create New Map
                        </button>
                    } else {
                        <button type="button" class="btn btn-sm btn-secondary" disabled>
                            Create New Map
                        </button>
                    }
                </div>
            </div>
        </div>
        <div class="card-body">
            @if(form.hasGlobalErrors){
                <div class="alert alert-warning">
                    @for(error <- form.globalErrors){
                        <span>@error.message</span>
                    }
                </div>
            }

            @if(sub.coursePlanViewModel.cloToPloMapperList.size() == 8) {
                <div class="alert alert-secondary">
                    <div class="row">
                        <div class="col">
                            <p>You've reached maximum number of CLO to PLO mapping. Click the button to continue to fill up assessments information.</p>
                        </div>
                        <div class="col">
                            <a href="/lecturer/@userId/courseInformation/@sub.coursePlanViewModel.courseId/details/assessmentInformation" type="button" class="btn btn-sm btn-secondary float-right">
                                Continue to Fill Up Assessments Information
                            </a>
                        </div>
                    </div>
                </div>
            }
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Programme Learning Outcome</th>
                    <th scope="col">Course Learning Outcome</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                @if(sub.coursePlanViewModel.cloToPloMapperList.size() > 0) {
                    @for(clo <- sub.coursePlanViewModel.cloToPloMapperList) {
                        <tr>
                            <td>
                                @clo.ploCode _ @clo.ploTitle
                            </td>
                            <td>
                                @clo.cloCode _ @clo.cloTitle
                            </td>
                            <td>
                                <a href="/lecturer/@userId/courseInformation/@sub.coursePlanViewModel.courseId/details/@clo.cloId/editMap" class="btn btn-link">
                                    <span class="text-secondary">
                                        <i class="fas fa-pencil-alt"></i>
                                    </span>
                                </a>
                            </td>
                            <td>
                                <form method="post" class="show-confirm" data-file="@clo.ploCode To @clo.cloTitle" action="/lecturer/@userId/courseInformation/@sub.coursePlanViewModel.courseId/details/@clo.cloId/deleteMap">
                                    <button type="submit" class="btn btn-link">
                                        <span class="text-secondary">
                                            <i class="fas fa-trash-alt"></i>
                                        </span>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    }
                } else {
                    <tr>
                        <td colspan="5" class="text-center">
                            <div class="alert alert-secondary" role="alert">
                                There is no CLO to PLO mapping yet. <br><br>
                                <button type="button" class="btn btn-sm btn-secondary" data-bs-toggle="modal" data-bs-target="#myModal">
                                    Create New Map
                                </button>
                            </div>
                        </td>
                    </tr>
                }
                </tbody>
            </table>
        </div>
        <div class="card-footer">
            @if(sub.coursePlanViewModel.cloToPloMapperList.size() >= 3 && sub.coursePlanViewModel.cloToPloMapperList.size() <= 8) {
                <a href="/lecturer/@userId/courseInformation/@sub.coursePlanViewModel.courseId/details/assessmentInformation" type="button" class="btn btn-sm btn-secondary float-right">
                    Continue to Fill Up Assessments Information
                </a>
            } else {
                <button class="btn btn-secondary float-right" disabled>Continue to Fill Up Assessments Information</button>
            }
        </div>
    </div>*/
