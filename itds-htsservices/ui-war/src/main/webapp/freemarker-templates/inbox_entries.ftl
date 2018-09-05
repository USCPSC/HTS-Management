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
    th {
        margin: 2px;
        padding: 2px;
    }
</style>
<h5>${emailHeader}</h5>
<table>
    <tr>
        <th>Entry</th>
        <th>Status</th>
        <th>Port ID</th>
        <th>Port Name</th>
        <#list inboxEntryLines as rowDat>
            <tr>
                <td>${rowDat.entryNumber}/${rowDat.entryLineNumber}</td>
                <td>${rowDat.workflowStatus}</td>
                <td>${rowDat.portCode}</td>
                <td>${rowDat.portName}</td>
            </tr>
        </#list>
</table>
