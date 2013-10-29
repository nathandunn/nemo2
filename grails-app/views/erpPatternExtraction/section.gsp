<fieldset>
    <legend>10. ERP Pattern Extraction</legend>

    <div class="section-narrative">
    <g:toggleTextLength input="${experimentInstance.erpPatternExtractionNarrativeSummary}" maxLength="160"/>
    </div>
    <br/>

    %{--<span class="fit-table" aria-labelledby="erpPatternExtractions-label">--}%
    %{--<table class="fit-table">--}%
    <table class="center-table">
        <thead>
        <tr>
            %{--<th>Erp Pattern Extraction</th>--}%
            <th>File</th>
            <th>ERP Pattern Extraction Method</th>
            <th>Baseline Condition</th>
            <th>Condition of Interest</th>
            %{--<th>Data File Contents</th>--}%
            %{--<th>Data Format</th>--}%
            %{--<th>Download File</th>--}%
        </tr>
        </thead>
    %{--<g:each in="${experimentInstance.erpDataPreprocessings}" var="erpDataPreprocessing" status="st1">--}%
        <r:require module="jquery"/>
    <g:set var="erpPatternExtractions" value="${erpPatternExtractions?.size()==0? [] : erpPatternExtractions}"/>
        <g:each in="${erpPatternExtractions.sort{ a,b -> a.artifactFileName < b.artifactFileName ? -1 : 1 }}" var="erpPatternExtraction" status="st2">
            <tr>
                <td>
                    <g:if test="${erpPatternExtraction.download}">
                        <g:link controller="erpPatternExtraction" action="show" id="${erpPatternExtraction.id}">
                            ${erpPatternExtraction.artifactFileName?.replaceAll("_", " ")}</g:link>
                        <g:link controller="erpPatternExtraction" action="download" id="${erpPatternExtraction.id}">
                            <g:img dir="images/icon" file="download2.png"/>
                        </g:link>
                    </g:if>
                    <g:else>
                        <g:link controller="erpPatternExtraction" action="show" id="${erpPatternExtraction.id}">
                            ${erpPatternExtraction.artifactFileName?.replaceAll("_", " ")}</g:link>
                        <div style="color: graytext;">Not uploaded</div>

                    </g:else>

                </td>

                <td>
                    <g:renderOntological input="${erpPatternExtraction.method}" related="erpPatternExtraction"/>
                </td>
                <td>
                    <g:renderOntological input="${erpPatternExtraction.baselineCondition}" related="erpPatternExtraction"/>
                </td>
                <td>
                    <g:renderOntological input="${erpPatternExtraction.conditionOfInterest}" related="erpPatternExtraction"/>
                </td>
            </tr>
        </g:each>
    </table>
    <g:editable users="${experimentInstance?.laboratory?.users}">
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <div class="nav" style="width: auto;">
                <g:link class="create" action="create" controller="erpPatternExtraction" id="${experimentInstance.id}">
                    New Data File
                </g:link>
            </div>
        </sec:ifAllGranted>
    </g:editable>
</fieldset>

