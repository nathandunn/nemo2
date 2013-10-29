<%@ page import="edu.uoregon.nic.nemo.portal.EegDataCollection" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'eegDataCollection.label', default: 'EEG Data Acquisition')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<g:if test="${experimentHeader}">
<div class="nav" role="navigation">
    <ul>
        <li><g:link class="list" action="list"><g:message code="default.list.label"
                                                          args="[entityName]"/></g:link></li>
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <li><g:link class="create" action="create" id="${experimentHeader.id}"><g:message code="default.new.label"
                                                                                              args="[entityName]"/></g:link></li>
        </sec:ifAllGranted>
    </ul>
</div>
</g:if>

<div id="list-eegDataCollection" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>

    <g:render template="/common/relatedFilter" model="['related': related]"/>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="identifier" params="[related: related?.relatedLookup]"
                              title="${message(code: 'eegDataCollection.identifier.label', default: 'ID')}"/>

            <g:sortableColumn property="electrodeArrayLayout" params="[related: related?.relatedLookup]"
                              title="${message(code: 'eegDataCollection.electrodeArrayLayout.label', default: 'Electrode Array (Layout)')}"/>

            <g:sortableColumn property="samplingRateSetting" params="[related: related?.relatedLookup]"
                              title="${message(code: 'eegDataCollection.samplingRateSetting.label', default: 'Sampling Rate Setting (ms)')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${eegDataCollectionInstanceList}" status="i" var="eegDataCollectionInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    <g:showIdentifier instance="${eegDataCollectionInstance}"/>
                </td>


                <td>
                    <g:renderOntological input="${eegDataCollectionInstance.electrodeArrayLayout}"
                                         related="eegDataCollection"/>
                </td>

                <td>
                    ${fieldValue(bean: eegDataCollectionInstance, field: "samplingRateSetting")}
                </td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${eegDataCollectionInstanceTotal}" id="${experimentHeader?.id}"
                    params="[related: related?.relatedLookup]"/>
    </div>
</div>
</body>
</html>
