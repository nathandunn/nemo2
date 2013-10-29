<g:if test="${experimentInstance?.laboratory}">
    <fieldset>
        <legend>
            1. Research Labs (General Features)
        </legend>
        <g:link controller="laboratory" action="show" id="${experimentInstance?.laboratory?.id}">
            %{--<g:renderAnnotator input="${experimentInstance?.laboratory?.principalInvestigatorRole}"/>--}%
            ${experimentInstance?.laboratory?.principalInvestigatorRole}
            at
            %{--<g:renderUrl input="${experimentInstance?.laboratory?.institution}"/>--}%
            ${experimentInstance?.laboratory?.institution}
            (${experimentInstance?.laboratory?.identifier})
        </g:link>
    </fieldset>
</g:if>
