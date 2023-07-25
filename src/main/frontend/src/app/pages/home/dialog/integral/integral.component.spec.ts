import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntegralComponent } from './integral.component';

describe('IntegralComponent', () => {
  let component: IntegralComponent;
  let fixture: ComponentFixture<IntegralComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntegralComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IntegralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
