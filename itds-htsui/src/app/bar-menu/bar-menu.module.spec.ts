import { BarMenuModule } from './bar-menu.module';

describe('BarMenuModule', () => {
  let barMenuModule: BarMenuModule;

  beforeEach(() => {
    barMenuModule = new BarMenuModule();
  });

  it('should create an instance', () => {
    expect(barMenuModule).toBeTruthy();
  });
});
