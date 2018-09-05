import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TransitionDialogComponent } from './transition-dialog.component';

describe('TransitionDialogComponent', () => {
  let component: TransitionDialogComponent;
  let fixture: ComponentFixture<TransitionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransitionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransitionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
