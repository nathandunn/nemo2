<%@ page import="edu.uoregon.nic.nemo.portal.Laboratory" %>

<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'experiment', 'error')} required">
    <label>
        Associated <g:message code="laboratory.experiment.label" default="Experiment"/>
    </label>
    <g:if test="${laboratoryInstance?.experiments}">
        <g:select multiple="true" name="experiments" from="${edu.uoregon.nic.nemo.portal.Experiment.findAllByLaboratoryIsNullOrLaboratory(laboratoryInstance)}"
                  optionValue="identifier" optionKey="id" value="${laboratoryInstance?.experiments}"
        >
        </g:select>
    </g:if>
    <g:elseif test="${edu.uoregon.nic.nemo.portal.Experiment.findAllByLaboratoryIsNull()?.size()==0}">
        Can only attach lab to experiment without a lab.
    </g:elseif>
    <g:else>
        <g:select multiple="true" name="experiments" from="${edu.uoregon.nic.nemo.portal.Experiment.findAllByLaboratoryIsNull()}"
                  optionValue="identifier" optionKey="id"
        >
        </g:select>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'users', 'error')} required">
    <label>
        Lab Members
    </label>
    <g:if test="${laboratoryInstance?.users}">
        <g:select multiple="true" name="users" from="${edu.uoregon.nic.nemo.portal.SecUser.listOrderByFullName()}"
                  optionValue="fullName" optionKey="id" value="${laboratoryInstance?.users}"
        >
        </g:select>
    </g:if>
    <g:else>
        <g:select multiple="true" name="users" from="${edu.uoregon.nic.nemo.portal.SecUser.listOrderByFullName()}"
                  optionValue="fullName" optionKey="id"
        >
        </g:select>
    </g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry number="1.1" term="NEMO_7431000" key="Lab ID" required="true"/>
    <g:textField name="identifier" value="${laboratoryInstance?.identifier}" />
</div>

<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'institution', 'error')} ">
    <g:propertyEntry key="Institution" number="1.2" term="NEMO_1725000" required="true"/>
    <g:textField name="institution" value="${laboratoryInstance?.institution}" size="80" />
    %{--<g:select name="institution" from="${institutions}"--}%
              %{--optionKey="url" optionValue="name" value="${laboratoryInstance.institution}"--}%
              %{--noSelection="[null: '- Other -']"--}%
              %{--onchange="--}%
                  %{--if(this.value!='null'){--}%
                    %{--document.getElementById('otherInstitution').value = '';--}%
                  %{--}--}%
                                %{--"/>--}%
</div>

%{--<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'institution', 'error')} ">--}%
    %{--<label>--}%
        %{--Other Institution--}%
    %{--</label>--}%
    %{--<g:if test="${laboratoryInstance.institution in institutions.url}">--}%
        %{--<g:textField name="otherInstitution" value="" size="60"/>--}%
    %{--</g:if>--}%
    %{--<g:else>--}%
        %{--<g:textField name="otherInstitution" value="${laboratoryInstance.institution}" size="80"/>--}%
    %{--</g:else>--}%
%{--</div>--}%

<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'principalInvestigatorRole', 'error')} ">
    <g:propertyEntry key="Principal Investigator (PI)" number="1.3" term="OBI_0000103" required="true"/>
    <g:textField size="80" name="principalInvestigatorRole" value="${laboratoryInstance?.principalInvestigatorRole}"/>

    %{--<g:select name="principalInvestigatorRole"--}%
              %{--from="${edu.uoregon.nic.nemo.portal.PrincipalInvestigator.findAll(sort: "url", order: "asc")}"--}%
              %{--optionKey="url" optionValue="nameOnly" value="${laboratoryInstance.principalInvestigatorRole}"--}%
              %{--noSelection="[null: '- Other -']"--}%
              %{--onchange="--}%
                %{--if(this.value!='null'){--}%
                %{--document.getElementById('otherPi').value = '';--}%
    %{--}    "/>--}%
    %{--<g:textField size="80" name="principalInvestigatorRole" value="${laboratoryInstance?.principalInvestigatorRole}"/>--}%
</div>

%{--<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'principalInvestigatorRole', 'error')} ">--}%
    %{--<label>--}%
        %{--Other PI--}%
    %{--</label>--}%
    %{--<g:if test="${laboratoryInstance.principalInvestigatorRole in edu.uoregon.nic.nemo.portal.PrincipalInvestigator.findAll(sort: "url", order: "asc").url}">--}%
        %{--<g:textField name="otherPi" value="" size="60"/>--}%
    %{--</g:if>--}%
    %{--<g:else>--}%
        %{--<g:textField name="otherPi" value="${laboratoryInstance.principalInvestigatorRole}" size="80"/>--}%
    %{--</g:else>--}%
%{--</div>--}%

<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'emailAddressPrincipalInvestigator', 'error')} ">
    <g:propertyEntry key="PI email address" number="1.4" term="NEMO_8251000"/>
    <g:textField name="emailAddressPrincipalInvestigator" size="40"
                 value="${laboratoryInstance?.emailAddressPrincipalInvestigator}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: laboratoryInstance, field: 'principalInvestigatorPostalAddress', 'error')} ">
    <g:propertyEntry key="PI postal address" number="1.5" term="NEMO_0670000"/>
    <g:textArea rows="4" cols="60" name="principalInvestigatorPostalAddress"
                value="${laboratoryInstance?.principalInvestigatorPostalAddress}"/>
</div>





