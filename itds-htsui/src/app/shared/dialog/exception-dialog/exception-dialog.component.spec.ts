import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExceptionDialogComponent } from './exception-dialog.component';

describe('TransitionDialogComponent', () => {
  let component: ExceptionDialogComponent;
  let fixture: ComponentFixture<ExceptionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExceptionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExceptionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
