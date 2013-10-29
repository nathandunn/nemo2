<%@ page import="edu.uoregon.nic.nemo.portal.ReferenceElectrode; edu.uoregon.nic.nemo.portal.GroundElectrode; edu.uoregon.nic.nemo.portal.ElectrodeArrayLayout; edu.uoregon.nic.nemo.portal.ElectrodeArrayManufacturer; edu.uoregon.nic.nemo.portal.Software; edu.uoregon.nic.nemo.portal.EegDataCollection" %>



<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'experiment', 'error')} required">
	<label for="experiment">
		<g:message code="eegDataCollection.experiment.label" default="Experiment" />
		<span class="required-indicator">*</span>
	</label>
    %{--<g:showIdentifier instance="${eegDataCollectionInstance}"/>--}%
    <g:if test="${eegDataCollectionInstance?.experiment?.id}">
        <g:hiddenField name="experiment.id" value="${eegDataCollectionInstance?.experiment?.id}" />
        <g:showIdentifier instance="${eegDataCollectionInstance?.experiment}"/>
    </g:if>
    <g:else>
        <g:hiddenField name="experiment.id" value="${experimentInstance?.id}" />
        <g:showIdentifier instance="${experimentInstance}"/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry key="EEG Data Collection Set ID" number="8.1" term="NEMO_6339000" required="true"/>
    <g:textField name="identifier" value="${eegDataCollectionInstance?.identifier}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'manufacturer', 'error')} ">
    <g:propertyEntry key="Electrode array (manufacturer)" number="8.2" term="NEMO_3240000" />
    %{--<g:textField name="electrodeArrayManufacturer" value="${eegDataCollectionInstance?.electrodeArrayManufacturer}"/>--}%
    <g:select name="manufacturer.id" from="${ElectrodeArrayManufacturer.listOrderByName()}"
              optionValue="name" optionKey="id" value="${eegDataCollectionInstance?.manufacturer?.id}"
              noSelection="[null:'- None -']"
    />
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'electrodeArrayLayout', 'error')} ">
    <g:propertyEntry key="Electrode array (layout)" number="8.3" term="NEMO_6227000" required="true"/>
    %{--<g:textField name="sensorNetMontage" value="${eegDataCollectionInstance?.sensorNetMontage}"/>--}%
    <g:select name="electrodeArrayLayout.id" from="${ElectrodeArrayLayout.listOrderByName()}"
              optionValue="name" optionKey="id" value="${eegDataCollectionInstance?.electrodeArrayLayout?.id}"
              noSelection="['':'- None -']"
    />
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'referenceElectrode', 'error')} ">
    <g:propertyEntry key="Recording reference" number="8.4" term="NEMO_6771000" />
    %{--<g:textField name="referenceElectrode" value="${eegDataCollectionInstance?.referenceElectrode}"/>--}%
    <g:select name="reference.id" from="${ReferenceElectrode.listOrderByName()}"
              optionValue="name" optionKey="id" value="${eegDataCollectionInstance?.reference?.id}"
              noSelection="[null:'- None -']"
    />
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'groundElectrode', 'error')} ">
    <g:propertyEntry key="Ground (noise) electrode" number="8.5"
                  term="NEMO_4335000" />
    %{--<g:textField name="groundElectrode" value="${eegDataCollectionInstance?.groundElectrode}"/>--}%
    <g:select name="ground.id" from="${GroundElectrode.listOrderByName()}"
              optionValue="name" optionKey="id" value="${eegDataCollectionInstance?.ground?.id}"
              noSelection="[null:'- None -']"
    />
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'scalpElectrodeImpedanceThreshold', 'error')} ">
    <g:propertyEntry key="Scalp-to-electrode impedance threshold (&Omega;)" number="8.6" term="NEMO_6541000" />
    <g:field type="number" name="scalpElectrodeImpedanceThreshold" step="any" value="${eegDataCollectionInstance.scalpElectrodeImpedanceThreshold}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'gainMeasurementDatum', 'error')} ">
    <g:propertyEntry key="Amplifier gain (bits/microvolt)" number="8.7" term="NEMO_0949000" />
    <g:field type="number" name="gainMeasurementDatum" step="any" value="${eegDataCollectionInstance.gainMeasurementDatum}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'amplifierInputImpedance', 'error')} ">
    <g:propertyEntry key="Amplifier input impedance (&Omega;)" number="8.8" term="NEMO_2655000" />
	<g:field type="number" name="amplifierInputImpedance" step="any" value="${eegDataCollectionInstance.amplifierInputImpedance}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'samplingRateSetting', 'error')} ">
    <g:propertyEntry key="Temporal sampling rate (Hz)" number="8.9" term="NEMO_2585000"  required="true"/>
    <g:field type="number" name="samplingRateSetting" step="any" value="${eegDataCollectionInstance.samplingRateSetting}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'voltageAmplifierHighpassFilterSetting', 'error')} ">
    <g:propertyEntry key="Highpass Amplifier filter setting (Hz)" number="8.10" term="NEMO_3383000" />
    <g:field type="number" name="voltageAmplifierHighpassFilterSetting" step="any" value="${eegDataCollectionInstance.voltageAmplifierHighpassFilterSetting}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'voltageAmplifierLowpassFilterSetting', 'error')} ">
    <g:propertyEntry key="Lowpass Amplifier filter setting (Hz)" number="8.11" term="NEMO_4273000" />
    <g:field type="number" name="voltageAmplifierLowpassFilterSetting" step="any" value="${eegDataCollectionInstance.voltageAmplifierLowpassFilterSetting}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'software', 'error')} ">
    <g:propertyEntry key="Eeg Data Collection Software" number="8.12" term="IAO_0000010" />
    %{--<g:field type="number" name="eegDataCollectionSoftware" step="any" value="${eegDataCollectionInstance.eegDataCollectionSoftware}"/>--}%
    <g:select name="software.id" from="${Software.listOrderByName()}"
              optionValue="name" optionKey="id" value="${eegDataCollectionInstance?.software?.id}"
              noSelection="[null:'- None -']"
    />
</div>


%{--<div class="fieldcontain ${hasErrors(bean: eegDataCollectionInstance, field: 'unverifiedCopy', 'error')} ">--}%
	%{--<label for="unverifiedCopy">--}%
		%{--<g:message code="eegDataCollection.unverifiedCopy.label" default="Unverified Copy" />--}%
		%{----}%
	%{--</label>--}%
	%{--<g:checkBox name="unverifiedCopy" value="${eegDataCollectionInstance?.unverifiedCopy}" />--}%
%{--</div>--}%


