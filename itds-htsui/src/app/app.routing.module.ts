import {RouterModule, Routes} from "@angular/router";

/**
 *
 * AppRoutingModule module used to route based on selected routes.
 */

const APP_ROUTES: Routes = [
  {path: '', redirectTo: 'view', pathMatch: "full"},
];

export const AppRoutingModule = RouterModule.forRoot(APP_ROUTES);
