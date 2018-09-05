import { DiffDashboardModule } from './diff-dashboard.module';

describe('DiffDashboardModule', () => {
  let diffDashboardModule: DiffDashboardModule;

  beforeEach(() => {
    diffDashboardModule = new DiffDashboardModule();
  });

  it('should create an instance', () => {
    expect(diffDashboardModule).toBeTruthy();
  });
});
