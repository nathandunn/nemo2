<%@ page import="edu.uoregon.nic.nemo.portal.Help" %>



<div class="fieldcontain ${hasErrors(bean: helpInstance, field: 'emailFrom', 'error')} ">
	<label for="emailFrom">
		<g:message code="help.emailFrom.label" default="Email From" />
		
	</label>
	<g:field type="email" name="emailFrom" value="${helpInstance?.emailFrom}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: helpInstance, field: 'subject', 'error')} ">
	<label for="subject">
		<g:message code="help.subject.label" default="Subject" />
		
	</label>
	<g:textField name="subject" value="${helpInstance?.subject}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: helpInstance, field: 'message', 'error')} ">
	<label for="message">
		<g:message code="help.message.label" default="Message" />
		
	</label>
	<g:textField name="message" value="${helpInstance?.message}"/>
</div>

