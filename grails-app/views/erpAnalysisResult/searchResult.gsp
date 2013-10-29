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
<div id="list-erpAnalysisResult" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>

%{--<g:render template="/common/relatedFilter" model="['related': related]"/>--}%

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <th>Erp Analysis Result</th>
            <th>Instances</th>

        </tr>
        </thead>
        <tbody>

        <g:each in="${results}" status="result" var="erpAnalysisResultInstance">
            <tr>
                <td>
                    <g:link action="show" id="${erpAnalysisResultInstance.erpAnalysisResultId}">
                        ${erpAnalysisResultInstance.erpAnalysisResultName}
                    </g:link>
                </td>
                <td>
                    <ul>
                    <g:each in="${erpAnalysisResultInstance.individuals.individualDTOList}" var="individual">
                        <li>
                            %{--<g:link url="${individual.url}">--}%

                            %{--</g:link>--}%
                            ${individual.nameFromUrl}
                        </li>
                    </g:each>
                        </ul>

                </td>

            </tr>
        </g:each>
        </tbody>
    </table>

    %{--<div class="pagination">--}%
    %{--<g:paginate total="${erpAnalysisResultInstanceTotal}" id="${experimentHeader?.id}"/>--}%
    %{--</div>--}%
</div>
</body>
</html>
