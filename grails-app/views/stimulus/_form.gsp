<%@ page import="edu.uoregon.nic.nemo.portal.Software; edu.uoregon.nic.nemo.portal.Stimulus" %>



<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'condition', 'error')} ">
    <label for="condition">
        Associated <g:message code="stimulus.condition.label" default="Condition"/>
    </label>
    <g:if test="${stimulusInstance?.condition?.id}">
        <g:hiddenField name="condition.id" value="${stimulusInstance?.condition?.id}"/>
        <g:showIdentifier instance="${stimulusInstance?.condition}"/>
    </g:if>
    <g:else>
        <g:hiddenField name="condition.id" value="${conditionInstance?.id}"/>
        <g:showIdentifier instance="${conditionInstance}"/>
    </g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry key="Stimulus Type ID" number="6.1" term="NEMO_1959000" required="true"/>
    <g:textField name="identifier" value="${stimulusInstance?.identifier}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'stimulusPresentationDevice', 'error')} ">
    <g:propertyEntry key="Stimulus presentation device" number="6.2" term="NEMO_8446000"/>
    <g:select name="presentationDevice.id"
              from="${edu.uoregon.nic.nemo.portal.StimulusPresentationDevice.listOrderByName()}"
              optionValue="name" optionKey="id" value="${stimulusInstance?.presentationDevice?.id}"
              noSelection="[null: '- None -']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'presentationSoftware', 'error')} ">
    <g:propertyEntry key="Stimulus presentation software" number="6.3" term="NEMO_0066000"/>
    %{--<g:textField name="stimulusPresentationSoftware" value="${stimulusInstance?.stimulusPresentationSoftware}"/>--}%
    <g:select name="presentationSoftware.id" from="${Software.listOrderByName()}"
              optionValue="name" optionKey="id" value="${stimulusInstance?.presentationSoftware?.id}"
              noSelection="[null: '- None -']"/>
</div>


<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'targetType', 'error')} ">
    <g:propertyEntry key="Target stimulus (type)" number="6.4" term="NEMO_6440000" required="true"/>
    %{--<g:textField name="targetStimulusType" value="${stimulusInstance?.targetStimulusType}"/>--}%
    <g:select name="targetType.id" from="${edu.uoregon.nic.nemo.portal.StimulusType.listOrderByName()}"
              optionValue="name" optionKey="id" value="${stimulusInstance?.targetType?.id}"
              noSelection="[null: '- None -']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'targetModality', 'error')} ">
    <g:propertyEntry key="Target stimulus modality" number="6.5" term="NEMO_0000443"  required="true"/>
    %{--<g:textField name="targetStimulusModality" value="${stimulusInstance?.targetStimulusModality}"/>--}%
    <g:select name="targetModality.id" from="${edu.uoregon.nic.nemo.portal.StimulusModality.listOrderByName()}"
              optionValue="name" optionKey="id" value="${stimulusInstance?.targetModality?.id}"
              noSelection="[null: '- None -']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'targetStimulusDuration', 'error')} ">
    <g:propertyEntry key="Target stimulus duration (ms)" number="6.6" term="NEMO_3331000" required="false"/>
    <g:field type="number" name="targetStimulusDuration" step="any" value="${stimulusInstance.targetStimulusDuration}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'targetStimulusQuality', 'error')} ">
    <g:propertyEntry key="Target stimulus quality (other)" number="6.7" term="NEMO_0000429"/>

    <g:if test="${stimulusInstance?.id}">
        <g:select optionValue="name" optionKey="id" name="addQuality"
                  from="${edu.uoregon.nic.nemo.portal.StimulusQuality.listOrderByName()-stimulusInstance?.targetQualities}"
                  noSelection="[null: '- Add Quality -']"
                  onchange="
                  ${remoteFunction(
                          action: 'addQuality'
                          , controller: 'stimulus'
                          , params: '\'qualityId=\' + this.value '
                          , id: stimulusInstance.id
                          , method: 'POST'
                          , onSuccess: 'window.location =\'' + request.contextPath + '/stimulus/edit/' + stimulusInstance.id + '\';'
                          , onError: '\'alert(\'error\');\''
                  )}"/>

        <g:each in="${stimulusInstance.targetQualities?.sort(it?.name)}" var="quality" status="status">
            <span id="quality${quality.id}">
                <g:link action="removeQuality"
                        params="[id: stimulusInstance.id, qualityId: quality.id]"
                        controller="stimulus">X</g:link>
                <g:link action="show" controller="term"
                        id="${quality.suffix}">${quality.name}</g:link>
                ${status.intValue() < stimulusInstance.targetQualities.size() - 1 ? "&bull;" : ""}
            </span>
        </g:each>
    </g:if>
    <g:else>
        <g:addAfterCreating/>
    </g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'primeStimulusType', 'error')} ">
    <g:propertyEntry key="Prime stimulus (type)" number="6.8" term="NEMO_0000413"/>
    %{--<g:textField name="primeStimulusType" value="${stimulusInstance?.primeStimulusType}"/>--}%
    <g:select name="primeType.id" from="${edu.uoregon.nic.nemo.portal.StimulusType.listOrderByName()}"
              optionValue="name" optionKey="id" value="${stimulusInstance?.primeType?.id}"
              noSelection="[null: '- None -']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'primeStimulusModality', 'error')} ">
    <g:propertyEntry key="Prime stimulus modality" number="6.9" term="NEMO_0000443"/>
    %{--<g:textField name="primeStimulusModality" value="${stimulusInstance?.primeStimulusModality}"/>--}%
    <g:select name="primeModality.id" from="${edu.uoregon.nic.nemo.portal.StimulusModality.listOrderByName()}"
              optionValue="name" optionKey="id" value="${stimulusInstance?.primeModality?.id}"
              noSelection="[null: '- None -']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'primeStimulusDuration', 'error')} ">
    <g:propertyEntry key="Prime stimulus duration (ms)" number="6.10" term="NEMO_5109000"/>
    <g:textField name="primeStimulusDuration" value="${stimulusInstance?.primeStimulusDuration}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'primeStimulusQuality', 'error')} ">
    <g:propertyEntry key="Prime stimulus quality (other)" number="6.11" term="NEMO_0000367"/>
    %{--<g:textField name="primeStimulusQuality" value="${stimulusInstance?.primeStimulusQuality}"/>--}%
    <g:select name="primeQuality.id" from="${edu.uoregon.nic.nemo.portal.StimulusQuality.listOrderByName()}"
              optionValue="name" optionKey="id" value="${stimulusInstance?.primeQuality?.id}"
              noSelection="[null: '- None -']"/>
</div>


<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'interStimulusInterval', 'error')} ">
    <g:propertyEntry key="Prime-Target Internal Stimulus Interval (ms)" number="6.12" term="NEMO_8410000"/>
    <g:field type="number" name="interStimulusInterval" step="any" value="${stimulusInstance.interStimulusInterval}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: stimulusInstance, field: 'stimulusOnsetAsynchrony', 'error')} ">
    <g:propertyEntry key="Prime-Target Stimulus Onset Asynchrony (ms)" number="6.13" term="NEMO_2541000" required="false"/>
    <g:field type="number" name="stimulusOnsetAsynchrony" step="any"
             value="${stimulusInstance.stimulusOnsetAsynchrony}"/>
</div>


