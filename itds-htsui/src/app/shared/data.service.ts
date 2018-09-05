import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material";
import {HtsFlatNode} from "app/hts-tree/hts-flat-node";
import {ConfirmationDialogComponent} from "app/shared/dialog/confirmation-dialog/confirmation-dialog.component";
import {ExceptionDialogComponent} from "app/shared/dialog/exception-dialog/exception-dialog.component";
import {ModalMessageComponent} from "app/shared/dialog/modal-message/modal-message.component";
import {TransitionDialogComponent} from "app/shared/dialog/transition-dialog/transition-dialog.component";
import {Filter, PersistanceStatusEnum, TransitionEnum} from "app/shared/hts.model";
import {Observable, timer} from "rxjs";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {map, switchMap, tap} from "rxjs/operators";
import {environment} from 'environments/environment';
import {
  FilterEnum,
  GlobalStateEnum,
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
    refActiveTotal: 0,
    refActiveTotalJurisdictionTrue: 0,
    refActiveTotalTargetedTrue: 0
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

  showUploadErrors(state: HtsState): boolean {
    const localStorage = window.localStorage;
    if (state.lastUpload && state.lastUpload.result === "failure") {
      const uploadTimestamp = localStorage.getItem('upload-timestamp');
      if (uploadTimestamp) {
        if (uploadTimestamp !== state.lastUpload.timestamp) {
          // Show dialog because it is new upload
          localStorage.setItem("upload-timestamp", state.lastUpload.timestamp);
          this.showUploadExceptionDialog();
          return true;
        }
      } else {
        // show dialog, no timestamp saved
        localStorage.setItem("upload-timestamp", state.lastUpload.timestamp);
        this.showUploadExceptionDialog();
        return true;
      }
    }
    return false;
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
          if (state.globalState !== GlobalStateEnum.transition) {
            if (this.state.value.globalState === GlobalStateEnum.transition || this.transitionDlgRef) {
              this.dialog.closeAll();
              this.transitionDlgRef = null;
              let showPopup = true;
              if (state.lastUpload && state.lastUpload.result === "failure") {
                const localStorage = window.localStorage;
                const iStartedUpload = localStorage.getItem("upload-started") === 'true';
                if (iStartedUpload) {
                  showPopup = !this.showUploadErrors(state);
                  localStorage.removeItem("upload-started");
                }
              }
              if (showPopup) {
                this.showNotification();
              }
            }
          } else if (this.state.value.globalState !== state.globalState) {
            this.showTransitionDialog();
          }
          this.state.next(state);
        }
      );
  }

  isSSOEnabled() {
    return (!environment.production && environment.sso)
      || (environment.production && location.host === 'EXAMPLE_HOST')
      || (environment.production && location.host === 'EXAMPLE_HOST')
      || (environment.production && location.host === 'EXAMPLE_HOST');
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

  // On Current HTS tab:
  // if globalState = CURRENT_HTS_WIP,  then source must = CURRENT_HTS_WIP
  // if globalState = anything else (e.g. FINALIZE_NO_WIP, ITC_UPLOAD_WIP) then source should = CURRENT

  // On ITC Upload tab:
  // if globalState = FINALIZE_NO_WIP or CURRENT_HTS_WIP, no get request allowed for ITC Upload

  getDiffReport(whichDiffs: String) {
    return this._http.get(environment.backend + 'getcsvreport/itc_upload_wip/no_filter',
      {withCredentials: true});
    //    return this._http.get('assets/hts_cloud_full_itc_output_201803110044.json');

  }

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

// {"persistenceStatus": "UNDERWAY",
// "persistenceProgressRemark": "Persistence is underway.",
// "percentage": "27",
// "progressInRecordsPersistedToScratchTable": "11000",
// "progressGoalInRecords": "39672",
// "progressLastCalculated": "2018-05-18 20:03:50",

// { "editingCurrentIsEnabled": "true", "globalState": "CPSC_CURRENT_WIP" }

  enableCurrentEdit() {
    this._http.get(environment.backend + 'enableeditingcurrent', {withCredentials: true})
      .subscribe((resp: HtsEditResponse) => {
        console.log("Edit enabled");
      });
    this.showTransitionDialog();
  }

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

