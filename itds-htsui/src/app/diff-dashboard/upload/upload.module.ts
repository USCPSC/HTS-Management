import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FlexLayoutModule} from "@angular/flex-layout";
import {MatButtonModule, MatCardModule, MatIconModule, MatProgressSpinnerModule} from "@angular/material";
import {FilesClickComponent} from "app/diff-dashboard/upload/files-click/files-click.component";
import {FilesDropComponent} from "app/diff-dashboard/upload/files-drop/files-drop.component";
import {FilesUtilsService} from "app/diff-dashboard/upload/services/files-utils.service";
import {FilesService} from "app/diff-dashboard/upload/services/files.service";
import {UploadComponent} from "app/diff-dashboard/upload/upload.component";

@NgModule({
  imports: [
    CommonModule,
    FlexLayoutModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatProgressSpinnerModule
  ],
  declarations: [UploadComponent,FilesClickComponent,FilesDropComponent],
  exports: [UploadComponent],
  providers: [FilesService, FilesUtilsService]
})
export class UploadModule { }
