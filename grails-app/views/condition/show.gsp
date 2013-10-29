<%@ page import="edu.uoregon.nic.nemo.portal.Condition" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'condition.label', default: 'Condition')}"/>
<title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<div class="nav" role="navigation">
    <ul>
        <li><g:link class="list" action="list">All ${entityName}s</g:link></li>
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <li><g:link class="create" action="create" id="${conditionInstance?.experiment?.id}"><g:message
                    code="default.new.label"
                    args="[entityName]"/></g:link></li>
        </sec:ifAllGranted>
    </ul>
</div>

<div id="show-condition" class="content scaffold-show" role="main">
    <h1>5. Experiment Condition</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <g:if test="${conditionInstance.unverifiedCopy}">
        <div class="error-with">
            <strong>Unverified</strong>
        </div>
    </g:if>

    <div class="associated-with">
        <strong>Associated Experiment</strong>
        <g:link controller="experiment" action="show"
                id="${conditionInstance.experiment.id}">
            ${conditionInstance.experiment.identifier}
        </g:link>
    </div>

    <div class="section-narrative">
        <g:toggleTextLength input="${conditionInstance.experiment.conditionsNarrativeSummary}" maxLength="350"/>
    </div>
    <br/>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>

        <g:tableEntry key="Experimental Condition ID" number="5.1" term="NEMO_0000536"
                      value="${conditionInstance.identifier}"/>
        <g:tableEntry key="Experimental Condition (Type)" number="5.2" term="NEMO_0000382"
                      ontological="${conditionInstance?.types}" related="condition"/>
        <g:tableEntry key="Task (Instructions)" number="5.3" term="NEMO_0000383"
                      ontological="${conditionInstance?.taskInstruction}" related="condition"/>

        %{--<g:link controller="condition" action="list" params="[related:conditionInstance?.taskInstruction]"--}%
        %{--> [related] </g:link>--}%
        <g:tableEntry key="Number trials (per condition)" number="5.4" term="NEMO_6697000"
                      value="${conditionInstance.numberTrials}"/>

    </table>


    <g:editable users="${conditionInstance?.experiment?.laboratory?.users}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${conditionInstance?.id}"/>
                <g:link class="edit" action="edit" id="${conditionInstance?.id}"><g:message
                        code="default.button.edit.label"
                        default="Edit"/></g:link>
                <g:actionSubmit class="delete" action="delete"
                                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </fieldset>
        </g:form>
        <br/>
        <br/>
    </g:editable>


    <div class="indent-dependent">
        <g:include view="stimulus/section.gsp" model="[stimuli: stimulusList]"/>

        <g:include view="response/section.gsp" model="[responses: responseList]"/>
    </div>

</div>
</body>
</html>
