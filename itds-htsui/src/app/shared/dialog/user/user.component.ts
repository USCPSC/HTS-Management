import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: { error: string; user: string; }) {
  }

  ngOnInit() {
  }

}
