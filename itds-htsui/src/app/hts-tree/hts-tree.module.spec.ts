import { HtsTreeModule } from './hts-tree.module';

describe('HtsTreeModule', () => {
  let htcTreeModule: HtsTreeModule;

  beforeEach(() => {
    htcTreeModule = new HtsTreeModule();
  });

  it('should create an instance', () => {
    expect(htcTreeModule).toBeTruthy();
  });
});
