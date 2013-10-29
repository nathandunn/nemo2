
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'erpPatternExtraction.label', default: 'ERP Pattern Extraction')}"/>
    <title>
        <g:message code="default.show.label" args="[entityName]"/>
        ${erpPatternExtractionInstance.artifactFileName}
    </title>
    <r:require module="jquery"/>
</head>

<body>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                                  args="[entityName]"/>s</g:link></li>
            </sec:ifAllGranted>
            <li><g:link class="create" action="create" id="${erpPatternExtractionInstance?.experiment?.id}"><g:message
                    code="default.new.label" args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="show-erpPatternExtraction" class="content scaffold-show" role="main">
    <h1>10. ERP Pattern Extraction ${erpPatternExtractionInstance.artifactFileName}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="associated-with">
        <g:if test="${erpPatternExtractionInstance?.erpDataPreprocessing}">
            <strong>Associated Erp Data Preprocessing</strong>
            <g:link controller="erpDataPreprocessing" action="show"
                    id="${erpPatternExtractionInstance?.erpDataPreprocessing?.id}">${erpPatternExtractionInstance?.erpDataPreprocessing?.identifier}</g:link>
            <br/>
        </g:if>

        <g:if test="${erpPatternExtractionInstance?.experiment}">
            <strong>Associated Experiment</strong>
            <g:link controller="experiment" action="show"
                    id="${erpPatternExtractionInstance?.experiment?.id}">${erpPatternExtractionInstance?.experiment?.identifier}</g:link>
        </g:if>
    </div>

    <div class="section-narrative">
        <g:toggleTextLength input="${erpPatternExtractionInstance.experiment.erpPatternExtractionNarrativeSummary}"
                            maxLength="350"/>
    </div>
    <br/>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>
        %{--<g:tableEntry key="Data File ID" number="10.1"--}%
                      %{--term="NEMO_9799000" value="${erpPatternExtractionInstance.identifier}"/>--}%
        %{--<g:tableEntry key="Data file contents" number="10.2" related="erpPatternExtraction"--}%
                      %{--term="IAO_0000100" ontological="${erpPatternExtractionInstance.set}"/>--}%
        %{--<g:tableEntry key="Data file format" number="10.3" related="erpPatternExtraction"--}%
                      %{--term="NEMO_1194000" ontological="${erpPatternExtractionInstance.format}"/>--}%
        %{--<g:tableEntry key="Artifact File Name" number="10.4"--}%
        %{--term="NEMO_3087000" url="${erpPatternExtractionInstance.dataFormat}"/>--}%


        <tr>
            <td class="table-uri">
                <g:renderLabel number="10.1" term="NEMO_7123457"/>
            </td>
            <td class="table-key">
                ERP Pattern Extraction ID
            </td>
            <td class="table-value">
                <g:if test="${erpPatternExtractionInstance.artifactFileName}">
                    ${erpPatternExtractionInstance?.artifactFileName?.replaceAll("_", " ")}
                    <g:if test="${erpPatternExtractionInstance.download}">
                        <g:link action="download" controller="erpPatternExtraction" id="${erpPatternExtractionInstance.id}">
                            <g:img dir="images/icon" file="download2.png"/></g:link>
                    </g:if>
                </g:if>
            </td>
        </tr>

        <g:tableEntry key="ERP Pattern Extraction Method" number="10.2"
                      term="NEMO_0000505" ontological="${erpPatternExtractionInstance.method}"  related="erpPatternExtraction"/>
        <g:tableEntry key="Condition for Comparison" number="10.3"
                      term="NEMO_7752000" ontological="${erpPatternExtractionInstance.baselineCondition}" related="erpPatternExtraction"/>
        <g:tableEntry key="Condition of Interest" number="10.4"
                      term="NEMO_2813000" ontological="${erpPatternExtractionInstance.conditionOfInterest}"  related="erpPatternExtraction"/>
        <g:tableEntry key="Data file format" number="10.5"
                      term="NEMO_1194000" ontological="${erpPatternExtractionInstance.format}" />
        <g:tableEntry key="Data file contents" number="10.6"
                      term="IAO_0000100" ontological="${erpPatternExtractionInstance.set}"/>

    </table>

    <g:editable users="${erpPatternExtractionInstance?.experiment?.laboratory?.users}">
            <g:form>
                <fieldset class="buttons">
                    <g:hiddenField name="id" value="${erpPatternExtractionInstance?.id}"/>
                    <g:link class="edit" action="edit" id="${erpPatternExtractionInstance?.id}">
                        <g:message code="default.button.edit.label" default="Edit"/>
                    </g:link>
                    <g:actionSubmit class="delete" action="delete"
                                    value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                </fieldset>
            </g:form>
    </g:editable>

    <br/>
    <g:include view="erpAnalysisResult/section.gsp"
               model="[experimentInstance: erpPatternExtractionInstance.experiment
            ,erpAnalysisResults:erpPatternExtractionInstance.erpAnalysisResults]"/>

</div>

</body>
