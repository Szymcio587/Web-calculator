import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterpolationOptionsDialogComponent } from './interpolation-options-dialog.component';

describe('InterpolationOptionsDialogComponent', () => {
  let component: InterpolationOptionsDialogComponent;
  let fixture: ComponentFixture<InterpolationOptionsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InterpolationOptionsDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InterpolationOptionsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
