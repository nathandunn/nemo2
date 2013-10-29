<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'datafile.label', default: 'File')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<a href="#show-experiment" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                 default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
    </ul>
</div>

<table>
    <tr>
        <td>
            <div id="instances" class="content scaffold-show" role="main">
                <ul>
                    <g:each in="${instances}" var="instance">
                        <li>
                            <g:link action="show" controller="pattern" id="${instance.key}">${instance.value}</g:link>
                        </li>
                    </g:each>
                </ul>
            </div>
        </td>
    </tr>
</table>

</body>
</html>

