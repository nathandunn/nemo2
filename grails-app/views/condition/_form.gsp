<%@ page import="edu.uoregon.nic.nemo.portal.Condition" %>

<div class="fieldcontain ${hasErrors(bean: conditionInstance, field: 'experiment', 'error')} required">
    <label for="experiment">
        Associated <g:message code="condition.experiment.label" default="Experiment"/>
        <span class="required-indicator">*</span>
    </label>
    <g:if test="${conditionInstance?.experiment?.id}">
        <g:hiddenField name="experiment.id" value="${conditionInstance?.experiment?.id}"/>
        <g:showIdentifier instance="${conditionInstance?.experiment}" controller="experiment"/>
    </g:if>
    <g:else>
        <g:hiddenField name="experiment.id" value="${experimentInstance?.id}"/>
        <g:showIdentifier instance="${experimentInstance}" controller="experiment"/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: conditionInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry key="Experimental Condition ID" number="5.1" term="NEMO_0000536" required="true"/>
    <g:textField name="identifier" value="${conditionInstance?.identifier}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: conditionInstance, field: 'experimentConditionType', 'error')} ">
    <g:propertyEntry key="Experimental Condition (Type)" number="5.2" term="NEMO_0000382"  required="true"/>
    <g:if test="${conditionInstance?.id}">
        <g:select optionValue="name" optionKey="id" name="addType"
                  from="${edu.uoregon.nic.nemo.portal.ConditionType.listOrderByName()-conditionInstance.types}"
                  noSelection="[null: '- Add Type -']"
                  onchange="
                  ${remoteFunction(
                          action: 'addType'
                          , controller: 'condition'
                          , params: '\'typeId=\' + this.value '
                          , id: conditionInstance.id
                          , method: 'POST'
                          , onSuccess: 'window.location =\'' + request.contextPath + '/condition/edit/' + conditionInstance.id + '\';'
                          , onError: '\'alert(\'error\');\''
                  )}"/>

        <g:each in="${conditionInstance.types?.sort(it?.name)}" var="type" status="status">
            <span id="type${type.id}">
                <g:link action="removeType"
                        params="[id: conditionInstance.id, typeId: type.id]"
                        controller="condition">X</g:link>
                <g:link action="show" controller="term"
                        id="${type.suffix}">${type.name}</g:link>
                ${status.intValue() < conditionInstance.types.size() - 1 ? "&bull;" : ""}
            </span>
        </g:each>
    </g:if>
    <g:else>
        <g:addAfterCreating/>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: conditionInstance, field: 'experimentInstruction', 'error')} ">
    <g:propertyEntry key="Task (Instructions)" number="5.3" term="NEMO_0000383"  required="true"/>
    <g:select name="taskInstruction.id" from="${edu.uoregon.nic.nemo.portal.TaskInstruction.listOrderByName()}"
              optionKey="id" optionValue="name" value="${conditionInstance?.taskInstruction?.id}"
              noSelection="[null: '- None -']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: conditionInstance, field: 'numberTrials', 'error')} ">
    <g:propertyEntry key="Number trials (per condition)" number="5.4" term="NEMO_6697000"/>
    <g:field type="number" name="numberTrials" value="${conditionInstance.numberTrials}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: conditionInstance, field: 'unverifiedCopy', 'error')} ">
    <label>
        Status
    </label>
    <g:if test="${!conditionInstance.unverifiedCopy}">
        <div id="verifiable" style="display: inline;">Verified <g:remoteLink action="unverify" update="verifiable"
                                                                             id="${conditionInstance.id}">Unverify</g:remoteLink></div>
    </g:if>
    <g:else>
        <div id="verifiable" style="display: inline;">
            Unverified
            <g:remoteLink action="verify" update="verifiable" id="${conditionInstance.id}">Verify</g:remoteLink>
        </div>
    </g:else>

</div>

