import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatToolbarModule} from "@angular/material";
import {RouterModule} from "@angular/router";
import {FooterComponent} from "app/footer/footer.component";

@NgModule({
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatToolbarModule,
  ],
  declarations: [FooterComponent],
  exports: [FooterComponent]
})
export class FooterModule { }
