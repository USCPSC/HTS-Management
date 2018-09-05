import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatisticsComponent } from './statistics.component';
import {MatCardModule} from "@angular/material";

@NgModule({
  imports: [
    CommonModule,
    MatCardModule,
  ],
  declarations: [StatisticsComponent],
  exports: [StatisticsComponent],
})
export class StatisticsModule { }
