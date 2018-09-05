<style type="text/css">
	html {
		margin: 3px;
		padding: 3px;
	}
	body {
		font-family: verdana,helvetica,arial,sans-serif;
		font-size: 14px;
		margin: 3px;
		padding: 3px;
	}
	h5 {
		font-family: verdana,helvetica,arial,sans-serif;
		line-height: 100%;
		font-size: 16px;
		font-weight: normal;
		margin: 3px;
		padding: 3px;
	}
</style>

<h5>The following entry line(s) are RAM Operational Targets pending CPSC examination:</h5>
<h5>Re-testing action message functionality. CPSC examination required. First time manufacturer of consumer products subject to safety standards.</h5>

<table>
    <tr>
        <th>Identifier</th>
        <th>Port Code</th>
        <th>Operation</th>
        <th>Status Before</th>
        <th>Current Status</th>
    </tr>
    <#list reconRows as rowDat>
    	<tr><!-- iterate list -->
            <td>${rowDat.entryNumber}</td>
            <td>${rowDat.portCode}</td>
            <td>${rowDat.operation}</td>
            <td>${rowDat.oldStatus}</td>
            <td>${rowDat.newStatus}</td>
    	</tr>
    </#list>
</table>
