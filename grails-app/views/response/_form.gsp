<%@ page import="edu.uoregon.nic.nemo.portal.Response" %>



<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'condition', 'error')} ">
    <label for="condition">
        <g:message code="response.condition.label" default="Condition" />

    </label>
    %{--<g:hiddenField name="condition" id=""--}%
    <g:if test="${responseInstance?.condition?.id}">
        <g:hiddenField name="condition.id" value="${responseInstance?.condition?.id}" />
        <g:showIdentifier instance="${responseInstance?.condition}"/>
    </g:if>
    <g:else>
        <g:hiddenField name="condition.id" value="${conditionInstance?.id}" />
        <g:showIdentifier instance="${conditionInstance}"/>
    </g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry key="Response Type ID" number="7.1" term="NEMO_9602000" required="true"/>
    <g:textField name="identifier" value="${responseInstance?.identifier}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'responseDevice', 'error')} ">
    <g:propertyEntry key="Response collection device" number="7.2" term="NEMO_0000503"/>
    %{--<g:textField name="responseDevice" value="${responseInstance?.responseDevice}"/>--}%
    <g:select name="device.id" from="${edu.uoregon.nic.nemo.portal.ResponseDevice.listOrderByName()}"
              optionKey="id" optionValue="name" value="${responseInstance?.device?.id}"
              noSelection="[null:'- None -']"
    />
</div>


<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'experimentControlSoftware', 'error')} ">
    <g:propertyEntry key="Response collection software" number="7.3" term="NEMO_1752000" />
	%{--<g:textField name="experimentControlSoftware" value="${responseInstance?.experimentControlSoftware}"/>--}%
    <g:select name="software.id" from="${edu.uoregon.nic.nemo.portal.Software.listOrderByName()}"
              optionKey="id" optionValue="name" value="${responseInstance?.software?.id}"
              noSelection="[null:'- None -']"
    />
</div>

<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'responseRole', 'error')} ">
    <g:propertyEntry key="Response type" number="7.4" term="NEMO_0000467"  required="true"/>
    %{--<g:textField name="responseRole" value="${responseInstance?.responseRole}"/>--}%
    <g:select name="role.id" from="${edu.uoregon.nic.nemo.portal.ResponseRole.listOrderByName()}"
              optionKey="id" optionValue="name" value="${responseInstance?.role?.id}"
              noSelection="[null:'- None -']"
    />
</div>

<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'modality', 'error')} ">
    <g:propertyEntry key="Response modality" number="7.5" term="NEMO_0000756"  required="true"/>
    %{--<g:textField name="responseModality" value="${responseInstance?.responseModality}"/>--}%
    <g:select name="modality.id" from="${edu.uoregon.nic.nemo.portal.ResponseModality.listOrderByName()}"
              optionKey="id" optionValue="name" value="${responseInstance?.modality?.id}"
              noSelection="[null:'- None -']"
    />
</div>

<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'responseDeadline', 'error')} ">
    <g:propertyEntry key="Response deadline (ms)" number="7.6" term="NEMO_2473000" />
    <g:field type="number" name="responseDeadline" step="any" value="${responseInstance.responseDeadline}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'responseAccuracy', 'error')} ">
    <g:propertyEntry key="Accuracy % out of 100 (average)" number="7.7" term="NEMO_0000431" />
	<g:field type="number" name="responseAccuracy" step="any" value="${responseInstance.responseAccuracy}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: responseInstance, field: 'responseTime', 'error')} ">
    <g:propertyEntry key="Response time in ms (average)" number="7.8" term="NEMO_0000433" />
	<g:field type="number" name="responseTime" step="any" value="${responseInstance.responseTime}"/>
</div>


