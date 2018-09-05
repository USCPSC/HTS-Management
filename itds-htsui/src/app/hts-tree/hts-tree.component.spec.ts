import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HtsTreeComponent } from './hts-tree.component';

describe('HtsTreeComponent', () => {
  let component: HtsTreeComponent;
  let fixture: ComponentFixture<HtsTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HtsTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HtsTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
