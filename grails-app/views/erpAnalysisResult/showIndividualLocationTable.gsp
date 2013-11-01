<%@ page import="edu.uoregon.nic.nemo.portal.client.BrainLocationEnum" %>
<!doctype html>

<head>
    %{--<meta name="layout" content="main">--}%
    %{--<g:set var="entityName" value="${message(code: 'erpAnalysisResult.label', default: 'Erp Analysis Result')}"/>--}%
    %{--<title>--}%
    %{--<g:message code="default.show.label" args="[entityName]"/>--}%
    %{--${erpAnalysisResultInstance.artifactFileName}--}%
    %{--</title>--}%
    %{--<r:require modules="jquery,jquery-ui"/>--}%

    %{--<r:script type="text/javascript">--}%
    %{--$(document).ready(function () {--}%
    %{--$("#tabs").tabs();--}%
    %{--});--}%
    %{--</r:script>--}%
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
        google.load("visualization", "1", {packages: ["corechart"]});
        google.setOnLoadCallback(drawChart);
        function drawChart() {
            var data = new google.visualization.DataTable();
            data.addColumn('number', 'Time(ms)'); // Implicit domain label col.
            data.addColumn('number', 'Mean Intensity'); // Implicit series 1 data col.
            data.addColumn({type: 'boolean', role: 'certainty'}); // annotation role col.
//            data.addColumn({type: 'string', role: 'annotationText'}); // annotation role col.


            %{--var data = google.visualization.arrayToDataTable([--}%
            data.addRows([

                %{--['Time (ms)','Mean Intensity µV',null],--}%
                <g:each in="${times}" var="time" status="loopStatus">
                [${time.intValue()}, ${individuals.get(time).meanIntensity}
                    ,${individuals.get(time).statisticallySignificant}
                    %{--,${individuals.get(time).statisticallySignificant ? "'Significant'" : "null"}--}%
                ]
                ${loopStatus.intValue()<times.size()? ',' :'' }

                </g:each>
            ]);

            var options = {
//                "title": "Peak Times (ms) versus Mean Intensity (µV)"
                 "hAxis.minValue": 0
                , "hAxis.maxValue": 1000
                , hAxis: {title:"Peak Time (ms)"}
                , vAxis: {title: "Mean Intensity (µV)"}
                , "annotation":{"1":"letter"}

            };

//            var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
            var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
            chart.draw(data, options);
        }

        function selectHandler(e)     {
            alert(data.getValue(chart.getSelection()[0].row, 0));
        }
    </script>

</head>

<body>

<div id="${location}">

    %{--${link}--}%
    %{--<g:link controller="erpAnalysisResult" id="${erpAnalysisResultInstance.id}#${erpAnalysisResultInstance.artifactFileName}" action="show">--}%
    %{--<div class="table-legend">--}%
    %{--<g:link controller="erpAnalysisResult" id="${erpAnalysisResultInstance.id}" fragment="${link}" action="show">--}%
    %{--Qualitative Results--}%
    %{--</g:link>--}%
    %{--</div>--}%
    %{--<br/>--}%

    <table class="center-detail-table">
        <thead>
        <tr>
            <g:each in="${times}" var="time">
                <th>
                    ${time}
                </th>
            </g:each>
        </tr>
        </thead>
        <tbody>
        <tr>
            <g:each in="${times}" var="time">
                <td class="value-link">
                    <g:render template="meanIntensityForTime" model="[time: time]"/>
                    %{--<g:formatNumber number="${individuals.get(time).meanIntensity}" maxFractionDigits="3"/>--}%
                </td>
            </g:each>
        </tr>
        </tbody>

    </table>
</div>

%{--<div id="chart_div" style="width: 900px; height: 500px;"></div>--}%
<div id="chart_div" style="width: 90%"></div>

</body>
