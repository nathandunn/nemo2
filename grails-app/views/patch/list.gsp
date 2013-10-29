
<%@ page import="edu.uoregon.nic.nemo.portal.Patch" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'patch.label', default: 'Patch')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-patch" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-patch" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'patch.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="dateApplied" title="${message(code: 'patch.dateApplied.label', default: 'Date Applied')}" />
					
						<g:sortableColumn property="note" title="${message(code: 'patch.note.label', default: 'Note')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${patchInstanceList}" status="i" var="patchInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${patchInstance.id}">${fieldValue(bean: patchInstance, field: "name")}</g:link></td>
					
						<td><g:formatDate date="${patchInstance.dateApplied}" /></td>
					
						<td>${fieldValue(bean: patchInstance, field: "note")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${patchInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
