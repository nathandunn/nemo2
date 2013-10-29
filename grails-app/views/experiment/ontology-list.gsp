<%@ page import="edu.uoregon.nic.nemo.portal.Experiment" %>
<!doctype html>
<html>
<head>
    %{--<meta name="layout" content="main">--}%
    <g:set var="entityName" value="${message(code: 'experiment.label', default: 'Experiment')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    %{--<r:require module="jquery"/>--}%
</head>

<body>

%{--${subclasses}--}%
%{--ASDFASDF--}%
%{--<g:renderStringArray delimiter="<br/>" inputs="${subclasses}"/>--}%
%{--<ul>--}%
    <g:each in="${subclasses}" var="subclass" status="i">
        %{--<li>--}%
            %{--[${subclass.trim()}]--}%
            <g:renderUrl input="${subclass.trim()}"/>
        %{--${i.}--}%
        %{--</li>--}%

    </g:each>
%{--</ul>--}%

</body>
</html>

