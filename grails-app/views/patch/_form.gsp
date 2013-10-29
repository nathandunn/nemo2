<%@ page import="edu.uoregon.nic.nemo.portal.Patch" %>



<div class="fieldcontain ${hasErrors(bean: patchInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="patch.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${patchInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: patchInstance, field: 'dateApplied', 'error')} required">
	<label for="dateApplied">
		<g:message code="patch.dateApplied.label" default="Date Applied" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateApplied" precision="day"  value="${patchInstance?.dateApplied}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: patchInstance, field: 'note', 'error')} ">
	<label for="note">
		<g:message code="patch.note.label" default="Note" />
		
	</label>
	<g:textField name="note" value="${patchInstance?.note}"/>
</div>

