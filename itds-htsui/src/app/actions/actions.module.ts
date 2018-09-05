import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActionsComponent } from './actions.component';
import {MatButtonModule, MatCardModule, MatIconModule} from "@angular/material";
import {FlexLayoutModule} from "@angular/flex-layout";

@NgModule({
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
  ],
  declarations: [ActionsComponent],
  exports: [ActionsComponent]
})
export class ActionsModule { }
