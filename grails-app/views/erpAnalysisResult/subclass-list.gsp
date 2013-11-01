<%@ page import="edu.uoregon.nic.nemo.portal.SearchService; java.util.regex.Pattern; java.util.regex.Matcher" %>
<!doctype html>
<html>
<head>
    <r:require modules="jquery"/>

    <r:layoutResources/>
</head>

<body>
<br/>

<div class="hideshow-buttons">
    <input id="hideButton" type="button" value="Hide Images" onclick="
        $('.erp-image').hide();
        $('#showButton').show();
        $('#hideButton').hide();
    "/>

    <input id="showButton" type="button" style="display: none;" value="Show Images" onclick="
        $('.erp-image').show();
        $('#showButton').hide();
        $('#hideButton').show();
    "/>
</div>
<br/>
<table class="embedded-table">
    <g:if test="${instances.size() == 0}">
        <div style="color: gray; text-align: center;">
            NO SIGNIFICANT EFFECTS
        </div>
    </g:if>
    <g:else>
        <thead>
        <tr>
            <th>
                Extracted ERP Pattern
            </th>
            <th>
                Reconstructed ERP Data
            </th>
            <th>
                ERP Difference Wave Effect
            </th>
        </tr>
        </thead>

        <g:each in="${instances}" var="instance" status="i">
            <tr>
                <td style="width: 50%;">
                    <a name="${instance.key}"></a>

                    <div class="erp-pattern-title">
                        <g:createIndividualLink time="${instance.key}" value="${erpAnalysisResult}"/>
                    </div>
                    <br/>


                    <g:ifImage key="${instance.key.encodeAsURL()}">
                        <g:link class="erp-image" controller="patternImage" action="viewImage"
                                id="${instance.key.encodeAsURL()}">
                            <img style="max-width: 400px;"
                                 src="${createLink(controller: 'patternImage', action: 'viewImage', id: instance.key.encodeAsURL())}"
                                 alt="${instance.key} Image"/>
                        </g:link>

                    </g:ifImage>

                    <g:if test="${edit}">
                        <g:uploadForm action="upload" controller="patternImage">
                            <g:hiddenField name="patternName" value="${instance.key.encodeAsURL()}"/>
                            <g:hiddenField name="id" value="${erpAnalysisResult.id}"/>
                            <input type="file" name="newImage"/>
                            <input type="submit" value="Upload"/>
                        </g:uploadForm>


                        <g:ifImage key="${instance.key.encodeAsURL()}">
                            <g:link action="deleteImage" controller="patternImage" id="${instance.key.encodeAsURL()}">
                                Delete
                            </g:link>
                        </g:ifImage>
                    </g:if>
                </td>

                <td>
                    &nbsp;<br/>

                    <g:ifRawImage key="${instance.key.encodeAsURL()}">
                        <g:link class="erp-image" controller="patternImage" action="viewRawImage"
                                id="${instance.key.encodeAsURL()}">
                            <img style="max-width: 400px;"
                                 src="${createLink(controller: 'patternImage', action: 'viewRawImage', id: instance.key.encodeAsURL())}"
                                 alt="${instance.key} Image"/>
                        </g:link>
                    </g:ifRawImage>

                    <g:if test="${edit}">
                        <g:uploadForm action="uploadRaw" controller="patternImage">
                            <g:hiddenField name="patternName" value="${instance.key.encodeAsURL()}"/>
                            <g:hiddenField name="id" value="${erpAnalysisResult.id}"/>
                            <input type="file" name="newImage"/>
                            <input type="submit" value="Upload"/>
                        </g:uploadForm>

                        <g:ifRawImage key="${instance.key.encodeAsURL()}">
                            <g:link action="deleteRawImage" controller="patternImage"
                                    id="${instance.key.encodeAsURL()}">
                                Delete
                            </g:link>
                        </g:ifRawImage>
                    </g:if>
                </td>


                <td nowrap>
                    <g:if test="${instance.value.size() == 0}">
                        <div style="color: gray; text-align: center;">
                            NO SIGNIFICANT EFFECTS
                        </div>
                    </g:if>
                    <g:else>
                        <g:each in="${instance.value}" var="inferredClass" status="st">
                            <g:link controller="term" action="show" id="${inferredClass.url}"
                                    style="color: ${inferredClass?.label?.startsWith('unnamed') ? 'green' : 'blue'};">${inferredClass.label?.replaceAll("_", " ")}
                            </g:link>&nbsp;<g:link controller="pattern" action="show" id="${inferredClass.url}"><g:img
                                dir="images/icon" file="filter.png"/></g:link>
                            <br/>
                        </g:each>
                    </g:else>
                </td>

            </tr>
        </g:each>
    </g:else>
</table>

</body>
</html>

