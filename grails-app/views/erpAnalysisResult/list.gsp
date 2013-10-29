<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpAnalysisResult.label', default: 'File')}"/>
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
<div id="list-erpAnalysisResult" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>

    <g:render template="/common/relatedFilter" model="['related': related]"/>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="artifactFileName" params="[related: related?.relatedLookup]"
                              title="${message(code: 'erpAnalysisResult.identifier.label', default: 'Filename')}"/>
            %{--<g:sortableColumn property="dependentVariable"--}%
                              %{--title="${message(code: 'erpAnalysisResult.set.label', default: 'Condition of Interest')}"/>--}%
            %{--<g:sortableColumn property="independentVariable"--}%
                              %{--title="${message(code: 'erpAnalysisResult.set.label', default: 'Baseline Condition')}"/>--}%
            <g:sortableColumn property="analysisMethod"
                              title="${message(code: 'erpAnalysisResult.set.label', default: 'Analysis Method')}"/>
            %{--<g:sortableColumn property="format" params="[related: related?.relatedLookup]"--}%
            %{--title="${message(code: 'erpAnalysisResult.format.label', default: 'Data format ')}"/>--}%
            %{--<g:sortableColumn property="artifactFileName" params="[related: related?.relatedLookup]"--}%
            %{--title="${message(code: 'erpAnalysisResult.artifactFileName.label', default: 'Artifact File Name')}"/>--}%
            <th>Classification</th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${erpAnalysisResultInstanceList}" status="i" var="erpAnalysisResultInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    <g:link action="show"
                            id="${erpAnalysisResultInstance.id}">${fieldValue(bean: erpAnalysisResultInstance, field: "artifactFileName")}
                    </g:link>
                    <g:if test="${erpAnalysisResultInstance.isDownloadable()}">
                        <g:link controller="erpAnalysisResult" action="download" id="${erpAnalysisResultInstance.id}">
                             <g:img dir="images/icon" file="download2.png"/>
                        </g:link>
                    </g:if>
                    <g:else>
                        <div style="color: graytext;">${erpAnalysisResultInstance.artifactFileName} Not uploaded</div>
                    </g:else>
                </td>
                %{--<td>--}%
                    %{--<g:renderOntological input="${erpAnalysisResultInstance.dependentVariable}" related="erpAnalysisResult"/>--}%
                %{--</td>--}%
                %{--<td>--}%
                    %{--<g:renderOntological input="${erpAnalysisResultInstance.independentVariable}" related="erpAnalysisResult"/>--}%
                %{--</td>--}%
                <td>
                    <g:renderOntological input="${erpAnalysisResultInstance.analysisMethod}" related="erpAnalysisResult"/>
                </td>
                %{--<td>--}%
                %{--<g:renderOntological input="${erpAnalysisResultInstance.set}" related="erpAnalysisResult"/>--}%
                %{--</td>--}%
                %{--<td>--}%
                %{--<g:renderOntological input="${erpAnalysisResultInstance.format}" related="erpAnalysisResult"/>--}%
                %{--</td>--}%
                <td>
                    <g:if test="${erpAnalysisResultInstance?.isRdfAvailable()}">
                        <r:script>
                            function showRow(id){
                                 var linkId = "classificationLink"+id;
//                                 alert(linkId)
                                         var element = document.getElementById(linkId);
                                         if(element.innerHTML.indexOf("Hide")==0){
                                             element.innerHTML = 'Show';
                                         }
                                         else{
                                             element.innerHTML = 'Hide';
                                         }
                            };

                            jQuery('#classificationRow${erpAnalysisResultInstance.id}').hide();
                                jQuery('#classificationLabel${erpAnalysisResultInstance.id}').click(function() {
                            jQuery('#classificationRow${erpAnalysisResultInstance.id}').toggle('slow', function() {
                                       showRow(${erpAnalysisResultInstance.id});
                                  });
                                });

                                  jQuery('#classificationRow${erpAnalysisResultInstance.id}').hide();
                        </r:script>

                        <div id="classificationLabel${erpAnalysisResultInstance.id}">
                            <a id="classificationLink${erpAnalysisResultInstance.id}"
                               href="javascript:;" onclick="
                                <g:remoteFunction action="classify" update="classificationBox${erpAnalysisResultInstance.id}"
                                asynchronous="true"
                                id="${erpAnalysisResultInstance.id}"
                                controller="erpAnalysisResult"
                                />
                            ">Show</a>
                        </div>

                    </g:if>
                    <g:elseif test="${erpAnalysisResultInstance.isRdfError()}">
                        <div class="error">There was an Error processing the RDF</div>
                        <br/>
                        <a href="">Reprocess via jQuery?</a>
                    </g:elseif>
                    <g:elseif test="${erpAnalysisResultInstance.isRdfInProcess()}">
                        Processing ...
                    </g:elseif>
                </td>
            </tr>
            <g:if test="${erpAnalysisResultInstance.isRdfAvailable()}">
                <tr id="classificationRow${erpAnalysisResultInstance.id}" class="nohover">
                    <td colspan="6"><div id="classificationBox${erpAnalysisResultInstance.id}"></div></td>
                </tr>
            </g:if>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${erpAnalysisResultInstanceTotal}" id="${experimentHeader?.id}"/>
    </div>
</div>
</body>
</html>
