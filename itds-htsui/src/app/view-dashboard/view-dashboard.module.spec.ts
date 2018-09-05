import { ViewDashboardModule } from './view-dashboard.module';

describe('ViewDashboardModule', () => {
  let viewDashboardModule: ViewDashboardModule;

  beforeEach(() => {
    viewDashboardModule = new ViewDashboardModule();
  });

  it('should create an instance', () => {
    expect(viewDashboardModule).toBeTruthy();
  });
});
