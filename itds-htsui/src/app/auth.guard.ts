import {Injectable, OnDestroy} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {Subscription} from "rxjs/Subscription";
import {DataService} from "./shared/data.service";
import {GlobalStateEnum, HtsState} from "./shared/hts.model";

@Injectable()
export class AuthGuard implements CanActivate, OnDestroy {
  subscription: Subscription;
  state: HtsState;
  private redirectUrl: string;

  constructor(private router: Router,
              private dataService: DataService) {
    this.subscription = this.dataService.state.subscribe(
      (data: HtsState) => {
        this.state = data;
        if(this.state.globalState === GlobalStateEnum.edit){
          this.router.navigate(["/view"]);
        }
        // if(this.state.globalState === GlobalStateEnum.upload){
        //   this.router.navigate(["/diff"]);
        // }
      });
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    // if (state.url === '/view' && this.state === GlobalStateEnum.upload) {
    //   this.router.navigate(["/diff"]);
    //   return false;
    // }
    if (state.url === '/diff' && this.state.globalState === GlobalStateEnum.edit) {
      this.router.navigate(["/view"]);
      return false;
    }
    return true;
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
