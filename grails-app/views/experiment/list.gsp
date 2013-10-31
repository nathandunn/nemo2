<%@ page import="edu.uoregon.nic.nemo.portal.SubjectGroup; edu.uoregon.nic.nemo.portal.Condition; edu.uoregon.nic.nemo.portal.EegDataCollection; edu.uoregon.nic.nemo.portal.ErpDataPreprocessing; edu.uoregon.nic.nemo.portal.ErpPatternExtraction; edu.uoregon.nic.nemo.portal.ErpAnalysisResult; edu.uoregon.nic.nemo.portal.Publication; edu.uoregon.nic.nemo.portal.Experiment" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'experiment.label', default: 'Experiment')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <r:require module="jquery" plugin="jquery"/>
    %{--<jq:plugin name="expander"/>--}%
    %{--<javascript src="http://plugins.learningjquery.com/summarize/jquery.summarize.js"></javascript>--}%
    %{--<r:script src="jquery/jquery.expander.min.js"/>--}%
</head>

<body>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                  args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="second_menu">
    <g:link action="list" controller="subjectGroup"
            class="${params.controller == "subjectGroup" ? "selected" : ""}">
        Subject Groups (${SubjectGroup.count()})
    </g:link>
    <g:link action="list" controller="condition"
            class="${params.controller == "condition" || params.controller == "stimulus" || params.controller == "response" ? "selected" : ""}">
        Conditions (${Condition.count()})
    </g:link>
    <g:link action="list" controller="eegDataCollection"
            class="${params.controller == "eegDataCollection" ? "selected" : ""}">
        EEG Data Acquisition (${EegDataCollection.count()})
    </g:link>
    <g:link action="list" controller="erpDataPreprocessing"
            class="${params.controller == "erpDataPreprocessing" ? "selected" : ""}">
        ERP Data Preprocessing (${ErpDataPreprocessing.count()})
    </g:link>
    <g:link action="list" controller="erpPatternExtraction"
            class="${params.action == "erpPatterns" ? "selected" : ""}">
        ERP Patterns (${ErpPatternExtraction?.count()})
    </g:link>
    <g:link action="list" controller="erpAnalysisResult"
            class="${params.action == "erpPatterns" ? "selected" : ""}">
        ERP Results (${ErpAnalysisResult?.count()})
    </g:link>
    %{--<g:link action="list" controller="dataFile" id="${experimentHeader.id}"--}%
    %{--class="${params.controller == "dataFile" && params.action != "erpPatterns" ? "selected" : ""}">--}%
    %{--Files (${experimentHeader?.dataFiles.size()})--}%
    %{--</g:link>--}%
    <g:link action="list" controller="publication"
            class="${params.controller == "publication" ? "selected" : ""}">
        Publications (${Publication.count()})
    </g:link>
</div>

<div id="list-experiment" class="content scaffold-list" role="main">

<h1><g:message code="default.list.label" args="[entityName]"/></h1>

<g:render template="/common/relatedFilter" model="['related': related]"/>

<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>


<table>
<thead>
<tr>
    <g:sortableColumn property="identifier" defaultOrder="asc" params="[related: related?.relatedLookup]"
                      title="${message(code: 'experiment.identifier.label', default: 'Experiment ID')}"/>
    <th>Experiment Paradigm</th>
    <g:sortableColumn property="conditionsNarrativeSummary" params="[related: related?.relatedLookup]"
                      title="${message(code: 'experiment.conditionsNarrativeSummary.label', default: 'Conditions Narrative Summary')}"/>
</tr>
</thead>
<tbody>
<g:each in="${experimentInstanceList}" status="i" var="experimentInstance">
    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

        <td>
            <g:showIdentifier instance="${experimentInstance}"/>
        </td>

        <td>
            <g:renderOntological input="${experimentInstance.experimentalParadigms}"
                                 related="experiment"/>
        </td>

        <td>
            %{--<g:output value="${experimentInstance.narrativeSummary}" maxSize="100" />--}%
            %{--<g:toggleText input="${experimentInstance.narrativeSummary}"/>--}%
            <g:toggleTextLength input="${experimentInstance.narrativeSummary}" maxLength="100"/>
    </div>
</td>

</tr>
</g:each>
</tbody>
</table>

<div class="pagination">
    <g:paginate total="${experimentInstanceTotal}" id="${experimentHeader?.id}"
                params="[related: related?.relatedLookup]"/>
</div>
</div>
%{--<jq:jquery>--}%
<r:script>

    //    $(document).ready(function () {
    //        $("div.expandable").expander({
    //            slicePoint:50, widow:2, expandEffect:'show'
    //        });
    //    });
</r:script>
%{--</jq:jquery>--}%
</body>
</html>
