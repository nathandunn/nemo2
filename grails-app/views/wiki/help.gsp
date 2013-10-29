<!doctype html>

<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'help.label', default: 'Help')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        %{--<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]"/></g:link></li>--}%
        %{--<li><g:link class="create" action="create"><g:message code="default.new.label"--}%
        %{--args="[entityName]"/></g:link></li>--}%
    </ul>
</div>

<div id="show-dataFile" class="content scaffold-show" role="main">
    <g:form action="submitHelp">
        <table>
            <tr>
                <td>From</td>
                <td>
                    <g:textField name="from"></g:textField>
                </td>
            </tr>
            <tr>
                <td>Subject</td>
                <td>
                    <g:textField name="subject"></g:textField>
                </td>
            </tr>
            <tr>
                <td>Message</td>
                <td>
                    <g:textArea name="message" cols="40" rows="40"></g:textArea>
                </td>
            </tr>
        </table>
        <fieldset class="buttons">
            %{--<g:link class="edit" action="edit" id="${dataFileInstance?.id}"><g:message code="default.button.edit.label"--}%
            %{--default="Edit"/></g:link>--}%
            <g:actionSubmit class="save" action="submitHelp"
                            value="${message(code: 'default.button.update.label', default: 'Submit')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
