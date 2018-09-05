import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material";
import {HtsFlatNode} from "app/hts-tree/hts-flat-node";

@Component({
  selector: 'app-edit-dialog',
  templateUrl: './edit-dialog.component.html',
  styleUrls: ['./edit-dialog.component.scss']
})
export class EditDialogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: {node: HtsFlatNode; save: boolean;}) {
    console.log(this.data);
  }

  jurisdictionChanged(val: boolean) {
    if(!val){
      this.data.node.targeted = false;
    }
  }

  targetChanged(val: boolean) {
    if(val){
      this.data.node.jurisdiction = true;
    }
  }

  ngOnInit() {
  }

}
