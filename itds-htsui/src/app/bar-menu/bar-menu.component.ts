import {Component, OnDestroy} from '@angular/core';
import {ThemePalette} from "@angular/material";
import {Router} from "@angular/router";
import {DataService} from "app/shared/data.service";
import {GlobalStateEnum, HtsState} from "app/shared/hts.model";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-bar-menu',
  templateUrl: './bar-menu.component.html',
  styleUrls: ['./bar-menu.component.scss']
})
export class BarMenuComponent implements OnDestroy {
  sub: Subscription;
  state: HtsState;

  tabNavBackground: ThemePalette;

  constructor(private router: Router,
              private dataService: DataService) {
    this.sub = this.dataService.state.subscribe(
      (data: HtsState) => {
        this.state = data;
      });
  }


  isCurrentEnabled(): boolean {
    return this.state.globalState === GlobalStateEnum.finalized
      || this.state.globalState === GlobalStateEnum.edit;
  }

  isUpdatesEnabled(): boolean {
    return this.state.globalState === GlobalStateEnum.finalized
      || this.state.globalState === GlobalStateEnum.upload;
  }

  isActive(rt: string): boolean {
    return this.router.routerState.snapshot.url.includes(rt);
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

}
