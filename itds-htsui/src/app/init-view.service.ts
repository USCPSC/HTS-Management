import {Injectable} from '@angular/core';
import {CanActivate, Router, RouterStateSnapshot, ActivatedRouteSnapshot} from '@angular/router';
import {DataService} from './shared/data.service';
import {Observable, Subject} from "rxjs";

@Injectable()
export class InitViewService implements CanActivate {

  private globalState = new Subject<string>();
  private stateUpdated = new Subject<boolean>();


  constructor(private router: Router, private dataService: DataService) {
    // this.dataService.getHtsState().subscribe(
    //   data => {
    //     this.globalState.next(data);
    //     console.log(this.globalState);
    //   },
    //   error => console.log(error)
    // );
    //
    // this.globalState.asObservable().subscribe(
    //   globalState => {
    //     if (globalState === 'finalized_no_wip' || globalState === 'cpsc_current_wip') {
    //       this.router.navigate(['/htsView']);
    //     } else {
    //       this.router.navigate(['/htsDiffView']);
    //     }
    //     this.stateUpdated.next(true);
    //   },
    //   error => console.log(error)
    // );
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
    /*    const isLoggedIn = false; // ... your login logic here
        if (isLoggedIn) {
          return true;
        } else {
          this.router.navigate(['/login']);
          return false;
        }
    */
    // return this.stateUpdated.asObservable();
    return true;
  }
}
