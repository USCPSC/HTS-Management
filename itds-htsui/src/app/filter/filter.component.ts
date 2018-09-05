import {ENTER} from "@angular/cdk/keycodes";
import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatSlideToggleChange} from "@angular/material";
import {Filter, FilterEnum, SourceEnum} from "app/shared/hts.model";

@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.scss']
})
export class FilterComponent implements OnInit {

  @Input() current = true;
  @Input() filter: FilterEnum;
  @Input() term = '';
  @Output() onFilterChange = new EventEmitter<Filter>();
  @Output() onViewChange = new EventEmitter<boolean>();
  @Output() onSourceChange = new EventEmitter<SourceEnum>();
  flatView = false;
  srcToggle: boolean;

  constructor() {
  }

  @Input("source") set setSource(src: SourceEnum) {
    this.srcToggle = src === SourceEnum.upload_diffs;
  }

  onFLatViewChange(ev: MatSlideToggleChange) {
    this.onViewChange.emit(ev.checked);
  }

  onSourceToggle(ev: MatSlideToggleChange) {
    this.onSourceChange.emit(ev.checked ? SourceEnum.upload_diffs : SourceEnum.upload_all);
  }

  filterChanged(sel: FilterEnum) {
    this.onFilterChange.emit({
      filter: sel,
      search: this.term.trim().length > 0 ? this.term.trim() : null
    });
  }

  termChanged() {
    this.onFilterChange.emit({
      filter: this.filter,
      search: this.term
    });
  }

  handleKeydown(event: KeyboardEvent): void {
    const keyCode = event.keyCode;

    if (keyCode === ENTER) {
      event.preventDefault();
      this.termChanged();
    }
  }

  ngOnInit() {
  }

}
