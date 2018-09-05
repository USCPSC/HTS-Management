import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FlexLayoutModule} from "@angular/flex-layout";
import {FormsModule} from "@angular/forms";
import {
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatIconModule,
  MatProgressSpinnerModule,
  MatTreeModule
} from "@angular/material";
import {DialogModule} from "app/shared/dialog/dialog.module";
import {HtsTreeComponent} from './hts-tree.component';
import {NodeComponent} from './node/node.component';
import {ScrollingModule} from '@angular/cdk-experimental';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    FlexLayoutModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatButtonModule,
    MatCheckboxModule,
    MatIconModule,
    MatTreeModule,
    DialogModule,
    ScrollingModule
  ],
  declarations: [HtsTreeComponent, NodeComponent],
  exports: [HtsTreeComponent]
})
export class HtsTreeModule {
}
