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
<h5>The automated hold for the following entry line(s)indicate an Override in CBP systems. If CPSC did not concur with the Override, please contact the port regarding the present location of the cargo for possible CPSC examination. If the Override was pre-approved by CPSC, change the Current Status in the Portal to indicate "CPSC Doc Review" or "CI Unavailable"</h5>
<table>
    <tr>
        <th>Identifier</th>
        <th>Port Code</th>
        <th>Operation</th>
        <th>Status Before</th>
        <th>Current Status</th>
    </tr>
    <#list reconRows as rowDat>
        <tr>
            <td>${rowDat.entryNumber}</td>
            <td>${rowDat.portCode}</td>
            <td>${rowDat.operation}</td>
            <td>${rowDat.oldStatus}</td>
            <td>${rowDat.newStatus}</td>
        </tr>
    </#list>
</table>
