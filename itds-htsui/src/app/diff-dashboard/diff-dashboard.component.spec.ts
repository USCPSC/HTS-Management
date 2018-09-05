import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiffDashboardComponent } from './diff-dashboard.component';

describe('DiffDashboardComponent', () => {
  let component: DiffDashboardComponent;
  let fixture: ComponentFixture<DiffDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiffDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiffDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
