
<%@ page import="edu.uoregon.nic.nemo.portal.Help" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'help.label', default: 'Help')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-help" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-help" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="emailFrom" title="${message(code: 'help.emailFrom.label', default: 'Email From')}" />
					
						<g:sortableColumn property="subject" title="${message(code: 'help.subject.label', default: 'Subject')}" />
					
						<g:sortableColumn property="message" title="${message(code: 'help.message.label', default: 'Message')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${helpInstanceList}" status="i" var="helpInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${helpInstance.id}">${fieldValue(bean: helpInstance, field: "emailFrom")}</g:link></td>
					
						<td>${fieldValue(bean: helpInstance, field: "subject")}</td>
					
						<td>${fieldValue(bean: helpInstance, field: "message")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${helpInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
