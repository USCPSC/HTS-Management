import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppBarComponent } from './app-bar.component';
import {
  MatButtonModule,
  MatIconModule,
  MatMenuModule, MatSnackBarModule,
  MatToolbarModule
} from "@angular/material";
import {RouterModule} from "@angular/router";
import {FlexLayoutModule} from "@angular/flex-layout";

@NgModule({
  imports: [
    CommonModule,
    FlexLayoutModule,
    RouterModule,
    MatToolbarModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  declarations: [AppBarComponent],
  exports: [AppBarComponent]
})
export class AppBarModule { }
