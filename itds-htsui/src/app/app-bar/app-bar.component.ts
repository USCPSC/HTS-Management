import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import download from "downloadjs";
import { MatSnackBar } from "@angular/material";
import { HttpClient } from "@angular/common/http";
import { AuthenticationService } from "../shared/authentication.service";
import {GlobalStateEnum, HtsState, SourceEnum} from "../shared/hts.model";
import { DataService } from "app/shared/data.service";
import { Subscription } from "rxjs/Subscription";
import {filter} from "rxjs/operators";
@Component({
  selector: 'app-bar',
  templateUrl: './app-bar.component.html',
  styleUrls: ['./app-bar.component.scss']
})
export class AppBarComponent implements OnInit {
  public date = new Date();
  subscription: Subscription;
  state: HtsState = null;
  source: SourceEnum;

  constructor(private http: HttpClient,
              private snackBar: MatSnackBar,
              public user: AuthenticationService,
              public ds: DataService) {
    this.subscription = this.ds.state
      .pipe(
        filter(state => state.globalState !== undefined)
      ).subscribe((state: HtsState) => {
        if (state.globalState !== GlobalStateEnum.transition) {
          if (state.globalState === GlobalStateEnum.edit) {
            this.source = SourceEnum.edit;
          } else {
            this.source = SourceEnum.currrent;
          }
        }
        this.state = state;
      });
  }

  getRamUrl() {
    return environment.ramUrl;
  }

  getRuleEngine() {
    return environment.ruleEngine;
  }

  getDate(): string {
    const month = this.date.getMonth() + 1;
    const year = this.date.getFullYear();
    const day = this.date.getDay();

    return `${year}-${month}-${day}`;
  }

  getSrcState() {
    let state: string = "";
    (this.state.globalState === GlobalStateEnum.transition)
      ? state = this.source
      : state = this.state.globalState;
    return state;
  }

  getAppendixBCurrent() {
    const currentDate = this.getDate();

    let appendJurisd = this.snackBar.open(`Saving AppendixBSummary-${currentDate}.csv to downloads`, 'Close', {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'right'
    });

    let appendBJuris = environment.backend + 'appendixbjurisdiction?source=CURRENT';
    this.http.get(appendBJuris,{responseType: 'blob', withCredentials: true})
      .subscribe((resp)=>{
        appendJurisd.dismiss();
        let appendSummary = this.snackBar.open(`Saving AppendixBJurisdiction-${currentDate}.csv to downloads`, 'Close', {
          duration: 4000,
          verticalPosition: 'top',
          horizontalPosition: 'right'
        });
        download(resp, `AppendixBJurisdiction-${currentDate}.csv`, "text/csv");
        this.http.get(environment.backend + 'appendixbsummary?source=CURRENT',{responseType: 'blob', withCredentials: true})
          .subscribe((resp1)=>{
            appendSummary.dismiss();
            download(resp1, `AppendixBSummary-${currentDate}.csv`, "text/csv");
          });
      });
  }

  getCBPCurrent() {
    const currentDate = this.getDate();

    let cbpJurisd = this.snackBar.open(`Saving CBP-CPSC-Jurisdiction-${currentDate}.csv to downloads`, 'Close', {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'right'
    });

    let CBPJuris = environment.backend + 'cbpcpscjurisdiction?source=CURRENT';
    this.http.get(CBPJuris,{responseType: 'blob', withCredentials: true})
      .subscribe((resp)=>{
        cbpJurisd.dismiss();
        let cbpFilter = this.snackBar.open(`Saving CBP-CPSC-EntryFilter-${currentDate}.csv to downloads`, 'Close', {
          duration: 4000,
          verticalPosition: 'top',
          horizontalPosition: 'right'
        });
        download(resp, `CBP-CPSC-Jurisdiction-${currentDate}.csv`, "text/csv");
        this.http.get(environment.backend + 'cbpcpscentryfilter?source=CURRENT',{responseType: 'blob', withCredentials: true})
          .subscribe((resp1)=>{
            cbpFilter.dismiss();
            download(resp1, `CBP-CPSC-EntryFilter-${currentDate}.csv`, "text/csv");
          });
      });
  }

  getAppendixB() {
    const currentDate = this.getDate();
    const srcState = this.getSrcState();

    let appendJurisd = this.snackBar.open(`Saving AppendixBSummary-${currentDate}.csv to downloads`, 'Close', {
        duration: 5000,
        verticalPosition: 'top',
        horizontalPosition: 'right'
    });

    let appendBJuris = environment.backend + 'appendixbjurisdiction?source=' + srcState;
    this.http.get(appendBJuris,{responseType: 'blob', withCredentials: true})
      .subscribe((resp)=>{
        appendJurisd.dismiss();
        let appendSummary = this.snackBar.open(`Saving AppendixBJurisdiction-${currentDate}.csv to downloads`, 'Close', {
          duration: 4000,
          verticalPosition: 'top',
          horizontalPosition: 'right'
        });
        download(resp, `AppendixBJurisdiction-${currentDate}.csv`, "text/csv");
        this.http.get(environment.backend + 'appendixbsummary?source=' + srcState,{responseType: 'blob', withCredentials: true})
          .subscribe((resp1)=>{
            appendSummary.dismiss();
            download(resp1, `AppendixBSummary-${currentDate}.csv`, "text/csv");
          });
      });
  }

  getCBP() {
    const currentDate = this.getDate();
    const srcState = this.getSrcState();

    let cbpJurisd = this.snackBar.open(`Saving CBP-CPSC-Jurisdiction-${currentDate}.csv to downloads`, 'Close', {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'right'
    });

    let appendBJuris = environment.backend + 'cbpcpscjurisdiction?source=' + srcState;
    this.http.get(appendBJuris,{responseType: 'blob', withCredentials: true})
      .subscribe((resp)=>{
        cbpJurisd.dismiss();
        let cbpFilter = this.snackBar.open(`Saving CBP-CPSC-EntryFilter-${currentDate}.csv to downloads`, 'Close', {
          duration: 4000,
          verticalPosition: 'top',
          horizontalPosition: 'right'
        });
        download(resp, `CBP-CPSC-Jurisdiction-${currentDate}.csv`, "text/csv");
        this.http.get(environment.backend + 'cbpcpscentryfilter?source=' + srcState,{responseType: 'blob', withCredentials: true})
          .subscribe((resp1)=>{
            cbpFilter.dismiss();
            download(resp1, `CBP-CPSC-EntryFilter-${currentDate}.csv`, "text/csv");
          });
      });
  }

  ngOnInit() {
  }

}
