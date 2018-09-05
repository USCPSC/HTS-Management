import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "app/auth.guard";
import {DiffDashboardComponent} from "./diff-dashboard.component";

@NgModule({
  imports: [
    RouterModule.forChild([
      {path: 'diff', component: DiffDashboardComponent, canActivate: [AuthGuard]},
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
export class DiffRoutingModule {
}
