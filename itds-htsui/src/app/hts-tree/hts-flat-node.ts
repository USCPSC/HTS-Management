export class HtsFlatNode {
  index: number;
  code: string;
  startDate: string;
  endDate: string;
  description: string;
  cdescription?: string;
  jurisdiction: boolean;
  // jindef: boolean;
  targeted: boolean;
  // tindef: boolean;
  level: number;
  expandable: boolean;
  notes: string;
  changeStatus: string;
  isExpanded: boolean;
  isVisible: boolean;

  modified: boolean;
  sunset: boolean;

  getColor(): string {
    return this.changeStatus === 'remove' ? 'red' :
      this.changeStatus === 'add'? 'green': 'default' ;
  }
}
