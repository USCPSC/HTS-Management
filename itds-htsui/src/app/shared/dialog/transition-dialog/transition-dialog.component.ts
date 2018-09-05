import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material";
import {GlobalStateEnum, HtsState, TransitionEnum} from "app/shared/hts.model";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-transition-dialog',
  templateUrl: './transition-dialog.component.html',
  styleUrls: ['./transition-dialog.component.scss']
})
export class TransitionDialogComponent implements OnInit, OnDestroy {
  sub: Subscription;
  state: HtsState;
  mode = 'indeterminate';

  constructor(@Inject(MAT_DIALOG_DATA) public data: { state: BehaviorSubject<HtsState>; }) {
    this.sub = this.data.state.subscribe(
      (data: HtsState) => {
        this.state = data;
        if (this.state.stateTransition === TransitionEnum.uploadStart) {
          this.mode = 'determinate';
          if (this.state.percentage == 0) {
            this.state.percentage = 10;
          }
        } else {
          this.mode = 'indeterminate';
        }
        console.log("Transition upload", this.state);
      });
  }

  isInProgress(): boolean {
    return this.state.globalState === GlobalStateEnum.transition;
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

}
