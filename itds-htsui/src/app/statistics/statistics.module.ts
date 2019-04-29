import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FlexLayoutModule} from "@angular/flex-layout";
import { StatisticsComponent } from './statistics.component';
import {MatCardModule} from "@angular/material";

@NgModule({
  imports: [
    CommonModule,
    MatCardModule,
    FlexLayoutModule,
  ],
  declarations: [StatisticsComponent],
  exports: [StatisticsComponent],
})
export class StatisticsModule { }
