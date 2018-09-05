import {HttpClientModule} from '@angular/common/http';
import {APP_INITIALIZER, NgModule} from '@angular/core';
import {FlexLayoutModule} from "@angular/flex-layout";
import {FormsModule} from '@angular/forms'
import {
  MatButtonModule,
  MatCardModule,
  MatGridListModule,
  MatIconModule,
  MatMenuModule,
  MatNativeDateModule,
  MatTabsModule
} from '@angular/material';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AuthGuard} from "app/auth.guard";
import {FooterModule} from "app/footer/footer.module";
import {AuthenticationService} from "app/shared/authentication.service";
import {AppBarModule} from "./app-bar/app-bar.module";
import {AppComponent} from './app.component';
import {AppRoutingModule} from "./app.routing.module";
import {BarMenuModule} from "./bar-menu/bar-menu.module";
import {DiffDashboardModule} from "./diff-dashboard/diff-dashboard.module";
import {InitViewService} from './init-view.service'
import {NavbarComponent} from './navbar/navbar.component';
import {DataService} from './shared/data.service';
import {DialogModule} from "./shared/dialog/dialog.module";
import {StatusViewComponent} from './status-view/status-view.component';
import {ViewDashboardModule} from "./view-dashboard/view-dashboard.module";

export function authenticate(auth: AuthenticationService) {
  function init() {
    return auth.initAuthenticate();
  }
  return init;
}

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    StatusViewComponent,
  ],
  imports: [
    BrowserModule,
    FlexLayoutModule,
    FormsModule,
    ViewDashboardModule,
    DiffDashboardModule,
    AppRoutingModule,
    MatNativeDateModule,
    MatTabsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatProgressSpinnerModule,
    AppBarModule,
    FooterModule,
    BarMenuModule,
    MatGridListModule,
    MatCardModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    DialogModule,
  ],
  providers: [
    AuthenticationService,
    DataService,
    {
      provide: APP_INITIALIZER,
      useFactory: authenticate,
      deps: [AuthenticationService],
      multi: true
    },
    AuthGuard,
    InitViewService],
  bootstrap: [AppComponent],
})
export class AppModule {
}
