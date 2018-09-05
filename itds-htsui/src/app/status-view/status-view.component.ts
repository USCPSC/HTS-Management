import { Component, OnInit } from '@angular/core';
import {MatTabChangeEvent} from '@angular/material';
import { Router } from "@angular/router";


@Component({
    selector: 'app-status-view',
    templateUrl: './status-view.component.html',
    styleUrls: ['./status-view.component.css']
})
export class StatusViewComponent implements OnInit {

    constructor(private router: Router) { }


    private tabState1: boolean;
    private tabState2: boolean;

    ngOnInit() {
    }

    tabState(event: MatTabChangeEvent) {

        if (event.index === 0) {
            this.router.navigate(['htsView']);
        } else {
            this.router.navigate(['htsDiffView']);
        }
    }

    uploadFile() {
        console.log('Uploading file');

    }
}


