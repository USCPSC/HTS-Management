import {Component, Input, OnInit} from '@angular/core';
import {HtsFlatNode} from "app/hts-tree/hts-flat-node";

@Component({
  selector: 'app-node-data',
  templateUrl: './node.component.html',
  styleUrls: ['./node.component.scss']
})
export class NodeComponent implements OnInit {
  _flat: boolean;
  _node: HtsFlatNode;
  classes: string[] = [];
  @Input() set node(nd: HtsFlatNode){
    this._node = nd;
    this.classes = [nd.getColor()];
    if(this._flat){
      this.classes.push("ellipsis reverse-ellipsis");
    }
  }
  @Input() set flat(viewFlat: boolean){
    this._flat = viewFlat;
    if(this._node) {
      this.classes = [this._node.getColor()];
    } else {
      this.classes = [];
    }
    if(viewFlat){
      this.classes.push("ellipsis reverse-ellipsis");
    }
  }

  constructor() { }

  ngOnInit() {
  }

}
