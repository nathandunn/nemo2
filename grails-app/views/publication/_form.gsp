<%@ page import="edu.uoregon.nic.nemo.portal.Publication" %>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'experiment', 'error')} required">
    <label>
        Associated <g:message code="condition.experiment.label" default="Experiment"/>
    </label>
    <g:if test="${publicationInstance?.experiments}">
        <g:select multiple="true" name="experiments" from="${edu.uoregon.nic.nemo.portal.Experiment.listOrderByIdentifier()}"
         optionValue="identifier" optionKey="id" value="${publicationInstance?.experiments}"
        >
        </g:select>
    </g:if>
    <g:else>
        <g:select multiple="true" name="experiments" from="${edu.uoregon.nic.nemo.portal.Experiment.listOrderByIdentifier()}"
                  optionValue="identifier" optionKey="id"
        >
        </g:select>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'authors', 'error')} required">
    <label>
        Associated Author
    </label>
    <g:if test="${publicationInstance?.authors}">
        <g:select multiple="true" name="authors" from="${edu.uoregon.nic.nemo.portal.SecUser.listOrderByFullName()}"
                  optionValue="fullName" optionKey="id" value="${publicationInstance?.authors}"
        >
        </g:select>
    </g:if>
    <g:else>
        <g:select multiple="true" name="authors" from="${edu.uoregon.nic.nemo.portal.SecUser.listOrderByFullName()}"
                  optionValue="fullName" optionKey="id"
        >
        </g:select>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'identifier', 'error')} ">
    <g:propertyEntry key="Publication ID" number="3.1" term="NEMO_0038000" required="true"/>
    <g:textField name="identifier" value="${publicationInstance?.identifier}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'publicationType', 'error')} ">
    <g:propertyEntry key="Publication type" number="3.2" term="Narrative_Resource" required="true"/>
    <g:select name="type.id" from="${edu.uoregon.nic.nemo.portal.PublicationType.listOrderByName()}" optionKey="id" optionValue="nameOnly" value="${publicationInstance?.type?.id}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'familyName', 'error')} ">
    <g:propertyEntry key="First author family name" number="3.3" term="NEMO_3229000" />
    <g:textField name="familyName" value="${publicationInstance?.familyName}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'publicationDate', 'error')} ">
    <g:propertyEntry key="Publication year (YYYY)" number="3.4" term="NEMO_1264000"  required="true"/>
    <g:field type="number" name="publicationDate" value="${publicationInstance.publicationDate}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'titlePaper', 'error')} ">
    <g:propertyEntry key="Paper title" number="3.5" term="NEMO_1010000"/>
    <g:textField name="titlePaper" value="${publicationInstance?.titlePaper}" size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'titleVolume', 'error')} ">
    <g:propertyEntry key="Volume title" number="3.6" term="NEMO_5339000"/>
    <g:textField name="titleVolume" value="${publicationInstance?.titleVolume}"  size="60"/>
</div>

<div class="fieldcontain ${hasErrors(bean: publicationInstance, field: 'digitalObjectIdentifier', 'error')} ">
    <g:propertyEntry key="Digital Object Identifier (DOI)" number="3.7" term="NEMO_2062000" required="true"/>
    <g:textField name="digitalObjectIdentifier" value="${publicationInstance?.digitalObjectIdentifier}"  size="60"/>
</div>





