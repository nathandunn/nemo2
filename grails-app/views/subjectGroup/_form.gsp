<%@ page import="edu.uoregon.nic.nemo.portal.SubjectGroup" %>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'experiment', 'error')} required">
    <label for="experiment">
        <g:message code="subjectGroup.experiment.label" default="Experiment"/>
        <span class="required-indicator">*</span>
    </label>
    <g:if test="${subjectGroupInstance?.experiment?.id}">
        <g:hiddenField name="experiment.id" value="${subjectGroupInstance?.experiment?.id}"/>
        <g:showIdentifier instance="${subjectGroupInstance?.experiment}"/>
    </g:if>
    <g:else>
        <g:hiddenField name="experiment.id" value="${experimentInstance?.id}"/>
        <g:showIdentifier instance="${experimentInstance}"/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry key="Subject Group ID" number="4.1" term="NEMO_2014000" required="true"/>
    <g:textField name="identifier" value="${subjectGroupInstance?.identifier}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'groupSize', 'error')} ">
    <g:propertyEntry key="Subject Group Size" number="4.2" term="NEMO_1365000" required="true"/>
    <g:field type="number" name="groupSize" value="${subjectGroupInstance.groupSize}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'diagnosticClassification', 'error')} ">
    <g:propertyEntry key="Diagnostic Classification" number="4.3" term="NEMO_5159000" required="true"/>

    <g:if test="${subjectGroupInstance?.id}">
        <g:select name="diagnosticClassification"
                  from="${edu.uoregon.nic.nemo.portal.DiagnosticClassification.listOrderByName() - subjectGroupInstance.diagnosticClassifications}"
                  optionKey="id" optionValue="name"
                  noSelection="['': '- Add Classification-']"
                  onchange="
                  ${remoteFunction(
                          action: 'addDiagnosticClassification'
                          , controller: 'subjectGroup'
                          , params: '\'classificationId=\' + this.value '
                          , id: subjectGroupInstance.id
                          , method: 'POST'
                          , onSuccess: 'window.location =\'' + request.contextPath + '/subjectGroup/edit/' + subjectGroupInstance.id + '\';'
                          , onError: 'alert(\'error\');'
                  )}"/>
        <g:each in="${subjectGroupInstance.diagnosticClassifications?.sort(it?.name)}" var="classification"
                status="status">
            <span id="classification${classification.id}">
                <g:link action="removeDiagnosticClassification"
                        params="[id: subjectGroupInstance.id, classificationId: classification.id]"
                        controller="subjectGroup">X</g:link>
                <g:link action="show" controller="term"
                        id="${classification.suffix}">${classification.name}</g:link>
                ${status.intValue() < subjectGroupInstance.diagnosticClassifications.size() - 1 ? "&bull;" : ""}
            </span>
        </g:each>
    </g:if>
    <g:else>
        <g:addAfterCreating/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'genus', 'error')} ">
    <g:propertyEntry key="Genus" number="4.4" term="NEMO_5621000" required="true"/>
    <g:hiddenField name="genus" value="http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_1614000"/>
    <select>
    <option>Homo</option>
    </select>
    %{--<g:textField name="genus" value="${subjectGroupInstance?.genus}"/>--}%
</div>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'species', 'error')} ">
    <g:propertyEntry key="Species" number="4.5" term="NEMO_3454000" required="true"/>
    %{--<g:textField name="species" value="${subjectGroupInstance?.species}"/>--}%
    <g:hiddenField name="species" value="http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_4093000"/>
    <select>
        <option>Sapien</option>
    </select>
</div>


<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'groupAge', 'error')} ">
    <g:propertyEntry key="Age (average)" number="4.6" term="NEMO_3819000" required="false"/>
    <g:field type="number" name="groupAge" value="${subjectGroupInstance.groupAge}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'numberMaleStudySubjectsRetainedForAnalysis', 'error')} ">
    <g:propertyEntry key="Number male subjects" number="4.7" term="NEMO_3593000"/>
    <g:field type="number" name="numberMaleStudySubjectsRetainedForAnalysis"
             value="${subjectGroupInstance.numberMaleStudySubjectsRetainedForAnalysis}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'numberRightHandedStudySubjectsRetainedForAnalysis', 'error')} ">
    <g:propertyEntry key="Number right-handed subjects" number="4.8" term="NEMO_0814000"/>
    <g:field type="number" name="numberRightHandedStudySubjectsRetainedForAnalysis"
             value="${subjectGroupInstance.numberRightHandedStudySubjectsRetainedForAnalysis}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'groupLanguage', 'error')} ">
    <g:propertyEntry key="Native language of majority of speakers" number="4.9" term="NEMO_6266000"/>
    <g:if test="${subjectGroupInstance?.id}">
        <g:select name="groupLanguage"
                  from="${edu.uoregon.nic.nemo.portal.Language.listOrderByName() - subjectGroupInstance.languages}"
                  optionKey="id" optionValue="name"
                  noSelection="['': '- Add Language -']"
                  onchange="
                  ${remoteFunction(
                          action: 'addLanguage'
                          , controller: 'subjectGroup'
                          , params: '\'languageId=\' + this.value '
                          , id: subjectGroupInstance.id
                          , method: 'POST'
                          , onSuccess: 'window.location =\'' + request.contextPath + '/subjectGroup/edit/' + subjectGroupInstance.id + '\';'
                          , onError: 'alert(\'error\');'
                  )}"/>
        <g:each in="${subjectGroupInstance?.languages?.sort(it?.name)}" var="language" status="status">
            <span id="language${language.id}">
                <g:link action="removeLanguage"
                        params="[id: subjectGroupInstance.id, languageId: language.id]"
                        controller="subjectGroup">X</g:link>
                <g:link action="show" controller="term"
                        id="${language.suffix}">${language.name}</g:link>
                ${status.intValue() < subjectGroupInstance.languages.size() - 1 ? "&bull;" : ""}
            </span>
        </g:each>

    </g:if>
    <g:else>
        <g:addAfterCreating/>
    </g:else>

</div>


<div class="fieldcontain ${hasErrors(bean: subjectGroupInstance, field: 'unverifiedCopy', 'error')} ">
    <label>
        Status
    </label>
    <g:if test="${!subjectGroupInstance.unverifiedCopy}">
        <div id="verifiable" style="display: inline;">Verified <g:remoteLink action="unverify" update="verifiable"
                                                                             id="${subjectGroupInstance.id}">Unverify</g:remoteLink></div>
    </g:if>
    <g:else>
        <div id="verifiable" style="display: inline;">
            Unverified
            <g:remoteLink action="verify" update="verifiable" id="${subjectGroupInstance.id}">Verify</g:remoteLink>
        </div>
    </g:else>

</div>


