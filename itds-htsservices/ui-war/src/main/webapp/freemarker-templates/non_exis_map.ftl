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
<#list listNonExisMapEmailDto as rowDat>
<h5>${rowDat.actionMessage}</h5>
<table>
    <tr><td>Op Name:</td><td>${rowDat.opName}</td></tr>
    <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
    <tr><th colspan="2"> --- Entry Details --- </th></tr>
    <tr><td>Actual Arr Dt:</td><td>${rowDat.actualArrivalDate}</td></tr>
    <tr><td>Estimated Arr Dt:</td><td>${rowDat.estimatedArrivalDate}</td></tr>
    <tr><td>Release Date:</td><td>${rowDat.releaseDate}</td></tr>
    <tr><td>Entry:</td><td>${rowDat.entryNo}</td></tr>
    <tr><td>Line(s):</td><td>${rowDat.lines}</td></tr>
    <tr><td>HTS:</td><td>${rowDat.hts}</td></tr>
    <tr><td>BOL:</td><td>${rowDat.bol}</td></tr>
    <tr><td>Carrier:</td><td>${rowDat.carrier}</td></tr>
    <tr><td>MOT:</td><td>${rowDat.mot}</td></tr>
    <tr><td>Vessel:</td><td>${rowDat.vessel}</td></tr>
    <tr><td>Foreign Port:</td><td>${rowDat.foreignPort}</td></tr>
    <tr><td>Port Name:</td><td>${rowDat.portName}</td></tr>
    <tr><td>Unlading Port:</td><td>${rowDat.unlandingPort}</td></tr>
    <tr><td>FTZ:</td><td>${rowDat.ftz}</td></tr>
    <tr><th colspan="2"> --- Importer Details --- </th></tr>
    <tr><td>EIN:</td><td>${rowDat.importerEin}</td></tr>
    <tr><td>Name:</td><td>${rowDat.importerName}</td></tr>
    <tr><td>Address:</td><td>${rowDat.importerAddress}</td></tr>
    <tr><td>Address:</td><td>${rowDat.importerAddress2}</td></tr>
    <tr><td>City:</td><td>${rowDat.importerCity}</td></tr>
    <tr><td>State:</td><td>${rowDat.importerState}</td></tr>
    <tr><td>ZIP:</td><td>${rowDat.importerZip}</td></tr>
    <tr><th colspan="2"> --- Consignee Details --- </th></tr>
    <tr><td>EIN:</td><td>${rowDat.consigneeEin}</td></tr>
    <tr><td>Name:</td><td>${rowDat.consigneeName}</td></tr>
    <tr><td>Address:</td><td>${rowDat.consigneeAddress}</td></tr>
    <tr><td>Address:</td><td>${rowDat.consigneeAddress2}</td></tr>
    <tr><td>City:</td><td>${rowDat.consigneeCity}</td></tr>
    <tr><td>State:</td><td>${rowDat.consigneeState}</td></tr>
    <tr><td>ZIP:</td><td>${rowDat.consigneeZip}</td></tr>
    <tr><th colspan="2"> --- Manufacturer Details - for line
        ${rowDat.lineNo} --- </th></tr>
    <tr><td>MID:</td><td>${rowDat.refMid}</td></tr>
    <tr><td>Name:</td><td>${rowDat.refName}</td></tr>
    <tr><td>Address:</td><td>${rowDat.refAddress}</td></tr>
    <tr><td>City:</td><td>${rowDat.refCity}</td></tr>
</table>
</#list>

<p>NOTICE: Any content or attached materials may include United States DHS-Customs and Border Protection (CBP) or U.S. Consumer Product Safety Commission (CPSC) information that is "For Official Use Only, Law Enforcement Sensitive", or other types of sensitive information requiring protection against unauthorized disclosure. All such material must be handled and safeguarded in accordance with CBP or CPSC management directives governing protection and dissemination of such information.  Any personally identifying information (PII) enclosed is restricted from distribution outside of DHS and CPSC.</p>
