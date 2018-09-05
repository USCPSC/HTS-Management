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
<h5>The following lines were automatically set to CBP Hold Approved by a Supply Chain Rule more than seven days ago.</h5>

<table>
    <tr>
        <td>Entry</td>
        <td>Line</td>
        <td>Status</td>
        <td>Port ID</td>
        <td>Port Name</td>
    </tr>
    <#list listSupplyChainEmailDataDto as rowDat>
        <tr>
            <td>${rowDat.entryNo}</td>
            <td>${rowDat.lineNo}</td>
            <td>${rowDat.workflowStatusDesc}</td>
            <td>${rowDat.portId}</td>
            <td>${rowDat.portName}</td>
        </tr>
    </#list>
</table>
