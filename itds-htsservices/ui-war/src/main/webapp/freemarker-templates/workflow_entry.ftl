<style type="text/css">
    html {
        margin: 3px;
        padding: 3px;
    }
    h5 {
        font-family: "Times New Roman", Georgia, Serif;
        font-size: 12px;
        font-weight: normal;
        padding-top: 0px;
        margin-top: 0px;
    }
    body {
        font-family: "Times New Roman", Georgia, Serif;
        font-size: 12px;
        margin: 0px;
        padding: 0px;
    }
    tr > td {
        padding-bottom: 0px;
        padding-top: 0px;
    }
</style>

<div>Status:  CBP Hold Approved On Date: (Tue May 24 09:57:40 EDT 2016)     Score: ${entryLine.ramScore}</div>

<div>--- Entry Details ---</div>
<table>
    <tr>
        <td>Arrival Date:</td>
        <td>${entryLine.arrivalDate}</td>
    </tr><tr>
        <td>Release Date:</td>
        <td>${entryLine.releaseDate}</td>
    </tr><tr>
        <td>Entry:</td>
        <td>${entryLine.entryNumber}</td>
    </tr><tr>
        <td>Line:</td>
        <td>${entryLine.entryLineNumber}</td>
    </tr><tr>
        <td>HTS:</td>
        <td>${entryLine.htsNumber}</td>
    </tr><tr>
        <td>BOL:</td>
        <td>${entryLine.billOfLading}</td>
    </tr><tr>
        <td>Carrier:</td>
        <td>${cbpEntry.carrier}</td>
    </tr><tr>
        <td>MOT:</td>
        <td>${cbpEntry.mot}</td>
    </tr><tr>
        <td>Vessel:</td>
        <td>${cbpEntry.vessel}</td>
    </tr><tr>
        <td>Port Name:</td>
        <td>${entryLine.portName}</td>
    </tr><tr>
        <td>FTZ:</td>
        <td>${cbpEntry.ftz}</td>
    </tr>
</table>

<div>--- Importer Details ---</div>
<table>
    <tr>
        <td>EIN:</td>
        <td>${cbpEntry.importerEin}</td>
    </tr><tr>
        <td>Name:</td>
        <td>${cbpEntry.importerName}</td>
    </tr><tr>
        <td>Address:</td>
        <td>${cbpEntry.importerAddress}</td>
    </tr><tr>
        <td>Address:</td>
        <td>${cbpEntry.importerAddress2}</td>
    </tr><tr>
        <td>City:</td>
        <td>${cbpEntry.importerCity}</td>
    </tr><tr>
        <td>State:</td>
        <td>${cbpEntry.importerState}</td>
    </tr><tr>
        <td>ZIP:</td>
        <td>${cbpEntry.importerZip}</td>
    </tr>
</table>
<div>--- Consignee Details ---</div>
<table>
    <tr>
        <td>EIN:</td>
        <td>${cbpEntry.consigneeEin}</td>
    </tr><tr>
        <td>Name:</td>
        <td>${cbpEntry.consigneeName}</td>
    </tr><tr>
        <td>Address:</td>
        <td>${cbpEntry.consigneeAddress}</td>
    </tr><tr>
        <td>Address:</td>
        <td>${cbpEntry.consigneeAddress2}</td>
    </tr><tr>
        <td>City:</td>
        <td>${cbpEntry.consigneeCity}</td>
    </tr><tr>
        <td>State:</td>
        <td>${cbpEntry.consigneeState}</td>
    </tr><tr>
        <td>ZIP:</td>
        <td>${cbpEntry.consigneeZip}</td>
    </tr>
</table>
<div>--- Manufacturer Details ---</div>
<table>
    <tr>
        <td>MID:</td>
        <td>${cbpEntry.refMid}</td>
    </tr><tr>
        <td>Name:</td>
        <td>${entryLine.manufacturerName}</td>
    </tr><tr>
        <td>Address:</td>
        <td>${cbpEntry.refAddress}</td>
    </tr><tr>
        <td>City:</td>
        <td>${cbpEntry.refCity}</td>
    </tr>
</table>
<div>NOTICE: Any content or attached materials may include United States DHS-Customs and Border Protection (CBP) or U.S. Consumer Product Safety Commission (CPSC) information that is "For Official Use Only, Law Enforcement Sensitive", or other types of sensitive information requiring protection against unauthorized disclosure. All such material must be handled and safeguarded in accordance with CBP or CPSC management directives governing protection and dissemination of such information.  Any personally identifying information (PII) enclosed is restricted from distribution outside of DHS and CPSC.</div>
