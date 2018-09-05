import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material";

@Component({
  selector: 'app-modal-message',
  templateUrl: './modal-message.component.html',
  styleUrls: ['./modal-message.component.scss']
})
export class ModalMessageComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: { message: string; title: string; ok: boolean; }) { }

  ngOnInit() {
  }

}
