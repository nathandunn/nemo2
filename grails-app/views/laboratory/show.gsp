<%@ page import="org.apache.commons.validator.UrlValidator; edu.uoregon.nic.nemo.portal.Laboratory" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'laboratory.label', default: 'Laboratory')}"/>
    <title>Research Lab ${laboratoryInstance.identifier}</title>
</head>

<body>

<div id="show-laboratory" class="content scaffold-show" role="main">

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <h1>1. Research Lab ${laboratoryInstance.identifier}</h1>

    <div class="associated-with">
        <strong>Associated Experiments</strong>
        <g:each in="${laboratoryInstance.experiments}" var="experiment" status="status">
            <g:link action="show" id="${experiment.id}" controller="experiment">${experiment.identifier}</g:link>
        %{--${status.intValue()<laboratoryInstance.experiments.size()?", ":""}--}%
        </g:each>
    </div>

    <div class="associated-with">
        <strong>Associated Users</strong>
        <g:each in="${laboratoryInstance.users.sort()}" var="user" status="status">
            <g:link action="show" id="${user.id}" controller="secUser">${user.fullName}</g:link>
        %{--${status.intValue()<laboratoryInstance.experiments.size()?", ":""}--}%
        </g:each>
    </div>

    %{--<table class="show-label">--}%
    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>
        <g:tableEntry key="Lab ID" number="1.1" term="NEMO_7431000" value="${laboratoryInstance.identifier}"/>

        %{--<g:tableEntry key="Institution" number="1.2" term="NEMO_1725000" url="${laboratoryInstance.institution}"/>--}%
        <g:tableEntry key="Institution" number="1.2" term="NEMO_1725000" value="${laboratoryInstance.institution}"/>

        %{--<g:tableEntry key="Principal Investigator (PI)" number="1.3" term="OBI_0000103"--}%
                      %{--annotator="${laboratoryInstance.principalInvestigatorRole}"/>--}%
        <g:tableEntry key="Principal Investigator (PI)" number="1.3" term="OBI_0000103"
                      value="${laboratoryInstance.principalInvestigatorRole}"/>
        <g:tableEntry key="PI email address" number="1.4" term="NEMO_8251000"
                      value="${laboratoryInstance.emailAddressPrincipalInvestigator}"/>
        <g:tableEntry key="PI postal address" number="1.5" term="NEMO_0670000"
                      value="${laboratoryInstance.principalInvestigatorPostalAddress?.replaceAll("/", "<br/>")}"/>

    </table>


    <g:editable users="${laboratoryInstance.users}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${laboratoryInstance?.id}"/>
                <g:link class="edit" action="edit" id="${laboratoryInstance?.id}"><g:message
                        code="default.button.edit.label" default="Edit"/></g:link>
                <sec:ifAllGranted roles="ROLE_ADMIN">
                    <g:actionSubmit class="delete" action="delete"
                                    value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                </sec:ifAllGranted>
            </fieldset>
        </g:form>
    </g:editable>

    <br/>
    <br/>


    <div id="show-experiments" class="content scaffold-show" role="main">
        <table>
            <thead>
            <tr nowrap>
                <th>
                    ${message(code: 'experiment.identifier.label', default: 'Experiment ID')}
                </th>
                <th>
                    ${message(code: 'experiment.experimentalParadigm.label', default: 'Experimental Paradigm')}
                </th>
                <th>
                    ${message(code: 'experiment.conditionsNarrativeSummary.label', default: 'Conditions Narrative Summary')}
                </th>
            </tr>
            </thead>
            <tbody>

            <g:each in="${experiments}" var="experiment">
                <tr>
                    <td>
                <g:link action="show" controller="experiment"
                        id="${experiment.id}">${fieldValue(bean: experiment, field: "identifier")}</g:link></td>
                    </td>
                <td>
                    <g:renderOntological input="${experiment.experimentalParadigms}"/>
                </td>
                <td>
                    <g:toggleTextLength input="${experiment.narrativeSummary}" maxLength="100"/>
                </td>
                </tr>
            </g:each>
            </tbody>

        </table>
    </div>
</div>

</body>
</html>
