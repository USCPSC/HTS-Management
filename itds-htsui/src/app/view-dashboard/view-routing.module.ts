import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
// import {AuthGuard} from "app/auth.guard";
import {ViewDashboardComponent} from "./view-dashboard.component";

@NgModule({
  imports: [
    RouterModule.forChild([
      {path: 'view', component: ViewDashboardComponent},
      // {path: 'view', component: ViewDashboardComponent, canActivate: [AuthGuard]},
      // {
      //   path: 'home', component: HomeComponent, canActivate: [AuthGuard], data: {
      //     protection: "home",
      //     title: 'Navigator-Home'
      //   }
      // },
      // {
      //   path: 'home/profile', component: UserProfileComponent, canActivate: [AuthGuard], data: {
      //     protection: "home",
      //     title: "Navigator-User Profile"
      //   }
      // }
    ])
  ],
  exports: [RouterModule]
})
export class ViewRoutingModule {
}
