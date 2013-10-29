
<%@ page import="edu.uoregon.nic.nemo.portal.Individual; edu.uoregon.nic.nemo.portal.client.BrainLocationEnum" %>
<%
    Individual individual = individuals.get(time)
%>
<g:if test="${individual?.statisticallySignificant && individual?.meanIntensity>0}">
    <div class="significant-positive">
</g:if>
<g:if test="${individual?.statisticallySignificant && individual?.meanIntensity<0}">
    <div class="significant-negative">
</g:if>
<g:else>
    <div class="meanIntensity">
</g:else>
<g:link action="showIndividuals" controller="erpAnalysisResult" id="${erpAnalysisResultInstance.id}"
        params="[time: time]">
    ${individual?.meanIntensity>0 ? "+" : ""}<g:formatNumber number="${individual?.meanIntensity}"
                    minFractionDigits="3"/><g:if test="${individual?.statisticallySignificant}">*</g:if>
</g:link>


</div>
