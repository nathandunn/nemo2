<fieldset>
    <legend>7. Behavior Data Collection</legend>
    <table class="center-table">
        <thead>
        <tr>
            <th>Experimental Condition ID</th>
            <th>Response Type</th>
            <th>Response Modality</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${responses}" var="response">
            <tr>
                <td>
                    <g:link controller="response" action="show" id="${response.id}">${response.identifier}</g:link>
                </td>
                <td>
                    <g:renderOntological input="${response.role}"  related="response"/>
                </td>
                <td>
                    <g:renderOntological input="${response.modality}"  related="response"/>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <g:if test="${conditionInstance}">
        <g:editable users="${experimentInstance?.laboratory?.users}">
            <sec:ifAllGranted roles="ROLE_VERIFIED">
                <div class="nav" style="width: auto;">
                    <g:link class="create" action="create" controller="response" id="${conditionInstance.id}">
                        New Behavior Data Collection to Condition ${conditionInstance.identifier}
                    </g:link>
                </div>
            </sec:ifAllGranted>
        </g:editable>
    </g:if>
</fieldset>
