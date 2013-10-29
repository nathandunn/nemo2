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
    %{--<r:require modules="jquery,jquery-ui"/>--}%
    <r:require modules="jquery"/>

    %{--<r:script type="text/javascript">--}%
    %{--$(document).ready(function () {--}%
    %{--$("#tabs").tabs();--}%
    %{--$(".${location}").tabs("option","active",1);--}%
    %{--//            $("#tabs").tabs({active:3});--}%
    %{--//            $("#tabs").tabs({selected:5});--}%
    %{--$("#tabs").tabs({selected:${selectedLocation}});--}%
    %{--//            var active = $( ".selector" ).tabs( "option", "active" );--}%
    %{--//            alert('active tab'+active.className);--}%
    %{--});--}%
    %{--</r:script>--}%

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
        <h2>Peak Times (ms) versus Mean Intensity (µV)</h2>
        <br/>


        <table class="location-table">
            <tr>
                <g:createLocationLink location="${BrainLocationEnum.LFTEMP}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.LFRONT}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.MFRONT}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.RFRONT}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.RFTEMP}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                %{--<g:if test="${currentLocation.name()==BrainLocationEnum.LFTEMP.name()}">--}%
                %{--<td class="selected">--}%
                %{--</g:if>--}%
                %{--<g:else>--}%
                %{--<td>--}%
                %{--</g:else>--}%
                %{--<a href="<g:createLink action='showIndividualsAtLocation' controller='erpAnalysisResult'--}%
                %{--id='${erpAnalysisResultInstance.id}'--}%
                %{--params='[locationName: currentLocation.name()]'/>"><strong>${currentLocation.name()}</strong></a>--}%
                %{--</td>--}%

                %{--<td>${BrainLocationEnum.LFRONT.name()}</td>--}%
                %{--<td>${BrainLocationEnum.MFRONT.name()}</td>--}%
                %{--<td>${BrainLocationEnum.RFRONT.name()}</td>--}%
                %{--<td>${BrainLocationEnum.RFTEMP.name()}</td>--}%
            </tr>
            <tr>
                <g:createLocationLink location="${BrainLocationEnum.LCTEMP}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                %{--<td>${BrainLocationEnum.LCTEMP.name()}</td>--}%
                %{--<td>${BrainLocationEnum.LCENT.name()}</td>--}%
                <g:createLocationLink location="${BrainLocationEnum.LCENT}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.MCENT}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.RCENT}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.RCTEMP}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                %{--<td>${BrainLocationEnum.MCENT.name()}</td>--}%
                %{--<td>${BrainLocationEnum.RCENT.name()}</td>--}%
                %{--<td>${BrainLocationEnum.RCTEMP.name()}</td>--}%
            </tr>
            <tr>
                <g:createLocationLink location="${BrainLocationEnum.LPTEMP}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.LPAR}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.MPAR}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.RPAR}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.RPTEMP}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                %{--<td>${BrainLocationEnum.LPTEMP.name()}</td>--}%
                %{--<td>${BrainLocationEnum.LPAR.name()}</td>--}%
                %{--<td>${BrainLocationEnum.MPAR.name()}</td>--}%
                %{--<td>${BrainLocationEnum.RPAR.name()}</td>--}%
                %{--<td>${BrainLocationEnum.RPTEMP.name()}</td>--}%
            </tr>
            <tr>
                <g:createLocationLink location="${BrainLocationEnum.LOTEMP}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.LOCC}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.MOCC}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.ROCC}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                <g:createLocationLink location="${BrainLocationEnum.ROTEMP}" selectedLocation="${currentLocation}"
                                      result="${erpAnalysisResultInstance}"/>
                %{--<td>${BrainLocationEnum.LOTEMP.name()}</td>--}%
                %{--<td>${BrainLocationEnum.LOCC.name()}</td>--}%
                %{--<td>${BrainLocationEnum.MOCC.name()}</td>--}%
                %{--<td>${BrainLocationEnum.ROCC.name()}</td>--}%
                %{--<td>${BrainLocationEnum.ROTEMP.name()}</td>--}%
            </tr>

        </table>


        %{--<a href="<g:createLink action='showIndividualLocationTable' controller='erpAnalysisResult'--}%
        %{--id='${erpAnalysisResultInstance.id}'--}%
        %{--params='[locationName: currentLocation.name()]'/>"><strong>${currentLocation.name()}</strong>--}%

        %{--<div id="tabs">--}%
        %{--<ul>--}%
        %{--<g:each in="${locations}" var="location">--}%
        %{--<li>--}%
        %{--<g:if test="${location == currentLocation}">--}%
        %{--<a href="<g:createLink action='showIndividualLocationTable' controller='erpAnalysisResult'--}%
        %{--id='${erpAnalysisResultInstance.id}'--}%
        %{--params='[locationName: location.name()]'/>"><strong>${location}</strong>--}%
        %{--</a>--}%
        %{--</g:if>--}%
        %{--<g:else>--}%
        %{--<a href="<g:createLink action='showIndividualLocationTable' controller='erpAnalysisResult'--}%
        %{--id='${erpAnalysisResultInstance.id}'--}%
        %{--params='[locationName: location.name()]'/>"><strong>${location}</strong>--}%
        %{--</a>--}%
        %{--</g:else>--}%
        %{--</li>--}%
        %{--</g:each>--}%
        %{--</ul>--}%
        %{--</div>--}%
        %{--<br/>--}%

        %{--</div>--}%
        <div class="significant">
            Significant *
            <div class="significant-positive">Positive</div>&nbsp;
            <div class="significant-negative">Negative</div>
        </div>

    </div>


    <g:include action="showIndividualLocationTable" controller="erpAnalysisResult"
               id="${erpAnalysisResultInstance.id}"
               params="[locationName: currentLocation.name()]"/>

</body>
</html>
