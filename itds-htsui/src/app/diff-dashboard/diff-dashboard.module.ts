import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FlexLayoutModule} from "@angular/flex-layout";
import {UploadModule} from "app/diff-dashboard/upload/upload.module";
import {HtsTreeModule} from "app/hts-tree/hts-tree.module";
import {ActionsModule} from "../actions/actions.module";
import {FilterModule} from "../filter/filter.module";
import {StatisticsModule} from "../statistics/statistics.module";
import {DiffDashboardComponent} from './diff-dashboard.component';
import {DiffRoutingModule} from "./diff-routing.module";

@NgModule({
  imports: [
    CommonModule,
    FlexLayoutModule,
    DiffRoutingModule,
    StatisticsModule,
    ActionsModule,
    FilterModule,
    UploadModule,
    HtsTreeModule
  ],
  declarations: [DiffDashboardComponent],
  exports: [DiffDashboardComponent]
})
export class DiffDashboardModule {
}
