import {Component, Input, OnDestroy} from '@angular/core';
import {Subscription} from "rxjs/Subscription";
import {environment} from '../../environments/environment';
import {DataService} from "../shared/data.service";
import {GlobalStateEnum, HtsState} from "../shared/hts.model";

@Component({
  selector: 'app-actions',
  templateUrl: './actions.component.html',
  styleUrls: ['./actions.component.scss']
})
export class ActionsComponent implements OnDestroy {
  @Input() current = true;
  subscription: Subscription;
  state: HtsState;
  @Input() link: string;

  constructor(public dataService: DataService) {
    this.subscription = this.dataService.state.subscribe(
      (data: HtsState) => {
        this.state = data;
        console.log("Action", this.state);
      });
  }

  getLink() {
    document.location.href = environment.backend + this.link;
  }


  isEditEnabled(): boolean {
    return this.current && this.state.globalState === GlobalStateEnum.finalized;
  }

  isAbortApplyEnabled(): boolean {
    return (this.current && this.state.globalState === GlobalStateEnum.edit)
      || (!this.current && this.state.globalState === GlobalStateEnum.upload);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
