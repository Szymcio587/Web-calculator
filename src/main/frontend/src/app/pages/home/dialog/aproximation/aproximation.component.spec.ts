import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AproximationComponent } from './aproximation.component';

describe('AproximationComponent', () => {
  let component: AproximationComponent;
  let fixture: ComponentFixture<AproximationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AproximationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AproximationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
