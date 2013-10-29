<%@ page import="edu.uoregon.nic.nemo.portal.DataSet;" %>



<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'experiment', 'error')} required">
    <label>
        Associated <g:message code="condition.experiment.label" default="Experiment"/>
    </label>
    <g:if test="${erpAnalysisResultInstance?.experiment?.id}">
        <g:hiddenField name="experiment.id" value="${erpAnalysisResultInstance?.experiment?.id}"/>
        <g:showIdentifier instance="${erpAnalysisResultInstance?.experiment}" controller="experiment"/>
    </g:if>
    <g:else>
        <g:hiddenField name="experiment.id" value="${experimentInstance?.id}"/>
        <g:showIdentifier instance="${experimentInstance}" controller="experiment"/>
    </g:else>
</div>


<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'erpPatternExpression', 'error')} ">
    <label>
        Associated <g:message code="condition.erpPatternExpression.label" default="ERP Pattern Extraction"/>
    </label>
    <g:select name="erpPatternExpression.id" from="${erpPatternExtractions}"
              optionKey="id" optionValue="artifactFileName"
              noSelection="[null: '- None -']"
              value="${erpAnalysisResultInstance?.erpPatternExpression?.id}"/>
</div>


<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'artifactFileName', 'error')}  required">
    <g:propertyEntry key="ERP Analysis Results ID" number="11.1" term="NEMO_7123457" required="true"/>
    <g:if test="${erpAnalysisResultInstance.id}">
        <g:textField name="artifactFileName" value="${erpAnalysisResultInstance?.artifactFileName}" size="60"/>
    </g:if>
    <g:else>
        %{--<div class="gray-out">Generated automatically</div>--}%
        <div class="gray-out">Generated automatically</div>
    </g:else>
</div>

%{--<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'artifactFileName', 'error')} required">--}%
%{--<g:propertyEntry number="11.1" term="NEMO_3087000" key="Artifact File Name"/>--}%
%{--<g:propertyEntry key="ERP Analysis Results ID" number="11.1" term="NEMO_7123457" required="true"/>--}%
%{--<g:textField name="identifier" value="${erpAnalysisResultInstance?.artifactFileName}" size="60"/>--}%
%{--<g:link controller="erpAnalysisResult" action="download"--}%
%{--id="${erpAnalysisResultInstance.id}">--}%
%{--<g:img dir="images/icon" file="download2.png"/>--}%
%{--</g:link>--}%

%{--<div class="property-value">--}%
%{--Last upload:--}%
%{--<g:if test="${erpAnalysisResultInstance.lastUploaded}">--}%
%{--<g:formatDate date="${erpAnalysisResultInstance.lastUploaded}" type="datetime" style="LONG"--}%
%{--timeStyle="SHORT"/>--}%
%{--</g:if>--}%
%{--<g:else>--}%
%{--N/A--}%
%{--</g:else>--}%

%{--<g:if test="${erpAnalysisResultInstance.isRdfInProcess()}">--}%
%{--Classifying ... can not upload new file--}%
%{--Started <g:formatDate date="${erpAnalysisResultInstance.startClassification}" type="datetime"--}%
%{--style="LONG"--}%
%{--timeStyle="SHORT"/>--}%
%{--</g:if>--}%
%{--<g:else>--}%
%{--<g:if test="${erpAnalysisResultInstance.startClassification && erpAnalysisResultInstance.endClassification}">--}%
%{--<br/>--}%
%{--Classification Time--}%
%{--${erpAnalysisResultInstance.processingMinutes()} minutes--}%
%{--</g:if>--}%
%{--<g:uploadForm action="upload" id="target" name="target">--}%
%{--<g:hiddenField name="id" value="${erpAnalysisResultInstance.id}"/>--}%
%{--<input type="file" id="file" name="newRdf"/>--}%
%{--<script type="text/javascript">--}%
%{--jQuery('#file').change(function () {--}%
%{--jQuery('#target').submit();--}%
%{--});--}%
%{--</script>--}%
%{--<input type="submit" value="Upload"/>--}%
%{--</g:uploadForm>--}%
%{--</g:else>--}%
%{--</div>--}%
%{--</div>--}%


<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'dependentVariable', 'error')}  required">
    %{--<g:propertyEntry key="Condition of Interest" number="11.2" term="NEMO_2494000" required="false"/>--}%
    <g:propertyEntry key="Condition of Interest"  term="NEMO_2494000" required="false"/>

    <g:renderOntological term="NEMO_2494000" input="${conditionOfInterest}"/>
    %{--<g:select name="dependentVariable.id"--}%
              %{--from="${edu.uoregon.nic.nemo.portal.AnalysisVariable.listOrderByName()}"--}%
              %{--optionKey="id" optionValue="name"--}%
              %{--value="${erpAnalysisResultInstance?.dependentVariable?.id}"--}%
              %{--class="many-to-one"/>--}%
</div>

<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'independentVariable', 'error')}  required">
    %{--<g:propertyEntry key="Baseline Condition" number="11.3" term="NEMO_9335000" required="false"/>--}%
    <g:propertyEntry key="Baseline Condition" term="NEMO_9335000" required="false"/>

    <g:renderOntological term="NEMO_9335000" input="${baselineCondition}"/>
    %{--<g:select name="independentVariable.id"--}%
              %{--from="${edu.uoregon.nic.nemo.portal.AnalysisVariable.listOrderByName()}"--}%
              %{--optionKey="id" optionValue="name"--}%
              %{--value="${erpAnalysisResultInstance?.independentVariable?.id}"--}%
              %{--class="many-to-one"/>--}%
</div>


<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'analysisMethod', 'error')}  required">
    <g:propertyEntry key="Statistical Analysis Method (ANOVA, ttest, etc.)" number="11.4" term="NEMO_6879000"
                     required="false"/>
    <g:select name="analysisMethod.id"
              from="${edu.uoregon.nic.nemo.portal.AnalysisMethod.listOrderByName()}"
              optionKey="id" optionValue="name"
              value="${erpAnalysisResultInstance?.analysisMethod?.id}"
              class="many-to-one"/>
</div>


<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'significanceThreshold', 'error')}  required">
    <g:propertyEntry key="Statistical Significance Threshold" number="11.5" term="NEMO_8259000" required="false"/>
    <g:textField name="significanceThreshold" value="${erpAnalysisResultInstance.significanceThreshold}"/>
    %{--<g:select name="set.id"--}%
    %{--optionKey="id" optionValue="name"--}%
    %{--value="${erpAnalysisResultInstance?.set?.id}"--}%
    %{--class="many-to-one"/>--}%
</div>

<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'thresholdQuality', 'error')}  required">
    <g:propertyEntry key="Statistical Threshold Quality (corrected, uncorrected…)" number="11.6" term="NEMO_6587000"
                     required="false"/>
    <g:select name="thresholdQuality.id"
              from="${edu.uoregon.nic.nemo.portal.ThresholdQuality.listOrderByName()}"
              optionKey="id" optionValue="name"
              value="${erpAnalysisResultInstance?.thresholdQuality?.id}"
              class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'format', 'error')}  required">
    <g:propertyEntry key="Data format" number="11.7" term="NEMO_1194000" value="${erpAnalysisResultInstance.format}"
                     required="true"/>
    <g:select name="format.id"
              from="${edu.uoregon.nic.nemo.portal.DataFormat.findByNameLike("rdf%")}"
              optionKey="id" optionValue="name"
              value="${erpAnalysisResultInstance?.format?.id}"
              class="many-to-one"/>
</div>


<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'dataSet', 'error')}  required">
    <g:propertyEntry key="Data content" number="11.8" term="IAO_0000100" required="true"/>
    <g:select name="set.id"
              from="${edu.uoregon.nic.nemo.portal.DataSet.findAll("from DataSet ds where url like '%NEMO_3872000' ")}"
              optionKey="id" optionValue="name"
              value="${erpAnalysisResultInstance?.set?.id}"
              class="many-to-one"/>
</div>

%{--<g:if test="${!erpAnalysisResultInstance.id}">--}%

%{--<div class="fieldcontain ${hasErrors(bean: erpAnalysisResultInstance, field: 'artifactFileName', 'error')}  required">--}%
%{--<g:propertyEntry number="11.4" term="NEMO_3087000" key="Artifact file name"/>--}%
%{--<g:addAfterCreating/>--}%
%{--</g:if>--}%



