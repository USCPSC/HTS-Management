import {Component, HostListener, Input, OnDestroy, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material";
import {HtsFlatNode} from "app/hts-tree/hts-flat-node";
import {DataService} from "app/shared/data.service";
import {EditDialogComponent} from "app/shared/dialog/edit-dialog/edit-dialog.component";
import {GlobalStateEnum, HtsTree, HtsTreeNode} from "app/shared/hts.model";
import {Observable} from "rxjs";
import {BehaviorSubject} from "rxjs/BehaviorSubject";
import {map} from "rxjs/operators";
import {Subscription} from "rxjs/Subscription";

// TODO: change it if you changed styling of other elements in dashboard
const heightShift = 353;

@Component({
  selector: 'app-hts-tree',
  templateUrl: './hts-tree.component.html',
  styleUrls: ['./hts-tree.component.scss']
})
export class HtsTreeComponent implements OnInit, OnDestroy {
  @Input() expand = false;
  @Input() current = true;
  data: HtsFlatNode[];
  observableData = new BehaviorSubject<HtsFlatNode[]>([]);
  transformer = (node: HtsTreeNode, level: number, desc: string): HtsFlatNode => {
    let flatNode = new HtsFlatNode();
    flatNode.code = node.data.htsCode;
    flatNode.modified = node.data.modified === 'true';
    flatNode.sunset = node.data.sunset === 'true';
    // console.log(node.data.htsCode);
    flatNode.description = node.data.htsDescription;
    if (desc) {
      flatNode.cdescription = desc + "; " + node.data.htsDescription;
    } else {
      flatNode.cdescription = node.data.htsDescription;
    }
    flatNode.endDate = node.data.endDate;
    flatNode.startDate = node.data.startDate;
    flatNode.jurisdiction = (node.data.jurisdiction === 'true');
    flatNode.targeted = (node.data.targeted === 'true');
    flatNode.notes = node.data.notes;
    flatNode.changeStatus = node.data.changeStatus;
    // console.log(flatNode.code, flatNode.jurisdiction, flatNode.targeted, node.data.jurisdiction, node.data.targeted);
    flatNode.level = level;
    flatNode.isVisible = flatNode.level === 0;
    flatNode.expandable = !!node.children && node.children.length > 0;
    flatNode.isExpanded = false;
    // console.log(level);
    return flatNode;
  };
  innerHeight = window.innerHeight;
  // isIEOrEdge = /msie\s|trident\/|edge\//i.test(window.navigator.userAgent);
  // isFirefox = /firefox/i.test(window.navigator.userAgent);
  height = 'auto';
  _flat = false;
  private sub: Subscription;

  constructor(public dataService: DataService,
              public dialog: MatDialog) {
    // console.log('ctor Height', this.innerHeight);
    this.calculateHeight();
  }

  @Input() set flatView(inp: boolean) {
    this._flat = inp;
    if (this.data) {
      this.observableData.next([]);
      setTimeout(()=>{
        if (inp) {
          this.createFlatView();
        } else {
          this.prepareView();
        }
      },0);
    }
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.innerHeight = window.innerHeight;
    // console.log('Height', this.innerHeight);
    this.calculateHeight();
  }

  isLoading(): Observable<boolean> {
    return this.current ? this.dataService.current_load : this.dataService.update_load;
  }

  getLongDescription(ind: number): string {
    if (this.data[ind].level > 0) {
      for (let i = ind - 1; i >= 0; i--) {
        if (this.data[i].level < this.data[ind].level) {
          return this.getLongDescription(i) + '; ' + this.data[ind].description;
        }
      }
    }
    return this.data[ind].description;
  }

  edit(node: HtsFlatNode) {
    // console.log("Edit", node);
    let clone = {...node};
    // clone.cdescription = this.getLongDescription(node.index);
    // console.log("Open dialog", this.isAllowedEdit());
    // this.dataService.getLongDesc(this.source, clone.code).subscribe((desc: string) => {
    //   clone.cdescription = desc;
    // });
    let dialogRef = this.dialog.open(EditDialogComponent, {
      width: '700px',
      // panelClass: "confirmation",
      data: {
        node: clone,
        save: this.isAllowedEdit() && node.changeStatus != 'remove'
      }
    });
    if (this.isAllowedEdit() && node.changeStatus != 'remove') {
      // let ind = this.data.indexOf(node, 0);
      dialogRef.afterClosed().subscribe(result => {
        // console.log('The dialog was closed', result);
        if (result) {
          node.description = clone.description;
          node.endDate = clone.endDate;
          node.startDate = clone.startDate;
          node.notes = clone.notes;
          node.targeted = clone.targeted;
          node.jurisdiction = clone.jurisdiction;
          node.cdescription = this.getLongDescription(clone.index);
          node.modified = true;
          this.dataService.save(node);
          if (node.expandable) {
            // traverse down
            // console.log(ind + 1, nodes.length, nodes[ind + 1].code, nodes[ind + 1].code.length, node.code.length, node.code);
            for (let i = node.index + 1; i < this.data.length && this.data[i].code.length > node.code.length; i++) {
              this.data[i].targeted = clone.targeted;
              if (this.data[i].targeted) {
                this.data[i].jurisdiction = true;
              } else {
                this.data[i].jurisdiction = clone.jurisdiction;
              }
            }
          }
        }
      });
    }
  }

  toFlatTree(node: HtsTreeNode, desc: string): HtsFlatNode[] {
    let fn = [this.transformer(node, node.data.htsCode.length / 2 - 1, desc)];
    if (node.children) {
      if (node.children.length > 0) {
        return fn.concat(node.children.reduce((arr: HtsFlatNode[], n: HtsTreeNode) => arr.concat(this.toFlatTree(n, fn[0].cdescription)), []));
      }
    }
    return fn;
  }

  toggleVisibility(ind: number) {
    for (let i = ind + 1; i < this.data.length && this.data[i].level > this.data[ind].level; i++) {
      if (this.data[i].level === this.data[ind].level + 1) {
        this.data[i].isVisible = this.data[ind].isExpanded && this.data[ind].isVisible;
        if (this.data[i].isExpanded) {
          this.toggleVisibility(i);
        }
      }
    }
  }

  expandIt(node: HtsFlatNode) {
    node.isExpanded = !node.isExpanded;
    this.toggleVisibility(this.data.indexOf(node, 0));
    this.observableData.next(this.data.filter(node => node.isVisible));
  }

  createExpandedView(): HtsFlatNode[] {
    return this.data.map(n => {
      n.isExpanded = n.expandable;
      n.isVisible = true;
      return n;
    });
  }

  initData(subj: BehaviorSubject<HtsTree>) {
    this.sub = subj.pipe(
      map((tree: HtsTree) => tree.htsView),
    ).subscribe(data => {
      this.data = data.reduce((arr: HtsFlatNode[], n: HtsTreeNode) => arr.concat(this.toFlatTree(n, null)), []);
      for (let i = 0; i < this.data.length; i++) {
        this.data[i].index = i;
      }
      if (this.current) {
        if (this._flat) {
          this.createFlatView();
        } else {
          this.prepareView();
        }
      } else {
        let levels: HtsFlatNode[] = [];
        for (let i = 0; i < this.data.length; i++) {
          levels[this.data[i].level] = this.data[i];
          if (this.data[i].changeStatus === 'remove' || this.data[i].changeStatus === 'add') {
            for (let j = 0; j < this.data[i].level; j++) {
              if (levels[j].expandable) {
                levels[j].isExpanded = true;
                this.toggleVisibility(this.data.indexOf(levels[j], 0));
              }
            }
          }
        }
        this.observableData.next(this.data.filter(node => node.isVisible));
      }
    });
  }

  ngOnInit() {
    if (this.current) {
      this.initData(this.dataService.allData);
    } else {
      this.initData(this.dataService.diffs);
    }
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  getMargin() {
    if (this.observableData.value.length * 30 > parseInt(this.height, 10)) {
      return "47px";
    }
    return "32px";
  }

  private calculateHeight() {
    // if (this.isIEOrEdge || this.isFirefox) {
    this.height = this.innerHeight - heightShift + 'px';
    // }
  }

  private createFlatView() {
    this.observableData.next(
      this.data.map(n => {
        n.isVisible = n.level === 4;
        return n;
      }).filter(node => node.isVisible));
  }

  private prepareView() {
    if (this.expand) {
      this.observableData.next(this.createExpandedView().filter(node => node.isVisible));
    } else {
      this.observableData.next(this.data.map(n => {
        n.isExpanded = false;
        n.isVisible = n.expandable && n.level === 0;
        return n;
      }).filter(node => node.isVisible));
    }
  }

  private isAllowedEdit() {
    return (this.current && this.dataService.state.value.globalState === GlobalStateEnum.edit)
      || (!this.current && this.dataService.state.value.globalState === GlobalStateEnum.upload);
  }
}
