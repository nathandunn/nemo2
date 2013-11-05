<%@ page import="edu.uoregon.nic.nemo.portal.client.BrainLocationEnum" %>
<!doctype html>
<html>

<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpAnalysisResult.label', default: 'Erp Analysis Result')}"/>
    <title>
        <g:message code="default.show.label" args="[entityName]"/>
        ${erpAnalysisResultInstance.artifactFileName}
    </title>
    <r:require modules="jquery,jquery-ui"/>

    <r:script type="text/javascript">
        $(document).ready(function () {
            $("#tabs").tabs();
            $(".${currentTime}").tabs("option","active",1);
//            $("#tabs").tabs({active:3});
//            $("#tabs").tabs({selected:5});
            $("#tabs").tabs({selected:${selectedTime}});
//            var active = $( ".selector" ).tabs( "option", "active" );
//            alert('active tab'+active.className);
        });
    </r:script>


    <script>
        var locations = {
             erpAnalysisResultId: ${erpAnalysisResultInstance.id}
            ,initialTime: ${currentTime}
            ,times: ${times}
            , baseTermUrl : '<g:createLink action="show" controller="term"/>'
        } ;
    </script>

    <script type="text/javascript" src="${resource(dir: 'gwt/edu.uoregon.nic.nemo.portal.BrainDisplay', file: 'edu.uoregon.nic.nemo.portal.BrainDisplay.nocache.js')}"></script>

</head>

<body>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                                  args="[entityName]"/>s</g:link></li>
            </sec:ifAllGranted>
            <li><g:link class="create" action="create" id="${erpAnalysisResultInstance?.experiment?.id}"><g:message
                    code="default.new.label" args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="show-erpAnalysisResult" class="content scaffold-show" role="main">
    <h1>11. Erp Analysis Result ${erpAnalysisResultInstance.artifactFileName}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="associated-with">
        <g:if test="${erpAnalysisResultInstance?.erpPatternExpression}">
            <strong>ERP Pattern Extraction</strong>
            <g:link controller="erpPatternExtraction" action="show"
                    id="${erpAnalysisResultInstance?.erpPatternExpression?.id}">${erpAnalysisResultInstance?.erpPatternExpression?.artifactFileName}</g:link>
            <br/>
        </g:if>

        <g:if test="${erpAnalysisResultInstance?.experiment}">
            <strong>Associated Experiment</strong>
            <g:link controller="experiment" action="show"
                    id="${erpAnalysisResultInstance?.experiment?.id}">${erpAnalysisResultInstance?.experiment?.identifier}</g:link>
        </g:if>
        <br/>
        <strong>Experimental Contrast</strong>
        <g:link controller="erpAnalysisResult" action="show"
                id="${erpAnalysisResultInstance?.id}">${erpAnalysisResultInstance?.artifactFileName}</g:link>
        <br/>
    </div>

    %{--<div class="section-narrative">--}%
    %{--<g:toggleTextLength input="${erpAnalysisResultInstance.experiment.erpAnalysisResultNarrativeSummary}"--}%
    %{--maxLength="350"/>--}%
    %{--</div>--}%
    %{--<br/>--}%


    <div class="table-legend">
        <br/>
        %{--<h3>--}%
        %{--Current Time ${currentTime} ms--}%
        %{--</h3>--}%
        %{--<h2>Mean Intensity (µV) at ROI's for a given Peak Time (ms)</h2>--}%
        <br/>

        <div class="significant">
            Significant *
            <div class="significant-positive">Positive</div>&nbsp;
            <div class="significant-negative">Negative</div>
        </div>

        <div id="tabs">
            <ul>
                <g:each in="${times}" var="time">
                    <li>
                        <g:if test="${time == currentTime}">
                            <a href="<g:createLink action='showIndividualTable' controller='erpAnalysisResult'
                                                   id='${erpAnalysisResultInstance.id}'
                                                   params='[time: time]'/>"><strong>${time}</strong></a>
                        </g:if>
                        <g:else>
                            <a href="<g:createLink action='showIndividualTable' controller='erpAnalysisResult'
                                                   id='${erpAnalysisResultInstance.id}'
                                                   params='[time: time]'/>"><strong>${time}</strong></a>
                        </g:else>
                    </li>
                </g:each>
            </ul>
        </div>
        <br/>

    </div>

</div>

<div id="brainPanel"></div>

<div id="searchPanel"></div>

</body>
</html>
