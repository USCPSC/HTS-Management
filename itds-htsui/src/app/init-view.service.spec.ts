import { TestBed, inject } from '@angular/core/testing';

import { InitViewService } from './init-view.service';

describe('InitViewService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [InitViewService]
    });
  });

  it('should be created', inject([InitViewService], (service: InitViewService) => {
    expect(service).toBeTruthy();
  }));
});
