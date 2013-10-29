<%@ page import="edu.uoregon.nic.nemo.portal.DataSet;" %>



<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'experiment', 'error')} required">
    <label>
        Associated <g:message code="condition.experiment.label" default="Experiment"/>
    </label>
    <g:if test="${erpPatternExtractionInstance?.experiment?.id}">
        <g:hiddenField name="experiment.id" value="${erpPatternExtractionInstance?.experiment?.id}"/>
        <g:showIdentifier instance="${erpPatternExtractionInstance?.experiment}" controller="experiment"/>
    </g:if>
    <g:else>
        <g:hiddenField name="experiment.id" value="${experimentInstance?.id}"/>
        <g:showIdentifier instance="${experimentInstance}" controller="experiment"/>
    </g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'erpDataPreprocessing', 'error')} ">
    <label>
        Associated <g:message code="condition.experiment.label" default="Data Preprocessing"/>
    </label>
    <g:select name="erpDataPreprocessing.id" from="${erpDataPreprocessings}"
              optionKey="id" optionValue="identifier"
              noSelection="[null: '- None -']"
              value="${erpPatternExtractionInstance?.erpDataPreprocessing?.id}"/>
</div>

%{--<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'artifactFileName', 'error')}  required">--}%
%{--<g:propertyEntry number="10.4" term="NEMO_3087000" key="Artifact file name"/>--}%
%{--<g:addAfterCreating/>--}%
%{--</g:if>--}%

<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'artifactFileName', 'error')}  required">
    <g:propertyEntry key="ERP Pattern Extraction ID" number="10.1" term="NEMO_7123457" required="true"/>
    <g:if test="${erpPatternExtractionInstance.id}">
        <g:textField name="artifactFileName" value="${erpPatternExtractionInstance?.artifactFileName}" size="60"/>
    </g:if>
    <g:else>
        <div class="gray-out">Generated automatically</div>
    </g:else>
</div>

<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'method', 'error')} ">
    <g:propertyEntry key="ERP Pattern Extraction Method" number="10.2" term="NEMO_8429000" required="true"/>
    <g:select name="method.id"
              from="${edu.uoregon.nic.nemo.portal.PatternExtractionMethod.listOrderByName()}"
              optionKey="id" optionValue="name"
              value="${erpPatternExtractionInstance?.method?.id}"
              class="many-to-one"/>
</div>


<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'baselineCondition', 'error')} ">
    <g:propertyEntry key="Condition for Comparison" number="10.3" term="NEMO_7752000" required="true"/>
    <g:select name="baselineCondition.id"
              from="${edu.uoregon.nic.nemo.portal.PatternExtractionCondition.listOrderByName()}"
              optionKey="id" optionValue="name"
              value="${erpPatternExtractionInstance?.baselineCondition?.id}"
              class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'conditionOfInterest', 'error')}  ">
    <g:propertyEntry key="Condition of Interest" number="10.4" term="NEMO_2813000" required="true"/>
    <g:select name="conditionOfInterest.id"
              from="${edu.uoregon.nic.nemo.portal.PatternExtractionCondition.listOrderByName()}"
              optionKey="id" optionValue="name"
              value="${erpPatternExtractionInstance?.conditionOfInterest?.id}"
              class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'format', 'error')} ">
    <g:propertyEntry key="Data format" number="10.5" term="NEMO_1194000" value="${erpPatternExtractionInstance.format}"
                     required="true"/>
    <g:select name="format.id"
              from="${edu.uoregon.nic.nemo.portal.DataFormat.findByUrl("http://purl.bioontology.org/NEMO/ontology/NEMO.owl#NEMO_2241000")}"
              optionKey="id" optionValue="name"
              value="${erpPatternExtractionInstance?.format?.id}"
              class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpPatternExtractionInstance, field: 'dataSet', 'error')} ">
    <g:propertyEntry key="Data content" number="10.6" term="IAO_0000100" required="true"/>
    <g:select name="set.id"
              from="${edu.uoregon.nic.nemo.portal.DataSet.findAllByNameLike("script%")}"
              optionKey="id" optionValue="name"
              value="${erpPatternExtractionInstance?.set?.id}"
              class="many-to-one"/>
</div>








