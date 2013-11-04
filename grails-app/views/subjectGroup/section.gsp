<fieldset>
    <legend>
        4. Study Participants (Group Characteristics)
    </legend>

    <div class="section-narrative">
        <g:toggleTextLength input="${experimentInstance.subjectGroupsNarrativeSummary}" maxLength="160"/>
    </div>

    <br/>

    <table class="center-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Size</th>
            <th>Classification</th>
            <th>Age (average years)</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${subjectGroups}" var="subject">
            <tr>
                <td>
                    <g:link controller="subjectGroup" action="show" id="${subject.id}">${subject.identifier}</g:link>
                    <g:if test="${subject.unverifiedCopy}">
                        <div class="error-small">Unverified</div>
                    </g:if>
                </td>
                <td>
                    ${subject.groupSize}
                </td>
                <td>
                    <g:renderOntological input="${subject.diagnosticClassifications}"/>
                </td>
                <td>
                    ${subject.groupAge}
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <g:editable users="${experimentInstance?.laboratory?.users}">
        <sec:ifAllGranted roles="ROLE_VERIFIED">
            <div class="nav" style="width: auto; ">
                <g:link class="create" action="create" controller="subjectGroup" id="${experimentInstance.id}">
                    New Subject Group
                </g:link>
                %{--<g:if test="${experimentInstance?.id}">--}%
                    %{--<g:select name="copySubjectGroup"--}%
                              %{--from="${edu.uoregon.nic.nemo.portal.SubjectGroup.listOrderByIdentifier()}"--}%
                              %{--optionKey="id" optionValue="identifier"--}%
                              %{--noSelection="['': '- Copy Subject Group To Experiment -']"--}%
                              %{--onchange="document.location.href='${request.contextPath}/subjectGroup/copySubjectGroup?id=${experimentInstance.id}&subjectGroupId='+this.value"/>--}%
                %{--</g:if>--}%
            </div>
        </sec:ifAllGranted>
    </g:editable>
</fieldset>

