<fieldset>
    <legend>3. Publications</legend>
    <g:if test="${publications}">
        <div class="fieldcontain">
                <g:each in="${publications}" var="pub">
                    <g:link controller="publication" action="show" id="${pub.id}">
                        ${pub.renderPubShort()}</g:link>
                    &nbsp;
                    &nbsp;
                    &nbsp;
                    [<g:link target="_blank" class="external-link"
                            url="http://dx.doi.org/${pub.digitalObjectIdentifier}"
                >Get <g:renderAnnotator input="${pub.type.url}"/></g:link>
                    ]
                    <br/>
                </g:each>
        </div>
    </g:if>
    <g:editable users="${experimentInstance?.laboratory?.users}">
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <br/>
            <div class="nav" style="width: auto;">
                <g:link class="create" action="create" controller="publication" id="${experimentInstance.id}">
                    New Publication
                </g:link>
            </div>
        </sec:ifAllGranted>
    </g:editable>
</fieldset>
