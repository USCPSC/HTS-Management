app.factory('ExamXrfPreview', function () {
    function ExamXrfPreview(xrf) {
        xrf = xrf ? xrf : {};
        this.id = xrf.id ? xrf.id : "";
        this.readingNo = xrf.readingNo ? xrf.readingNo : "";
        this.readingTime = xrf.readingTime ? xrf.readingTime : "";
        this.sampleNumber = xrf.sampleNumber ? xrf.sampleNumber : "";
        this.fileIndex = xrf.fileIndex ? xrf.fileIndex : "";
        this.type = xrf.type ? xrf.type : "";
        this.duration = xrf.duration ? xrf.duration : "";
        this.units = xrf.units ? xrf.units : "";
        this.sigmaValue = xrf.sigmaValue ? xrf.sigmaValue : "";
        this.sequence = xrf.sequence ? xrf.sequence : "";
        this.sampleType = xrf.sampleType ? xrf.sampleType : "";
        this.result = xrf.result ? xrf.result : "";
        this.flags = xrf.flags ? xrf.flags : "";
        this.color = xrf.color ? xrf.color : "";
        this.sku = xrf.sku ? xrf.sku : "";
        this.location = xrf.location ? xrf.location : "";
        this.misc = xrf.misc ? xrf.misc : "";
        this.note = xrf.note ? xrf.note : "";
        this.ba = xrf.ba ? xrf.ba : "";
        this.baError = xrf.baError ? xrf.baError : "";
        this.sb = xrf.sb ? xrf.sb : "";
        this.sbError = xrf.sbError ? xrf.sbError : "";
        this.sn = xrf.sn ? xrf.sn : "";
        this.snError = xrf.snError ? xrf.snError : "";
        this.cd = xrf.cd ? xrf.cd : "";
        this.cdError = xrf.cdError ? xrf.cdError : "";
        this.bal = xrf.bal ? xrf.bal : "";
        this.balError = xrf.balError ? xrf.balError : "";
        this.bi = xrf.bi ? xrf.bi : "";
        this.biError = xrf.biError ? xrf.biError : "";
        this.pb = xrf.pb ? xrf.pb : "";
        this.pbError = xrf.pbError ? xrf.pbError : "";
        this.br = xrf.br ? xrf.br : "";
        this.brError = xrf.brError ? xrf.brError : "";
        this.se = xrf.se ? xrf.se : "";
        this.seError = xrf.seError ? xrf.seError : "";
        this.ars = xrf.ars ? xrf.ars : "";
        this.arsError = xrf.arsError ? xrf.arsError : "";
        this.hg = xrf.hg ? xrf.hg : "";
        this.hgError = xrf.hgError ? xrf.hgError : "";
        this.au = xrf.au ? xrf.au : "";
        this.auError = xrf.auError ? xrf.auError : "";
        this.zn = xrf.zn ? xrf.zn : "";
        this.znError = xrf.znError ? xrf.znError : "";
        this.cu = xrf.cu ? xrf.cu : "";
        this.cuError = xrf.cuError ? xrf.cuError : "";
        this.ni = xrf.ni ? xrf.ni : "";
        this.niError = xrf.niError ? xrf.niError : "";
        this.fe = xrf.fe ? xrf.fe : "";
        this.feError = xrf.feError ? xrf.feError : "";
        this.cr = xrf.cr ? xrf.cr : "";
        this.crError = xrf.crError ? xrf.crError : "";
        this.v = xrf.v ? xrf.v : "";
        this.vError = xrf.vError ? xrf.vError : "";
        this.ti = xrf.ti ? xrf.ti : "";
        this.tiError = xrf.tiError ? xrf.tiError : "";
        this.cl = xrf.cl ? xrf.cl : "";
        this.clError = xrf.clError ? xrf.clError : "";
        this.clAndBr = xrf.clAndBr ? xrf.clAndBr : "";
        this.clAndBrError = xrf.clAndBrError ? xrf.clAndBrError : "";
        this.ind = xrf.ind ? xrf.ind : "";
        this.indError = xrf.indError ? xrf.indError : "";
        this.pd = xrf.pd ? xrf.pd : "";
        this.pdError = xrf.pdError ? xrf.pdError : "";
        this.ag = xrf.ag ? xrf.ag : "";
        this.agError = xrf.agError ? xrf.agError : "";
        this.mo = xrf.mo ? xrf.mo : "";
        this.moError = xrf.moError ? xrf.moError : "";
        this.nb = xrf.nb ? xrf.nb : "";
        this.nbError = xrf.nbError ? xrf.nbError : "";
        this.zr = xrf.zr ? xrf.zr : "";
        this.zrError = xrf.zrError ? xrf.zrError : "";
        this.w = xrf.w ? xrf.w : "";
        this.wError = xrf.wError ? xrf.wError : "";
        this.pt = xrf.pt ? xrf.pt : "";
        this.ptError = xrf.ptError ? xrf.ptError : "";
        this.co = xrf.co ? xrf.co : "";
        this.coError = xrf.coError ? xrf.coError : "";
        this.mn = xrf.mn ? xrf.mn : "";
        this.mnError = xrf.mnError ? xrf.mnError : "";
        this.createTimestamp = xrf.createTimestamp ? xrf.createTimestamp : undefined;
        this.createUserId = xrf.createUserId ? xrf.createUserId : "";
        this.lastUpdateTimestamp = xrf.lastUpdateTimestamp ? xrf.lastUpdateTimestamp : undefined;
        this.lastUpdateUserId = xrf.lastUpdateUserId ? xrf.lastUpdateUserId : "";
    }

    ExamXrfPreview.prototype.isValid = function () {
        return (this.isReadingNoValid() && this.isSampleNumberValid() && this.isFileIndexValid() && this.isSampleTypeValid() &&
                this.isResultValid() && this.isFlagsValid() && this.isColorValid() && this.isSkuValid() && this.isLocationValid &&
                this.isMiscValid() && this.isNoteValid() && this.isPbValid() && this.isCdValid());
    };

    ExamXrfPreview.prototype.isReadingNoValid = function () {
        if (!this.readingNo) {
            this.readingNoError = "Reading Number is required";
            return false;
        } else if ('regex',/^([0-9]{0,})$/.test(this.readingNo)) {
            this.readingNoError = undefined;
            return true;
        } else {
            this.readingNoError = "Reading Number must be an integer";
            return false;
        }
    };

    ExamXrfPreview.prototype.isSampleNumberValid = function () {
        if (this.sampleNumber.length > 255) {
            this.sampleNumberError = "Sample Number must contain 255 or fewer characters";
            return false;
        } else {
            this.sampleNumberError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isFileIndexValid = function () {
        if (this.fileIndex.length > 255) {
            this.fileIndexError = "File Index must contain 255 or fewer characters";
            return false;
        } else {
            this.fileIndexError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isSampleTypeValid = function () {
        if (this.sampleType.length > 255) {
            this.sampleTypeError = "Sample Type must contain 255 or fewer characters";
            return false;
        } else {
            this.sampleTypeError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isResultValid = function () {
        if (this.result.length > 255) {
            this.resultError = "Result must contain 255 or fewer characters";
            return false;
        } else {
            this.resultError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isFlagsValid = function () {
        if (this.flags.length > 255) {
            this.flagsError = "Flags must contain 255 or fewer characters";
            return false;
        } else {
            this.flagsError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isColorValid = function () {
        if (this.color.length > 255) {
            this.colorError = "Color must contain 255 or fewer characters";
            return false;
        } else {
            this.colorError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isSkuValid = function () {
        if (this.sku.length > 255) {
            this.skuError = "SKU must contain 255 or fewer characters";
            return false;
        } else {
            this.skuError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isLocationValid = function () {
        if (this.location.length > 255) {
            this.locationError = "Location must contain 255 or fewer characters";
            return false;
        } else {
            this.locationError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isMiscValid = function () {
        if (this.misc.length > 255) {
            this.miscError = "Misc must contain 255 or fewer characters";
            return false;
        } else {
            this.miscError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isNoteValid = function () {
        if (this.note.length > 255) {
            this.noteError = "Note must contain 255 or fewer characters";
            return false;
        } else {
            this.noteError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isCdValid = function () {
        if (isNaN(this.cd)) {
            this.cadmiumError = "Cadmium Reading must be a number";
            return false;
        } else {
            this.cadmiumError = undefined;
            return true;
        }
    };

    ExamXrfPreview.prototype.isPbValid = function () {
        if (isNaN(this.pb)) {
            this.leadError = "Lead Reading must be a number";
            return false;
        } else {
            this.leadError = undefined;
            return true;
        }
    };

    return ExamXrfPreview;
});