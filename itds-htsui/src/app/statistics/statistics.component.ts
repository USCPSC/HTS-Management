import {Component, Input, OnDestroy} from '@angular/core';
import {filter} from "rxjs/operators";
import {Subscription} from "rxjs/Subscription";
import {DataService} from "../shared/data.service";
import {GlobalStateEnum, HtsState, SourceEnum, Statistics} from "../shared/hts.model";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnDestroy {
  @Input() upload = false;
  sub1: Subscription;
  sub2: Subscription;
  stats: Statistics;
  showSecond = false;

  constructor(public dataService: DataService) {
    this.sub1 = this.dataService.statistics.subscribe(
      (data: Statistics) => {
        this.stats = data;
      });
    this.sub2 = this.dataService.state
      .pipe(
        filter(state => state.globalState !== undefined)
      ).subscribe((state: HtsState) => {
        if(this.upload){
          this.showSecond = state.globalState === GlobalStateEnum.upload;
        } else {
          this.showSecond = state.globalState === GlobalStateEnum.edit;
        }
      });
  }

  ngOnDestroy(): void {
    this.sub1.unsubscribe();
    this.sub2.unsubscribe();
  }

}
