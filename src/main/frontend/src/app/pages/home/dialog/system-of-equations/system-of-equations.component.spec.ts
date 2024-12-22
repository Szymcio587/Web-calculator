import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemOfEquationsComponent } from './system-of-equations.component';

describe('SystemOfEquationsComponent', () => {
  let component: SystemOfEquationsComponent;
  let fixture: ComponentFixture<SystemOfEquationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemOfEquationsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SystemOfEquationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
