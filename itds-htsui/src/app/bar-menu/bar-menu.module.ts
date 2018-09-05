import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BarMenuComponent } from './bar-menu.component';
import {MatTabsModule} from "@angular/material";
import {RouterModule} from "@angular/router";

@NgModule({
  imports: [
    CommonModule,
    MatTabsModule,
    RouterModule
  ],
  declarations: [BarMenuComponent],
  exports: [BarMenuComponent]
})
export class BarMenuModule { }
