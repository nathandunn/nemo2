<%@ page import="edu.uoregon.nic.nemo.portal.ErpPatternExtraction" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpPatternExtraction.label', default: 'File')}"/>
    <title>
    <g:if test="${actionName == 'erpPatterns'}">
        ERP Results
    </g:if>
    <g:else>
        <g:message code="default.list.label" args="[entityName]"/>
    </g:else>
    </title>
    %{--<r:require module="jquery"/>--}%
    <r:require module="jquery"/>
</head>

<body>
<sec:ifAllGranted roles="ROLE_VERIFIED">
    <g:if test="${experimentHeader?.id}">
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="list" action="list"><g:message
                        code="default.list.label"
                        args="[entityName]"/>s</g:link></li>
                <li><g:link class="create" action="create" id="${experimentHeader.id}"><g:message
                        code="default.new.label"
                        args="[entityName]"/></g:link></li>
            </ul>
        </div>
    </g:if>
</sec:ifAllGranted>
<div id="list-erpPatternExtraction" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>

    <g:render template="/common/relatedFilter" model="['related': related]"/>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            %{--<g:sortableColumn property="identifier" params="[related: related?.relatedLookup]"--}%
                              %{--title="${message(code: 'erpPatternExtraction.identifier.label', default: 'Identifier')}"/>--}%
            %{--<g:sortableColumn property="set" params="[related: related?.relatedLookup]"--}%
                              %{--title="${message(code: 'erpPatternExtraction.set.label', default: 'Data Set')}"/>--}%
            %{--<g:sortableColumn property="format" params="[related: related?.relatedLookup]"--}%
                              %{--title="${message(code: 'erpPatternExtraction.format.label', default: 'Data format ')}"/>--}%
            <g:sortableColumn property="artifactFileName" params="[related: related?.relatedLookup]"
                              title="${message(code: 'erpPatternExtraction.artifactFileName.label', default: 'Artifact File Name')}"/>
            <g:sortableColumn property="method" title="Method"/>
            <g:sortableColumn property="baselineCondition" title="Baseline Condition"/>
            <g:sortableColumn property="conditionOfInterest" title="Condition of Interest"/>
            %{--<th>Classification</th>--}%

        </tr>
        </thead>
        <tbody>
        <g:each in="${erpPatternExtractionInstanceList}" status="i" var="erpPatternExtractionInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${erpPatternExtractionInstance.id}">${fieldValue(bean: erpPatternExtractionInstance, field: "artifactFileName")}</g:link>

                <g:link controller="erpPatternExtraction" action="download" id="${erpPatternExtractionInstance.id}">
                    <g:img dir="images/icon" file="download2.png"/>
                </g:link>
                </td>

                <td>
                    <g:renderOntological input="${erpPatternExtractionInstance.method}" related="erpPatternExtraction"/>
                </td>
                <td>
                    <g:renderOntological input="${erpPatternExtractionInstance.baselineCondition}" related="erpPatternExtraction"/>
                </td>
                <td>
                    <g:renderOntological input="${erpPatternExtractionInstance.conditionOfInterest}" related="erpPatternExtraction"/>
                </td>

                %{--<td>--}%
                    %{--<g:renderOntological input="${erpPatternExtractionInstance.set}" related="erpPatternExtraction"/>--}%
                %{--</td>--}%
                %{--<td>--}%
                    %{--<g:renderOntological input="${erpPatternExtractionInstance.format}" related="erpPatternExtraction"/>--}%
                %{--</td>--}%
                %{--<td>--}%
                    %{--<g:if test="${erpPatternExtractionInstance.isDownloadable()}">--}%
                    %{--</g:if>--}%
                    %{--<g:else>--}%
                        %{--<div style="color: graytext;">${erpPatternExtractionInstance.artifactFileName} Not uploaded</div>--}%
                    %{--</g:else>--}%
                %{--</td>--}%
                %{--<td>--}%
                    %{--<g:if test="${erpPatternExtractionInstance?.isRdfAvailable()}">--}%
                        %{--<r:script>--}%
                            %{--function showRow(id){--}%
                                 %{--var linkId = "classificationLink"+id;--}%
%{--//                                 alert(linkId)--}%
                                         %{--var element = document.getElementById(linkId);--}%
                                         %{--if(element.innerHTML.indexOf("Hide")==0){--}%
                                             %{--element.innerHTML = 'Show';--}%
                                         %{--}--}%
                                         %{--else{--}%
                                             %{--element.innerHTML = 'Hide';--}%
                                         %{--}--}%
                            %{--};--}%

                            %{--jQuery('#classificationRow${erpPatternExtractionInstance.id}').hide();--}%
                                %{--jQuery('#classificationLabel${erpPatternExtractionInstance.id}').click(function() {--}%
                            %{--jQuery('#classificationRow${erpPatternExtractionInstance.id}').toggle('slow', function() {--}%
                                       %{--showRow(${erpPatternExtractionInstance.id});--}%
                                  %{--});--}%
                                %{--});--}%

                                  %{--jQuery('#classificationRow${erpPatternExtractionInstance.id}').hide();--}%
                        %{--</r:script>--}%

                        %{--<div id="classificationLabel${erpPatternExtractionInstance.id}">--}%
                            %{--<a id="classificationLink${erpPatternExtractionInstance.id}"--}%
                               %{--href="javascript:;" onclick="--}%
                                %{--<g:remoteFunction action="classify" update="classificationBox${erpPatternExtractionInstance.id}"--}%
                                %{--asynchronous="true"--}%
                                %{--id="${erpPatternExtractionInstance.id}"--}%
                                %{--controller="experiment"--}%
                                %{--/>--}%
                            %{--">Show</a>--}%
                        %{--</div>--}%

                    %{--</g:if>--}%
                    %{--<g:elseif test="${erpPatternExtractionInstance.isRdfError()}">--}%
                        %{--<div class="error">There was an Error processing the RDF</div>--}%
                        %{--<br/>--}%
                        %{--<a href="">Reprocess via jQuery?</a>--}%
                    %{--</g:elseif>--}%
                    %{--<g:elseif test="${erpPatternExtractionInstance.isRdfInProcess()}">--}%
                        %{--Processing ...--}%
                    %{--</g:elseif>--}%
                %{--</td>--}%
            </tr>
            %{--<g:if test="${erpPatternExtractionInstance.isRdfAvailable()}">--}%
                %{--<tr id="classificationRow${erpPatternExtractionInstance.id}" class="nohover">--}%
                    %{--<td colspan="5"><div id="classificationBox${erpPatternExtractionInstance.id}"></div></td>--}%
                %{--</tr>--}%
            %{--</g:if>--}%
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${erpPatternExtractionInstanceTotal}" id="${experimentHeader?.id}"/>
    </div>
</div>
</body>
</html>
