<%@ page import="edu.uoregon.nic.nemo.portal.OfflineReference; edu.uoregon.nic.nemo.portal.ErpEvent; edu.uoregon.nic.nemo.portal.ErpDataPreprocessing" %>



<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'experiment', 'error')} required">
    <label for="experiment">
        <g:message code="erpDataPreprocessing.experiment.label" default="Experiment"/>
        <span class="required-indicator">*</span>
    </label>
    <g:if test="${erpDataPreprocessingInstance?.experiment?.id}">
        <g:hiddenField name="experiment.id" value="${erpDataPreprocessingInstance?.experiment?.id}"/>
        <g:showIdentifier instance="${erpDataPreprocessingInstance?.experiment}"/>
    </g:if>
    <g:else>
        <g:hiddenField name="experiment.id" value="${experimentInstance?.id}"/>
        <g:showIdentifier instance="${experimentInstance}"/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry key="ERP Data Preprocessing Set ID" number="9.1" term="NEMO_4694000" required="true"/>
    <g:textField name="identifier" value="${erpDataPreprocessingInstance?.identifier}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'highpassFilterAlgorithm', 'error')} ">
    <g:propertyEntry key="Digital highpass filter transformation (Hz)" number="9.2" term="NEMO_1525000"/>
    <g:field type="number" name="highpassFilterAlgorithm" step="any"
             value="${erpDataPreprocessingInstance.highpassFilterAlgorithm}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'lowpassFilterAlgorithm', 'error')} ">
    <g:propertyEntry key="Digital lowpass filter transformation (Hz)" number="9.3" term="NEMO_9612000"/>
    <g:field type="number" name="lowpassFilterAlgorithm" step="any"
             value="${erpDataPreprocessingInstance.lowpassFilterAlgorithm}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'dataCleaningTransformation', 'error')} ">
    <g:propertyEntry key="Data cleaning transformation" number="9.4" term="NEMO_1626000"/>
    <g:if test="${erpDataPreprocessingInstance?.id}">
        <g:select name="cleaningTransformation"
                  from="${edu.uoregon.nic.nemo.portal.CleaningTransformation.listOrderByName()-erpDataPreprocessingInstance.cleaningTransformations}"
                  optionKey="id" optionValue="name"
                  noSelection="['': '- Add Transformation -']"
                  onchange="
                  ${remoteFunction(
                          action: 'addTransformation'
                          , controller: 'erpDataPreprocessing'
                          , params: '\'transformId=\' + this.value '
                          , id: erpDataPreprocessingInstance.id
                          , method: 'POST'
                          , onSuccess: 'window.location =\'' + request.contextPath + '/erpDataPreprocessing/edit/' + erpDataPreprocessingInstance.id + '\';'
                          , onError: 'alert(\'error\');'
                  )}"/>
        <g:each in="${erpDataPreprocessingInstance.cleaningTransformations?.sort(it?.name)}" var="transform" status="status">
            <span id="transform${transform.id}">
                <g:link action="removeTransformation"
                        params="[id: erpDataPreprocessingInstance.id, transformId: transform.id]"
                        controller="erpDataPreprocessing">X</g:link>
                <g:link action="show" controller="term"
                        id="${transform.suffix}">${transform.name}</g:link>
                ${status.intValue() < erpDataPreprocessingInstance.cleaningTransformations.size() - 1 ? "&bull;" : ""}
            </span>
        </g:each>
    </g:if>
    <g:else>
        <g:addAfterCreating/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'event', 'error')} ">
    <g:propertyEntry key="Erp event (for averaging)" number="9.5" term="NEMO_6783000" required="true"/>
    %{--<g:textField name="erpEvent" value="${erpDataPreprocessingInstance?.erpEvent}"/>--}%
    <g:select name="event.id" from="${ErpEvent.listOrderByName()}"
              optionValue="name" optionKey="id" value="${erpDataPreprocessingInstance?.event?.id}"
              noSelection="[null: '- None -']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'numberGoodTrials', 'error')} ">
    <g:propertyEntry key="Number EEG Segments (per condition)" number="9.6" term="NEMO_2055000"/>
    <g:field type="number" name="numberGoodTrials" value="${erpDataPreprocessingInstance.numberGoodTrials}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'erpEpochLength', 'error')} ">
    <g:propertyEntry key="Erp epoch length (ms)" number="9.7" term="NEMO_3620000" required="true"/>
    <g:field type="number" name="erpEpochLength" step="any" value="${erpDataPreprocessingInstance.erpEpochLength}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'baselineLength', 'error')} ">
    <g:propertyEntry key="Erp baseline length (ms)" number="9.8" term="NEMO_6232000" required="true"/>
    <g:field type="number" name="baselineLength" step="any" value="${erpDataPreprocessingInstance.baselineLength}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'reference', 'error')} ">
    <g:propertyEntry key="Offline reference" number="9.9" term="NEMO_2400000" required="true"/>
    %{--<g:textField name="electrophysiologicalDataReference" value="${erpDataPreprocessingInstance?.electrophysiologicalDataReference}"/>--}%
    <g:select name="reference.id" from="${OfflineReference.listOrderByName()}"
              optionValue="name" optionKey="id" value="${erpDataPreprocessingInstance?.reference?.id}"
              noSelection="[null: '- None -']"/>
</div>

%{--<div class="fieldcontain ${hasErrors(bean: erpDataPreprocessingInstance, field: 'dataFiles', 'error')} ">--}%
%{--<label for="dataFiles">--}%
%{--<g:message code="erpDataPreprocessing.dataFiles.label" default="Data Files" />--}%
%{----}%
%{--</label>--}%
%{----}%
%{--<ul class="one-to-many">--}%
%{--<g:each in="${erpDataPreprocessingInstance?.dataFiles?}" var="d">--}%
%{--<li><g:link controller="dataFile" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></li>--}%
%{--</g:each>--}%
%{--<li class="add">--}%
%{--<g:link controller="dataFile" action="create" params="['erpDataPreprocessing.id': erpDataPreprocessingInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'dataFile.label', default: 'File')])}</g:link>--}%
%{--</li>--}%
%{--</ul>--}%

%{--</div>--}%





