import {ENTER} from "@angular/cdk/keycodes";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {Injectable} from '@angular/core';
import {MatDialog} from "@angular/material";
import {DataService} from "app/shared/data.service";
import {ModalMessageComponent} from "app/shared/dialog/modal-message/modal-message.component";
import {UserComponent} from "app/shared/dialog/user/user.component";
import {environment} from "environments/environment";
import {of} from "rxjs";
import {flatMap, map, retryWhen, tap} from "rxjs/operators";

@Injectable()
export class AuthenticationService {

  user: string;
  authenticated: boolean;
  private errorMessage: string = null;

  constructor(private http: HttpClient,
              private ds: DataService,
              public dialog: MatDialog) {
  }

  initAuthenticate() {
    return new Promise((resolve) => {
      // this.isSSOEnabled();
      // this.ssoLogin();
      this.authenticate();
      resolve(true);
    });
  }

  authenticate() {
	  var bypassSSO = 'basic';
	  if (environment.authentication !== null &&
			  environment.authentication !== undefined && 
			  bypassSSO === environment.authentication.toLowerCase()) {
		  return this.authenticateViaBasic(); 
	  } else {
		  return this.authenticateViaSSO();
	  }
  }
  
  authenticateViaSSO() {
    of(this.ds.isSSOEnabled()).pipe(
      flatMap(sso => {
	      return this.ssoLogin().pipe(
	        map((user: string) => user.substring(1, user.length - 1)));
      }),
      map(user => {
        console.log('map', user);
        if (user) {
          return user.trim();
        }
        throw "User is not defined";
        // throwError("User is not defined");
      }),
      flatMap(user => {
        console.log("before login User", user);
        if (user.length === 0) {
          throw "User is empty";
          // return throwError("User is empty");
        }
        return this.login({username: user, password: 'Cpsc#1'});
      }),
      map((roles: any) => {
        if (roles === null
          || !roles.authenticated
          || roles.authorities === null
          || !this.isAuthorized(roles.authorities)) {
          this.dialog.open(ModalMessageComponent, {
            hasBackdrop: true,
            disableClose: true,
            closeOnNavigation: false,
            width: '250px',
            data: {
              message: "Permission denied",
              title: "HTS Security",
              ok: false
            }
          });
          return null;
        }
        this.authenticated = roles.authenticated;
        this.user = roles.name;
        return roles;
      }),
      retryWhen(err => err.pipe(
        tap(error => {
            console.log('Error', error);
            if (error instanceof HttpErrorResponse) {
              this.errorMessage = error.statusText;
            } else {
              this.errorMessage = <string>error;
            }
            return error;
          }
        )))
    ).subscribe(res => {
      console.log("Auth", res);
      if (res) {
        this.ds.startPollingState();
        this.ds.getStatistics();
      }
    }, error => {
      console.log("Sub Errr", error);
    });
  }

  authenticateViaBasic() {
    of(this.ds.isSSOEnabled()).pipe(
      flatMap(sso => {
          let dialogRef = this.dialog.open(UserComponent, {
            hasBackdrop: true,
            disableClose: true,
            closeOnNavigation: false,
            width: '250px',
            data: {
              error: this.errorMessage,
              user: "",
              passwd: ""
            }
          });
          dialogRef.keydownEvents().subscribe(v => {
            if (v.keyCode === ENTER) {
              dialogRef.close(dialogRef.componentInstance.data);
            }
          });
          return dialogRef.afterClosed();
      }),
      map(drac => {
        console.log('map', drac.user);
        if (drac.user) {
          return {user2: drac.user.trim(), passwd2: drac.passwd};
        }
        throw "User is not defined";
      }),
      flatMap(({user2, passwd2}) => {
        console.log("before login User", user2);
        if (user2.length === 0) {
          throw "User is empty";
        }
        return this.login({username: user2, password: passwd2});
      }),
      map((roles: any) => {
        if (roles === null
          || !roles.authenticated
          || roles.authorities === null
          || !this.isAuthorized(roles.authorities)) {
          this.dialog.open(ModalMessageComponent, {
            hasBackdrop: true,
            disableClose: true,
            closeOnNavigation: false,
            width: '250px',
            data: {
              message: "Permission denied",
              title: "HTS Security",
              ok: false
            }
          });
          return null;
        }
        this.authenticated = roles.authenticated;
        this.user = roles.name;
        return roles;
      }),
      retryWhen(err => err.pipe(
        tap(error => {
            console.log('Error', error);
            if (error instanceof HttpErrorResponse) {
              this.errorMessage = error.statusText;
            } else {
              this.errorMessage = <string>error;
            }
            return error;
          }
        )))
    ).subscribe(res => {
      console.log("Auth", res);
      if (res) {
        this.ds.startPollingState();
        this.ds.getStatistics();
      }
    }, error => {
      console.log("Sub Errr", error);
    });
  }
  
  isAuthorized(permissions: { authority: string }[]): boolean {
    let model: [string, boolean][] = [
      ["ROLE_MODEL_DEL", false],
      ["ROLE_MODEL_READ", false],
      ["ROLE_MODEL_SAVE", false],
    ];
    return permissions.some((permission: { authority: string }) => model.every(tuple => tuple[1] = tuple[1] || tuple[0] === permission.authority));
  }

  ssoLogin() {
    return this.http.get(environment.backend + 'login/sso',
      {
        withCredentials: true,
        responseType: 'text'
      });
  }

  login(credentials: { username: string; password: string; }) {
    return this.http.get(environment.backend + 'login',
      {
        withCredentials: true,
        headers: new HttpHeaders(credentials ? {
          authorization: "Basic " + btoa(credentials.username
            + ":" + credentials.password)
        } : {})
      });
  }

}
