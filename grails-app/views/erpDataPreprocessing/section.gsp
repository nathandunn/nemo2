<fieldset>
    <legend>9. ERP Data Preprocessing</legend>

    <div class="section-narrative">
        <g:toggleTextLength input="${experimentInstance.erpDataPreprocessingsNarrativeSummary}" maxLength="160"/>
    </div>
    <br/>

    <table class="center-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>ERP event <br/>(for averaging)</th>
            <th>ERP epoch <br/>length (ms)</th>
            <th>ERP baseline <br/>length (ms)</th>
            <th>Offline <br/>reference</th>
            <th>Transformation <br/>labels</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${erpDataPreprocessingSet}" var="erpDataPreprocessing" status="st1">
            <tr>

                <td>
                    <g:showIdentifier instance="${erpDataPreprocessing}" controller="erpDataPreprocessing"/>
                </td>

                <td>
                    <g:renderOntological
                            input="${erpDataPreprocessing.event}" related="erpDataPreprocessing"/>
                </td>

                <td>
                    ${erpDataPreprocessing.erpEpochLength}
                </td>

                <td>
                    ${erpDataPreprocessing.baselineLength}
                </td>

                <td>
                    <g:renderOntological
                            input="${erpDataPreprocessing.reference}" related="erpDataPreprocessing"/>
                </td>

                <td>
                    <g:renderOntological input="${erpDataPreprocessing.cleaningTransformations}" delimiter=", " related="erpDataPreprocessing"/>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <g:editable users="${experimentInstance?.laboratory?.users}">
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <div class="nav" style="width: auto;">
                <g:link class="create" action="create" controller="erpDataPreprocessing" id="${experimentInstance.id}">
                    New ERP Data Preprocessing
                </g:link>
            </div>
        </sec:ifAllGranted>
    </g:editable>
</fieldset>
