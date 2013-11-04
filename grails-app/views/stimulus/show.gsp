<%@ page import="edu.uoregon.nic.nemo.portal.Stimulus" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'stimulus.label', default: 'Stimulus')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-stimulus" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                               default="Skip to content&hellip;"/></a>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                                  args="[entityName]"/></g:link></li>
            </sec:ifAllGranted>
            <li><g:link class="create" action="create" id="${stimulusInstance?.condition?.id}"><g:message
                    code="default.new.label"
                    args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="show-stimulus" class="content scaffold-show" role="main">
    <h1>6. Stimulus ${stimulusInstance.identifier}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="associated-with">
        <strong>Associated Condition</strong>
        <g:link controller="condition" action="show"
                id="${stimulusInstance?.condition?.id}">
            ${stimulusInstance?.condition?.identifier}
        </g:link>
    </div>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>
        <g:tableEntry key="Stimulus Type ID" number="6.1" term="NEMO_1959000" value="${stimulusInstance.identifier}"/>
        <g:tableEntry key="Stimulus presentation device" number="6.2" term="NEMO_8446000"
                      ontological="${stimulusInstance.presentationDevice}"/>
        <g:tableEntry key="Stimulus presentation software" number="6.3" term="NEMO_0066000"
                      ontological="${stimulusInstance.presentationSoftware}"/>
        <g:tableEntry key="Target stimulus (type)" number="6.4" term="NEMO_0000413"
                      ontological="${stimulusInstance.targetType}" related="stimulus"/>
        <g:tableEntry key="Target stimulus modality" number="6.5" term="NEMO_0000443"
                      ontological="${stimulusInstance.targetModality}" related="stimulus"/>
        <g:tableEntry key="Target stimulus duration (ms)" number="6.6" term="NEMO_3331000"
                      value="${stimulusInstance.targetStimulusDuration} ms"/>
        <g:tableEntry key="Target stimulus quality (other)" number="6.7" term="NEMO_0000441"
                      ontological="${stimulusInstance.targetQualities}"/>
        <g:tableEntry key="Prime stimulus (type)" number="6.8" term="NEMO_0000413"
                      ontological="${stimulusInstance.primeType}" related="stimulus" hideIfNull="true"/>
        <g:tableEntry key="Prime stimulus modality" number="6.9" term="NEMO_0000443"
                      ontological="${stimulusInstance.primeModality}" related="stimulus" hideIfNull="true"/>
        <g:tableEntry key="Prime stimulus duration (ms)" number="6.10" term="NEMO_5109000"
                      value="${stimulusInstance.primeStimulusDuration}" hideIfNull="true"/>
        <g:tableEntry key="Prime stimulus quality (other)" number="6.11" term="NEMO_0000367"
                      ontological="${stimulusInstance.primeQuality}" hideIfNull="true"/>
        <g:tableEntry key="Prime-Target ISI (ms)" number="6.12" term="NEMO_8410000"
                      value="${stimulusInstance.interStimulusInterval}" hideIfNull="true"/>
        <g:tableEntry key="Prime-Target SOA (ms)" number="6.13" term="NEMO_2541000"
                      value="${stimulusInstance.stimulusOnsetAsynchrony}" hideIfNull="true"/>

    </table>


    <g:editable users="${stimulusInstance?.condition?.experiment?.laboratory?.users}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${stimulusInstance?.id}"/>
                <g:link class="edit" action="edit" id="${stimulusInstance?.id}"><g:message
                        code="default.button.edit.label"
                        default="Edit"/></g:link>
                <g:actionSubmit class="delete" action="delete"
                                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </fieldset>
        </g:form>
    </g:editable>
</div>
</body>
</html>
