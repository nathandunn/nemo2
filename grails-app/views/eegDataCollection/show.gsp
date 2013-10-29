<%@ page import="edu.uoregon.nic.nemo.portal.EegDataCollection" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                                  args="[entityName]"/>s</g:link></li>
            </sec:ifAllGranted>
            <g:if test="${eegDataCollectionInstance?.experiment?.id}">
                <li><g:link class="create" action="create" id="${eegDataCollectionInstance?.experiment?.id}"><g:message
                        code="default.new.label"
                        args="[entityName]"/></g:link></li>
            </g:if>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="show-eegDataCollection" class="content scaffold-show" role="main">
    <h1>8. Eeg Data Collection ${eegDataCollectionInstance.identifier}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="associated-with">
        <strong>Associated Experiment</strong>
        <g:link controller="experiment" action="show"
                id="${eegDataCollectionInstance.experiment.id}">
            ${eegDataCollectionInstance.experiment.identifier}
        </g:link>
    </div>

    <div class="section-narrative">
        <g:toggleTextLength input="${eegDataCollectionInstance.experiment.eegDataCollectionNarrativeSummary}"
                            maxLength="350"/>
    </div>
    <br/>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>

        <g:tableEntry key="EEG Data Collection Set ID" number="8.1"
                      term="NEMO_6339000" value="${eegDataCollectionInstance.identifier}"/>
        <g:tableEntry key="Electrode array (manufacturer)" number="8.2"
                      term="NEMO_3240000" ontological="${eegDataCollectionInstance.manufacturer}"/>
        <g:tableEntry key="Electrode array (layout)" number="8.3" related="eegDataCollection"
                      term="NEMO_6227000" ontological="${eegDataCollectionInstance.electrodeArrayLayout}"/>
        <g:tableEntry key="Recording reference" number="8.4"
                      term="NEMO_6771000" ontological="${eegDataCollectionInstance.reference}"/>
        <g:tableEntry key="Ground (noise) electrode" number="8.5"
                      term="NEMO_4335000" ontological="${eegDataCollectionInstance.ground}"/>
        <g:tableEntry key="Scalp-to-electrode impedance threshold" number="8.6"
                      term="NEMO_6541000"
                      value="${eegDataCollectionInstance.scalpElectrodeImpedanceThreshold}" suffix=" &Omega;"/>
        <g:tableEntry key="Amplifier gain" number="8.7"
                      term="NEMO_0949000" value="${eegDataCollectionInstance.gainMeasurementDatum}"
                      suffix=" bits / microvolt"/>
        <g:tableEntry key="Amplifier input impedance" number="8.8"
                      term="NEMO_2655000" value="${eegDataCollectionInstance.amplifierInputImpedance}"
                      suffix=" &Omega;"/>
        <g:tableEntry key="Temporal sampling rate" number="8.9"
                      term="NEMO_2585000" value="${eegDataCollectionInstance.samplingRateSetting}" suffix=" ms"/>
        <g:tableEntry key="Highpass Amplifier filter setting" number="8.10"
                      term="NEMO_3383000"
                      value="${eegDataCollectionInstance.voltageAmplifierHighpassFilterSetting}" suffix=" Hz"/>
        <g:tableEntry key="Lowpass Amplifier filter setting" number="8.11"
                      term="NEMO_4273000" value="${eegDataCollectionInstance.voltageAmplifierLowpassFilterSetting}"
                      suffix=" Hz"/>
        <g:tableEntry key="Eeg Data Collection Software" number="8.12"
                      term="IAO_0000010" ontological="${eegDataCollectionInstance.software}"/>
    </table>

    <g:editable users="${eegDataCollectionInstance?.experiment?.laboratory.users}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${eegDataCollectionInstance?.id}"/>
                <g:link class="edit" action="edit" id="${eegDataCollectionInstance?.id}"><g:message
                        code="default.button.edit.label" default="Edit"/></g:link>
                <g:actionSubmit class="delete" action="delete"
                                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </fieldset>
        </g:form>
    </g:editable>
</div>
</body>
</html>
