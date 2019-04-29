import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material";
import {HtsFlatNode} from "app/hts-tree/hts-flat-node";
import {ConfirmationDialogComponent} from "app/shared/dialog/confirmation-dialog/confirmation-dialog.component";
import {ExceptionDialogComponent} from "app/shared/dialog/exception-dialog/exception-dialog.component";
import {ModalMessageComponent} from "app/shared/dialog/modal-message/modal-message.component";
import {TransitionDialogComponent} from "app/shared/dialog/transition-dialog/transition-dialog.component";
import {Filter, TransitionEnum} from "app/shared/hts.model";
import {environment} from 'environments/environment';
import {Observable, timer} from "rxjs";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {map, switchMap, tap} from "rxjs/operators";
import {
  FilterEnum,
  HtsEditResponse,
  HtsRevertApplyResponse,
  HtsState,
  HtsTree,
  SourceEnum,
  Statistics
} from "./hts.model";

const EMPTY_TREE = {
  username: null,
  globalState: null,
  source: null,
  filter: null,
  searchterm: null,
  recordCount: 0,
  htsView: []
};

@Injectable()
export class DataService {

  defaultFilter = FilterEnum.juris;
  defaultUpdateSource = SourceEnum.upload_diffs;

  allData = new BehaviorSubject<HtsTree>(EMPTY_TREE);
  diffs = new BehaviorSubject<HtsTree>(EMPTY_TREE);
  current_load = new BehaviorSubject<boolean>(false);
  update_load = new BehaviorSubject<boolean>(false);
  state = new BehaviorSubject<HtsState>({});
  isDisabled: boolean = true;
  // state = new BehaviorSubject<PersistanceState>({
  //   persistenceStatus: PersistanceStatusEnum.finished,
  //   percentage: 0
  // });
  statistics = new BehaviorSubject<Statistics>({
    refActiveTotal: "",
    refActiveTotalJurisdictionTrue: "",
    refActiveTotalTargetedTrue: ""
  });
  // if globalState = ITC_UPLOAD_WIP, source can = DIFFS_FROM_UPLOAD for just diffs or  ITC_UPLOAD_WIP for whole HTS including diffs
  transitionDlgRef: MatDialogRef<TransitionDialogComponent> = null;

  constructor(private _http: HttpClient,
              public dialog: MatDialog) {
    console.log('backend: ' + environment.backend);
  }

  showNotification() {
    this.dialog.open(ModalMessageComponent, {
      hasBackdrop: true,
      disableClose: true,
      closeOnNavigation: false,
      width: '450px',
      data: {
        message: "HTS Data Has Been Updated.  Press OK to refresh.",
        title: "HTS Data Update",
        ok: true
      }
    });
  }


  startPollingState() {
    timer(0, 2000)
      .pipe(
        switchMap(() => this._http.get(environment.backend + 'state', {withCredentials: true})),
        tap((state: HtsState) => {
          this.isDisabled = !(state.globalState === "ITC_UPLOAD_WIP" || state.globalState === "CPSC_CURRENT_WIP");
        })
      )
      .subscribe((state: HtsState) => {
          this.stateHandler(state);
        }
      );
  }

  // stateTransitionTimestamp
  // * stateTransition
  // * Provides the most precise insight into the internal state of the HTS Services application. There are 12 possible
  // * values, which cannot be overridden. They are: ENABLE_START, ENABLE_END, UPLOAD_START, UPLOAD_END, SAVE_START,
  // * SAVE_END, REVERT_START, REVERT_END, FINALIZE_START, FINALIZE_END, and FINALIZE_STALL.

  isSSOEnabled() {
    return (!environment.production && environment.sso)
      || (environment.production && location.host === 'ramtest.cpsc.gov')
      || (environment.production && location.host === 'ram.cpsc.gov')
      || (environment.production && location.host === 'ramqa.cpsc.gov');
  }

  resetCurrent() {
    this.allData.next(EMPTY_TREE);
  }

  getCurrentData(source: SourceEnum, filter: Filter) {
    this.current_load.next(true);
    this._http.get(this.getDataQuery(source, filter), {withCredentials: true})
      .subscribe((tree: HtsTree) => {
        this.allData.next(tree);
        this.current_load.next(false);
      });
  }

  getDataQuery(source: SourceEnum, filter: Filter): string {
    let url = environment.backend + 'get?includelongdesc=false';
    url += '&source=' + source;
    if (filter && filter.filter) {
      url += '&filter=' + filter.filter;
    }
    if (filter && filter.search && filter.search.length > 0) {
      url += '&searchterm=' + filter.search;
    }
    return url;
  }

  resetDiff() {
    this.diffs.next(EMPTY_TREE);
  }

  getDiffs(source = this.defaultUpdateSource, filter: Filter) {
    this.update_load.next(true);
    this._http.get(this.getDataQuery(source, filter), {withCredentials: true})
      .subscribe((tree: HtsTree) => {
        this.diffs.next(tree);
        this.update_load.next(false);
      });
  }

  getStatistics() {
    this._http.get(environment.backend + 'counts', {withCredentials: true})
      .subscribe((counts: Statistics) => {
        this.statistics.next(counts);
      });
  }

  getDiffReport(whichDiffs: String) {
    return this._http.get(environment.backend + 'getcsvreport/itc_upload_wip/no_filter',
      {withCredentials: true});
    //    return this._http.get('assets/hts_cloud_full_itc_output_201803110044.json');

  }

  // On Current HTS tab:
  // if globalState = CURRENT_HTS_WIP,  then source must = CURRENT_HTS_WIP
  // if globalState = anything else (e.g. FINALIZE_NO_WIP, ITC_UPLOAD_WIP) then source should = CURRENT

  // On ITC Upload tab:
  // if globalState = FINALIZE_NO_WIP or CURRENT_HTS_WIP, no get request allowed for ITC Upload

  postFile(fileToUpload: File) {
    const endpointUrl = environment.backend + 'upload';
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    this._http
      .post(endpointUrl, formData, {withCredentials: true})
      .subscribe(() => {
      });
    this.showTransitionDialog();
    window.localStorage.setItem("upload-started", "true");
  }

// "timestamp": "2018-05-18 20:04:02"}
  getHtsUploadProgress() {
    return this._http.get(environment.backend + 'uploadprogress', {withCredentials: true});
  }

  enableCurrentEdit() {
    this._http.get(environment.backend + 'enableeditingcurrent', {withCredentials: true})
      .subscribe((resp: HtsEditResponse) => {
        console.log("Edit enabled");
      });
    this.showTransitionDialog();
  }

// {"persistenceStatus": "UNDERWAY",
// "persistenceProgressRemark": "Persistence is underway.",
// "percentage": "27",
// "progressInRecordsPersistedToScratchTable": "11000",
// "progressGoalInRecords": "39672",
// "progressLastCalculated": "2018-05-18 20:03:50",

// { "editingCurrentIsEnabled": "true", "globalState": "CPSC_CURRENT_WIP" }

  abortChanges() {
    this.openDialog("Abort Changes Confirm", "Are you sure?")
      .subscribe(result => {
        console.log('The dialog was closed', result);
        if (result) {
          this._http.get(environment.backend + 'revert', {withCredentials: true})
            .subscribe((resp: HtsRevertApplyResponse) => {
              console.log("Edit aborted");
            });
          this.showTransitionDialog();
        }
      });
  }

  save(node: HtsFlatNode) {
    this._http.post(environment.backend + 'save', {
      htsCode: node.code,
      description: node.description,
      jurisdiction: node.jurisdiction,
      targeted: node.targeted,
      notes: node.notes,
      startDate: node.startDate,
      endDate: node.endDate,
      changeStatus: node.changeStatus
    }, {withCredentials: true}).subscribe(x => {
      console.log('Save', x);
      this.getStatistics();
    });
    this.showTransitionDialog();
  }

  applyToRam() {
    this.openDialog("Apply To RAM Confirm", "Are you sure?")
      .subscribe(result => {
        console.log('The dialog was closed');
        if (result) {
          this._http.get(environment.backend + 'finalize', {withCredentials: true})
            .subscribe((resp: HtsRevertApplyResponse) => {
              console.log("Edit applied");
            });
          this.showTransitionDialog();
        }
      });
  }

  openDialog(title: string, question: string) {
    let dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      panelClass: "confirmation",
      data: {title: title, question: question}
    });
    return dialogRef.afterClosed();
  }

// "startDate": "2018-05-10", "endDate": ""}
  getLongDesc(source: SourceEnum, htsCode: string): Observable<string> {
    return this._http.get(environment.backend + 'get?maxdepth=0&includelongdesc=true&root='
      + htsCode + '&source=' + source, {withCredentials: true})
      .pipe(map((res: any) => <string>res.cpscDescription));
  }

  private stateHandler(state: HtsState) {
    const localStorage = window.localStorage;
    if (state.stateTransition.endsWith("START")) {
      const timeStarted = localStorage.getItem(state.stateTransition);
      if (!timeStarted || timeStarted !== state.stateTransitionTimestamp) {
        // close all dialogs except Transitiondialog
        this.dialog.openDialogs.forEach((d: MatDialogRef<any>) => {
          if (d !== this.transitionDlgRef) {
            d.close();
          }
        });
        // show transition dialog
        this.showTransitionDialog();
        localStorage.setItem(state.stateTransition, state.stateTransitionTimestamp);
      }
    } else if (state.stateTransition.endsWith("END")) {
      const timeEnded = localStorage.getItem(state.stateTransition);
      if (!timeEnded || timeEnded !== state.stateTransitionTimestamp) {
        // show notification dialog
        this.dialog.closeAll();
        this.transitionDlgRef = null;
        if (state.stateTransition === TransitionEnum.uploadEnd
          && state.lastUpload && state.lastUpload.result === "failure"
          && localStorage.getItem("upload-started") === 'true') {
          this.showUploadExceptionDialog();
          localStorage.removeItem("upload-started");
        } else {
          this.showNotification();
        }
        localStorage.setItem(state.stateTransition, state.stateTransitionTimestamp);
        this.getStatistics();
      }
    }
    this.state.next(state);
  }

  // http://ram2qass01:8080/htsservices-war/get?root=1704903550&maxdepth=0&includelongdesc=true
// { "htsCode": "1704903550",
// "htsCodeType": "10",
// "htsDescription": "Other",
// "isItcDescription": "true",
// "cpscDescription": "Sugars and sugar confectionery:Sugar confectionery (including white chocolate), not containing cocoa: Other: Confections or sweetmeats ready for consumption: Other: Other: Put up for retail sale: Other",
// "jurisdiction": "true",
// "targeted": "false",
// "changeStatus": "none",
// "notes": "",

  private showTransitionDialog() {
    if (this.transitionDlgRef === null) {
      this.transitionDlgRef = this.dialog.open(TransitionDialogComponent, {
        hasBackdrop: true,
        disableClose: true,
        closeOnNavigation: false,
        width: '450px',
        height: '250px',
        data: {
          state: this.state
        }
      });
    }
  }

  private showUploadExceptionDialog() {
    this.dialog.open(ExceptionDialogComponent, {
      hasBackdrop: true,
      disableClose: true,
      closeOnNavigation: false,
      width: '450px',
      // height: '250px',
      data: {
        state: this.state
      }
    });
  }
}

