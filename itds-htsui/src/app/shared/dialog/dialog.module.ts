import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FlexLayoutModule} from "@angular/flex-layout";
import {FormsModule} from "@angular/forms";
import {
  MatButtonModule,
  MatCheckboxModule,
  MatDatepickerModule,
  MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatProgressSpinnerModule
} from "@angular/material";
import {ExceptionDialogComponent} from "app/shared/dialog/exception-dialog/exception-dialog.component";
import {ConfirmationDialogComponent} from './confirmation-dialog/confirmation-dialog.component';
import {EditDialogComponent} from './edit-dialog/edit-dialog.component';
import {ModalMessageComponent} from './modal-message/modal-message.component';
import {ReportsDialogComponent} from './reports-dialog/reports-dialog.component';
import {TransitionDialogComponent} from './transition-dialog/transition-dialog.component';
import {UserComponent} from './user/user.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatDatepickerModule,
    FlexLayoutModule,
    MatProgressSpinnerModule
  ],
  declarations: [ConfirmationDialogComponent, EditDialogComponent,
    TransitionDialogComponent,
    ReportsDialogComponent, UserComponent, ModalMessageComponent,
    ExceptionDialogComponent],
  entryComponents: [ConfirmationDialogComponent, EditDialogComponent,
    TransitionDialogComponent,
    ReportsDialogComponent, UserComponent,
    ModalMessageComponent, ExceptionDialogComponent]
})
export class DialogModule {
}
