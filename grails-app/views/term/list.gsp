<%@ page import="edu.uoregon.nic.nemo.portal.Response" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'response.label', default: 'Term View')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-response" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                               default="Skip to content&hellip;"/></a>

<sec:ifAllGranted roles="ROLE_VERIFIED">
    <div class="nav" role="navigation">
        <ul>
            %{--<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>--}%
            <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                  args="[entityName]"/></g:link></li>
        </ul>
    </div>
</sec:ifAllGranted>

<div id="list-response" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>

%{--<g:render template="/common/relatedFilter" model="['related': related]"/>--}%

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <div class="pagination">
        <g:paginate total="${termInstanceTotal}" id="${experimentHeader?.id}"
                    params="[related: related?.relatedLookup]"/>
    </div>
    <table>
        <thead>
        <tr>
            %{--<g:sortableColumn property="label"--}%
            %{--title="${message(code: 'response.label.label', default: 'Label')}"/>--}%
            <th>
                Label
            </th>
            <g:sortableColumn property="uri"
                              title="${message(code: 'response.uri.label', default: 'URI')}"/>
            %{--<th>--}%
                %{--URI--}%
            %{--</th>--}%
            <th>
                Synonyms
            </th>
            %{--<g:sortableColumn property="role" params="[related: related?.relatedLookup]"--}%
            %{--title="${message(code: 'response.role.label', default: 'Response Type')}"/>--}%
            %{--<g:sortableColumn property="modality" params="[related: related?.relatedLookup]"--}%
            %{--title="${message(code: 'response.modality.label', default: 'Response Modality')}"/>--}%
        </tr>
        </thead>
        <tbody>
        <g:each in="${termInstanceList}" status="i" var="termInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td>
                    <g:link action="show" id="${termInstance.nemoId}">
                        ${termInstance.label}
                    </g:link>
                    %{--<g:showIdentifier instance="${termInstance}"/>--}%
                </td>

                <td>
                    <g:link action="show" id="${termInstance.nemoId}">
                        ${termInstance.nemoId}
                    </g:link>
                    %{--<g:renderOntological input="${termInstance.role}"  related="response"/>--}%
                </td>

                <td>
                    <g:each var="syn" in="${termInstance.synonyms}" status="st">
                        <g:if test="${syn.startsWith('http://')}">
                            <g:link url="${syn}">
                                ${syn}
                            </g:link>
                        </g:if>
                        <g:else>
                            ${syn}
                        </g:else>


                        <g:if test="${st.intValue() < termInstance.synonyms.size()-1}">
                            &bull;
                        </g:if>
                    </g:each>
                    %{--${termInstance.synonyms}--}%
                    %{--<g:renderOntological input="${termInstance.modality}"  related="response"/>--}%
                </td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${termInstanceTotal}" id="${experimentHeader?.id}"
                    params="[related: related?.relatedLookup]"/>
    </div>
</div>
</body>
</html>
