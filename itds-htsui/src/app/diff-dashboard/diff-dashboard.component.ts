import {Component, OnDestroy} from '@angular/core';
import {DataService} from "app/shared/data.service";
import {Filter, FilterEnum, GlobalStateEnum, HtsState, SourceEnum} from "app/shared/hts.model";
import {filter} from "rxjs/operators";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-diff-dashboard',
  templateUrl: './diff-dashboard.component.html',
  styleUrls: ['./diff-dashboard.component.scss'],
})
export class DiffDashboardComponent implements OnDestroy {
  subscription: Subscription;
  state: HtsState = null;
  currentFilter: Filter = {
    filter: this.dataService.defaultFilter,
    search: null
  };
  flatView = false;
  source = this.dataService.defaultUpdateSource;

  constructor(public dataService: DataService) {
    this.subscription = this.dataService.state
      .pipe(
        filter(state => state.globalState !== undefined)
      ).subscribe((state: HtsState) => {
        if(state.globalState === GlobalStateEnum.upload){
          if (this.state === null || this.state.globalState !== state.globalState){
            this.dataService.getDiffs(this.source, this.currentFilter);
          }
        }
        this.state = state;
        // console.log("DiffDash", this.state);
      });
  }

  onFilterChange(filter: Filter) {
    this.currentFilter = filter;
    this.dataService.resetDiff();
    this.dataService.getDiffs(this.source, this.currentFilter);
  }

  onSourceChange(source:SourceEnum){
    this.source = source;
    this.dataService.resetDiff();
    this.dataService.getDiffs(this.source, this.currentFilter);
  }

  getReportLink() {
    let link = "get?source=" + this.source + "&format=";
    if (this.flatView) {
      link += 'csvflat&';
    } else {
      link += 'csvtree&';
    }
    if (this.currentFilter) {
      link += "filter=" + this.currentFilter.filter;
      if (this.currentFilter.search) {
        link += "&searchterm=" + this.currentFilter.search;
      }
    } else {
      link += "filter=" + this.dataService.defaultFilter;
    }
    return link;
  }

  isReady2Upload(): boolean {
    return this.state === null || this.state.globalState === GlobalStateEnum.finalized;
  }

  isInIncorrectState(): boolean {
    return this.stateInitialized() && this.state.globalState === GlobalStateEnum.edit;
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  isFlatView(data) {
    console.log("view-dashboard", data);
    this.flatView = data;
  }

  private stateInitialized(): boolean {
    return !(this.state == null || this.state.persistenceStatus == null);
  }

}
