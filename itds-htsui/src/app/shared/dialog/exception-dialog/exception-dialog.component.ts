import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material";
import {GlobalStateEnum, HtsState, TransitionEnum} from "app/shared/hts.model";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-exception-dialog',
  templateUrl: './exception-dialog.component.html',
  styleUrls: ['./exception-dialog.component.scss']
})
export class ExceptionDialogComponent implements OnDestroy {
  sub: Subscription;
  state: HtsState;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { state: BehaviorSubject<HtsState>; }) {
    this.sub = this.data.state.subscribe(
      (data: HtsState) => {
        this.state = data;
      });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

}
