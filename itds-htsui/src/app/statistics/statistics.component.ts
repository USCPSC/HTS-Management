import {Component, Input, OnDestroy} from '@angular/core';
import {Subscription} from "rxjs/Subscription";
import {DataService} from "../shared/data.service";
import {Statistics} from "../shared/hts.model";

@Component({
  selector: 'app-statistics[header]',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnDestroy {
  @Input("header") title: string;
  @Input() details = false;
  subscription: Subscription;
  currTotalHTScodeCount: number;
  currentTargeted: number;
  currentJurisdiction: number;

  constructor(public dataService: DataService) {
    this.subscription = this.dataService.statistics.subscribe(
      (data: Statistics) => {
        this.currTotalHTScodeCount = data.refActiveTotal;
        this.currentJurisdiction = data.refActiveTotalJurisdictionTrue;
        this.currentTargeted = data.refActiveTotalTargetedTrue;
      });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
