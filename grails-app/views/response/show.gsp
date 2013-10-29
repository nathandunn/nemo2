<%@ page import="edu.uoregon.nic.nemo.portal.Response" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'response.label', default: 'Response')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-response" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                               default="Skip to content&hellip;"/></a>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            %{--<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>--}%
            <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                              args="[entityName]"/></g:link></li>
            <li><g:link class="create" action="create" id="${responseInstance?.condition?.id}"><g:message code="default.new.label"
                                                                  args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="show-response" class="content scaffold-show" role="main">
    <h1>7. Response ${responseInstance.identifier}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

    <div class="associated-with">
        <strong>Associated Condition</strong>
        <g:link controller="condition" action="show"
                id="${responseInstance?.condition?.id}">
            ${responseInstance?.condition?.identifier}
        </g:link>
    </div>

    <table class="center-detail-table">
        <g:render template="/detail-table-header"/>
        <g:tableEntry key="Response Type ID" number="7.1" term="NEMO_9602000" value="${responseInstance.identifier}"/>
        <g:tableEntry key="Response collection device" number="7.2" term="NEMO_0000503"
                      ontological="${responseInstance.device}"/>
        <g:tableEntry key="Response collection software" number="7.3"
                      term="NEMO_1752000" ontological="${responseInstance.software}"/>
        <g:tableEntry key="Response type" number="7.4"
                      term="NEMO_0000467" ontological="${responseInstance.role}" related="response"/>
        <g:tableEntry key="Response modality" number="7.5"
                      term="NEMO_0000756" ontological="${responseInstance.modality}" related="response"/>
        <g:tableEntry key="Response deadline" number="7.6"
                      term="NEMO_2473000" value="${responseInstance.responseDeadline}" suffix=" ms"/>
        <g:tableEntry key="Accuracy (average)" number="7.7"
                      term="NEMO_0000431" value="${responseInstance.responseAccuracy}" suffix=" %"/>
        <g:tableEntry key="Response time (average)" number="7.8"
                      term="NEMO_0000433" value="${responseInstance.responseTime}" suffix=" ms"/>

    </table>


    <g:editable users="${responseInstance?.condition?.experiment?.laboratory?.users}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${responseInstance?.id}"/>
                <g:link class="edit" action="edit" id="${responseInstance?.id}"><g:message
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
