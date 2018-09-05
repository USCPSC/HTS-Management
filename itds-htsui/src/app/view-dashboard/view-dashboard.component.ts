import {Component, OnDestroy} from '@angular/core';
import {DataService} from "app/shared/data.service";
import {Filter, FilterEnum, GlobalStateEnum, HtsState, SourceEnum} from "app/shared/hts.model";
import {filter} from "rxjs/operators";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'view-dashboard',
  templateUrl: './view-dashboard.component.html',
  styleUrls: ['./view-dashboard.component.scss'],
})
export class ViewDashboardComponent implements OnDestroy {
  currentFilter: Filter = {
    filter: this.ds.defaultFilter,
    search: null
  };
  expand = this.ds.defaultFilter === FilterEnum.juris;
  flatView = false;
  subscription: Subscription;
  state: HtsState = null;
  source: SourceEnum;

  constructor(public ds: DataService) {
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
          if (this.state === null || this.state.globalState !== state.globalState) {
            this.ds.getCurrentData(this.source, this.currentFilter);
          }
        }
        this.state = state;
      });
  }

  onFilterChange(filter: Filter) {
    this.currentFilter = filter;
    this.ds.resetCurrent();
    this.ds.getCurrentData(this.source, filter);
    this.expand = filter.filter === FilterEnum.target || filter.filter === FilterEnum.juris;
  }

  isFlatView(data) {
    console.log("view-dashboard", data);
    this.flatView = data;
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
      link += "filter=" + this.ds.defaultFilter;
    }
    return link;
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
