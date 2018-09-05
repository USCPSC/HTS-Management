import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FlexLayoutModule} from "@angular/flex-layout";
import {HtsTreeModule} from "app/hts-tree/hts-tree.module";
import {ActionsModule} from "../actions/actions.module";
import {FilterModule} from "../filter/filter.module";
import {StatisticsModule} from "../statistics/statistics.module";
import {ViewDashboardComponent} from "./view-dashboard.component";
import {ViewRoutingModule} from "./view-routing.module";

@NgModule({
  imports: [
    CommonModule,
    FlexLayoutModule,
    ViewRoutingModule,
    StatisticsModule,
    ActionsModule,
    FilterModule,
    HtsTreeModule
  ],
  declarations: [ViewDashboardComponent],
  exports: [ViewDashboardComponent]
})
export class ViewDashboardModule {
}
