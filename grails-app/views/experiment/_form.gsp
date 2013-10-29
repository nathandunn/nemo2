<%@ page import="edu.uoregon.nic.nemo.portal.Experiment" %>

<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'laboratory', 'error')} required">
    <label for="laboratory">
        <g:message id="laboratory" code="experiment.associated.laboratory.label" default="Associated Laboratory"/>
        %{--<span class="required-indicator">*</span>--}%
    </label>
%{--<g:showIdentifier instance="${experimentInstance?.laboratory}"/>--}%
    <g:if test="${availableLabs}">
        <g:select name="laboratory.id" from="${availableLabs}" optionKey="id" optionValue="identifier"
        value="${experimentInstance?.laboratory?.id}"
        />
    %{--<g:hiddenField name="laboratory.id" value="${experimentInstance?.laboratory?.id}"/>--}%
    %{--<g:showIdentifier instance="${experimentInstance?.laboratory}"/>--}%
    </g:if>
    <g:elseif test="${experimentInstance?.laboratory?.id}">
        <g:hiddenField name="laboratory.id" value="${experimentInstance?.laboratory?.id}"/>
        <g:showIdentifier instance="${experimentInstance?.laboratory}"/>
    </g:elseif>
    <g:else>
        <span class="error">No Laboratory Associated with User</span>
    %{--<g:hiddenField name="laboratory.id" value="${laboratoryInstance?.id}" />--}%
    %{--<g:showIdentifier instance="${laboratoryInstance}"/>--}%
    </g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry key="ID" number="2.1" term="NEMO_0000537" required="true"/>
    <g:textField name="identifier" value="${experimentInstance?.identifier}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'experimentalParadigm', 'error')} ">
    <g:propertyEntry key="Experimental Paradigm" number="2.2" term="NEMO_0000379" required="true"/>

    <g:if test="${experimentInstance?.id}">
        <g:select optionValue="name" optionKey="id" name="addExperimentalParadigm"
                  from="${edu.uoregon.nic.nemo.portal.ExperimentalParadigm.listOrderByName()-experimentInstance?.experimentalParadigms}"
                  noSelection="[null: '- Add Paradigm -']"
                  onchange="
                  ${remoteFunction(
                          action: 'addExperimentalParadigm'
                          , controller: 'experiment'
                          , params: '\'paradigmId=\' + this.value '
                          , id: experimentInstance.id
                          , method: 'POST'
                          , onSuccess: 'window.location =\'' + request.contextPath + '/experiment/edit/' + experimentInstance.id + '\';'
                          , onError: 'alert(\'error\');'
                  )}"/>

        <g:each in="${experimentInstance.experimentalParadigms?.sort(it?.name)}" var="experimentalParadigm" status="status">
            <span id="paradigm${experimentalParadigm.id}">
                <g:link action="removeExperimentalParadigm"
                        params="[id: experimentInstance.id, paradigmId: experimentalParadigm.id]"
                        controller="experiment">X</g:link>
                <g:link action="show" controller="term"
                        id="${experimentalParadigm.suffix}">${experimentalParadigm.name}</g:link>
                ${status.intValue() < experimentInstance.experimentalParadigms.size() - 1 ? "&bull;" : ""}
            </span>
        </g:each>
    </g:if>
    <g:else>
        <g:addAfterCreating/>
    </g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'startDateForDataCollection', 'error')} ">
    <g:propertyEntry number="2.3" term="NEMO_8539000" key="Start Date (YYYY)"/>
    <g:field type="number" name="startDateForDataCollection" value="${experimentInstance.startDateForDataCollection}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'endDateForDataCollection', 'error')} ">
    <g:propertyEntry number="2.4" term="NEMO_0917000" key="End Date (YYYY)"/>
    <g:field type="number" name="endDateForDataCollection" value="${experimentInstance.endDateForDataCollection}"/>
</div>


%{--http://headit.aciss.uoregon.edu/studies/af331b72-8526-11e2-83cc-0050563ff472--}%
<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'headItLink', 'error')} ">
    <label>
        <a href="http://headit.aciss.uoregon.edu">HeadIT Link</a> (UUID only) <br/>
    </label>
    %{--http://headit.aciss.uoregon.edu/studies/af331b72-8526-11e2-83cc-0050563ff472/description --}%
    <g:field type="string" name="headItLink" value="${experimentInstance.headItLink}" size="80" placeholder="e.g., af331b72-8526-11e2-83cc-0050563ff472"/>
</div>

<br/>
<br/>


<h3>Narrative Summaries</h3>

<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'narrativeSummary', 'error')} ">
    <label for="narrativeSummary">
        <g:message code="experiment.narrativeSummary.label" default="Experiment Narrative Summary"/>
    </label>
    <g:textArea class="narrative-summary" rows="3" cols="80" name="narrativeSummary" value="${experimentInstance?.narrativeSummary}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'subjectGroupsNarrativeSummary', 'error')} ">
    <label for="subjectGroupsNarrativeSummary">
        <g:message code="experiment.subjectGroupsNarrativeSummary.label" default="Subject Groups Narrative Summary"/>

    </label>
    <g:textArea class="narrative-summary" rows="3" cols="80" name="subjectGroupsNarrativeSummary"
                value="${experimentInstance?.subjectGroupsNarrativeSummary}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'conditionsNarrativeSummary', 'error')} ">
    <label for="conditionsNarrativeSummary">
        <g:message code="experiment.conditionsNarrativeSummary2.label" default="Conditions Narrative Summary"/>
    </label>
    <g:textArea class="narrative-summary" rows="3" cols="80" name="conditionsNarrativeSummary"
                value="${experimentInstance?.conditionsNarrativeSummary}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'eegDataCollectionNarrativeSummary', 'error')} ">
    <label for="eegDataCollectionNarrativeSummary">
        <g:message code="experiment.eegDataCollectionNarrativeSummary.label"
                   default="Eeg Data Collection Narrative Summary"/>
    </label>
    <g:textArea class="narrative-summary" rows="3" cols="80" name="eegDataCollectionNarrativeSummary"
                value="${experimentInstance?.eegDataCollectionNarrativeSummary}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'erpDataPreprocessingsNarrativeSummary', 'error')} ">
    <label for="erpDataPreprocessingsNarrativeSummary">
        <g:message code="experiment.erpDataPreprocessingsNarrativeSummary.label"
                   default="Erp Data Preprocessings Narrative Summary"/>
    </label>
    <g:textArea class="narrative-summary" rows="3" cols="80" name="erpDataPreprocessingsNarrativeSummary"
                value="${experimentInstance?.erpDataPreprocessingsNarrativeSummary}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'erpPatternExtractionNarrativeSummary', 'error')} ">
    <label for="erpPatternExtractionNarrativeSummary">
        <g:message code="experiment.erpPatternExtractionNarrativeSummary.label" default="Erp Pattern Extraction Narrative Summary"/>
    </label>
    <g:textArea class="narrative-summary" rows="3" cols="80" name="erpPatternExtractionNarrativeSummary"
                value="${experimentInstance?.erpPatternExtractionNarrativeSummary}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'erpAnalysisResultNarrativeSummary', 'error')} ">
    <label for="erpAnalysisResultNarrativeSummary">
        <g:message code="experiment.erpAnalysisResultNarrativeSummary.label" default="Erp Analysis Result Narrative Summary"/>
    </label>
    <g:textArea class="narrative-summary" rows="3" cols="80" name="erpAnalysisResultNarrativeSummary"
                value="${experimentInstance?.erpAnalysisResultNarrativeSummary}"/>
</div>

%{--<div class="fieldcontain ${hasErrors(bean: experimentInstance, field: 'unverifiedCopy', 'error')} ">--}%
%{--<label for="unverifiedCopy">--}%
%{--<g:message code="experiment.unverifiedCopy.label" default="Unverified Copy" />--}%
%{----}%
%{--</label>--}%
%{--<g:checkBox name="unverifiedCopy" value="${experimentInstance?.unverifiedCopy}" />--}%
%{--</div>--}%


