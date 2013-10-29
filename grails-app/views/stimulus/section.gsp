<fieldset>
    <legend>6. Stimulus Presentation</legend>
    <table class="center-table">
        <thead>
        <tr>
            <th>Experimental <br/> Condition ID</th>
            <th>Stimulus Type</th>
            <th>Stimulus <br/>Modality</th>
            %{--<th>Prime-Target <br/>SOA (ms)</th>--}%
        </tr>
        </thead>


        <g:each in="${stimuli}" var="stimulus">
            <tr>
                <td>
                    <g:link controller="stimulus" action="show"
                            id="${stimulus.id}">${stimulus.identifier}</g:link>
                </td>
                <td>
                    %{--<g:renderUrlLink input="${stimulus.targetStimulusType}" />--}%
                    <g:renderOntological input="${stimulus.targetType}" related="stimulus" />
                </td>
                <td>
                    %{--<g:renderUrlLink input="${stimulus.targetStimulusModality}"/>--}%
                    <g:renderOntological input="${stimulus.targetModality}" related="stimulus"/>
                </td>
                %{--<td>--}%
                    %{--${stimulus.stimulusOnsetAsynchrony}--}%
                %{--</td>--}%
            </tr>
        </g:each>
    </table>
    <g:if test="${conditionInstance}">
        <g:editable users="${experimentInstance?.laboratory?.users}">
            <sec:ifAllGranted roles="ROLE_VERIFIED">
                <div class="nav" style="width: auto;">
                    <g:link class="create" action="create" controller="stimulus" id="${conditionInstance.id}">
                        New Stimulus to Condition ${conditionInstance.identifier}
                    </g:link>
                </div>
            </sec:ifAllGranted>
        </g:editable>
    </g:if>
</fieldset>

