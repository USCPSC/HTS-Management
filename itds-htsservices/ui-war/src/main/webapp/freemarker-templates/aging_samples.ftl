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
<h5>It has been 45 days or more since the following Exam(s) were completed and the following Entry and Sample Number(s) still have a Disposition of Sample Pending Evaluation:</h5>
<table>
    <tr>
        <th>Exam Date</th>
        <th>Entry Number</th>
        <th>Sample Number</th>
        <th>Storage Location</th>
        <#list agingSamples as rowDat>
            <tr>
                <td style="text-align: right">${rowDat.dispositionDate}</td>
                <td style="text-align: right">${rowDat.entryLineNumber}</td>
                <td style="text-align: right">${rowDat.sampleNumber}</td>
                <td style="text-align: center">${rowDat.storageLocation}</td>
            </tr>
        </#list>
</table>
<h5>Please use the Exam Log to update the Disposition(s) from Sample Pending Evaluation to a final state Disposition if an update is warranted.</h5>
