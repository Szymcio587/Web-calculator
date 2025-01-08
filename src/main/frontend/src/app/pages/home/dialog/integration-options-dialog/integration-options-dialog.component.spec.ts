import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntegrationOptionsDialogComponent } from './integration-options-dialog.component';

describe('IntegrationOptionsDialogComponent', () => {
  let component: IntegrationOptionsDialogComponent;
  let fixture: ComponentFixture<IntegrationOptionsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntegrationOptionsDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IntegrationOptionsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
