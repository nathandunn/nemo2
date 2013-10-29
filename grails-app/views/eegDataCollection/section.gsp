<fieldset>
    <legend>
        8. EEG Data Collection
    </legend>

    <div class="section-narrative">
        <g:toggleTextLength input="${experimentInstance.eegDataCollectionNarrativeSummary}" maxLength="160"/>
    </div>
    <br/>

    <table class="center-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Electrode Array (Layout)</th>
            <th>Temporal Sampling Rate</th>
        </tr>
        </thead>
        <tbody>

        <g:each in="${eegDataCollections}" var="eegDataCollection">
            <tr>
                <td>
                    <g:link controller="eegDataCollection" action="show"
                            id="${eegDataCollection.id}">${eegDataCollection.identifier}</g:link>
                </td>

                <td>
                    %{--<g:renderUrlLink input="${eegDataCollection.sensorNetMontage}"/>--}%
                    <g:renderOntological input="${eegDataCollection.electrodeArrayLayout}" related="eegDataCollection"/>
                </td>

                <td>
                    ${eegDataCollection.samplingRateSetting} (ms)
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <g:editable users="${experimentInstance?.laboratory?.users}">
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <div class="nav" style="width: auto;">
                <g:link class="create" action="create" controller="eegDataCollection" id="${experimentInstance.id}">
                    New Eeg Data Collection
                </g:link>
            </div>
        </sec:ifAllGranted>
    </g:editable>
</fieldset>

