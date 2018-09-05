import { AppBarModule } from './app-bar.module';

describe('AppBarModule', () => {
  let appBarModule: AppBarModule;

  beforeEach(() => {
    appBarModule = new AppBarModule();
  });

  it('should create an instance', () => {
    expect(appBarModule).toBeTruthy();
  });
});
