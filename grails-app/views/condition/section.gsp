<fieldset>
    <legend>5. Experiment Conditions</legend>

    <div class="section-narrative">
        <g:toggleTextLength input="${experimentInstance.conditionsNarrativeSummary}" maxLength="160"/>
    </div>

    <br/>
    <table class="center-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Type</th>
            <th>Task (Instructions)</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${conditions}" var="condition">
            <tr>
                <td>
                    <g:link controller="condition" action="show"
                            id="${condition.id}">${condition.identifier}</g:link>
                    <g:if test="${condition.unverifiedCopy}">
                        <div class="error-small">Unverified</div>
                    </g:if>
                </td>
                <td>
                    <g:renderOntological input="${condition.types}"  related="condition"/>
                </td>
                <td>
                    <g:renderOntological input="${condition.taskInstruction}"  related="condition"/>
                </td>
            </tr>
        </g:each>

        </tbody>
    </table>

    <g:editable users="${experimentInstance?.laboratory?.users}">
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <div class="nav" style="width: auto;">
                <g:link class="create" action="create" controller="condition" id="${experimentInstance.id}">
                    New Condition
                </g:link>
                <g:if test="${experimentInstance?.id}">
                    <g:select name="condition"
                              from="${edu.uoregon.nic.nemo.portal.Condition.listOrderByIdentifier()}"
                              optionKey="id" optionValue="identifier"
                              noSelection="['': '- Copy Condition To Experiment -']"
                              onchange="document.location.href='${request.contextPath}/condition/copyCondition?id=${experimentInstance.id}&conditionId='+this.value"/>
                </g:if>
            </div>
        </sec:ifAllGranted>
    </g:editable>
</fieldset>

