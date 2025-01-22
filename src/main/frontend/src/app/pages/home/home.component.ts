import { Component, OnInit, ViewContainerRef, ComponentFactoryResolver, Inject, Renderer2, ElementRef } from '@angular/core';
import {MatDialog, MatDialogRef, MatDialogConfig, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { InterpolationComponent } from './dialog/interpolation/interpolation.component';
import { IntegralComponent } from './dialog/integral/integral.component';
import { SystemOfEquationsComponent } from './dialog/system-of-equations/system-of-equations.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {

  dialogInterpolation: MatDialogRef<InterpolationComponent> | null = null;
  dialogIntegral: MatDialogRef<IntegralComponent> | null = null;
  dialogSystemOfEquations: MatDialogRef<SystemOfEquationsComponent> | null = null;
  @Inject(MAT_DIALOG_DATA) public data: any =  { };
  public isVisible: boolean[];
  visibleTooltip: string | null = null;

  config: MatDialogConfig = {
      disableClose: false,
      hasBackdrop: true,
      backdropClass: '',
      position: {
          top: '',
          bottom: '',
          left: '',
          right: ''
      },
      panelClass: 'unique'
  };

  constructor(private dialog: MatDialog, private renderer: Renderer2, private el: ElementRef) {
    this.isVisible = new Array(4).fill(false);
  }

  ngOnInit(): void {}

  OpenInterpolation(name: string): void {
    this.dialogInterpolation = this.dialog.open(InterpolationComponent, {...this.config  , data: { name: name }});
    this.dialogInterpolation.addPanelClass('dialog');
  }

 OpenIntegral(name: string): void {
  this.dialogIntegral = this.dialog.open(IntegralComponent,  {...this.config  , data: { name: name }});
  this.dialogIntegral.addPanelClass('dialog');
 }

 OpenSystemOfEquations(): void {
  this.dialogSystemOfEquations = this.dialog.open(SystemOfEquationsComponent, this.config);
  this.dialogSystemOfEquations.addPanelClass('dialog');
 }

 toggleTooltip(option: string) {
  this.visibleTooltip = this.visibleTooltip === option ? null : option;
}

 Collapse(number: string): void {
    const divElements = document.getElementsByClassName(`option ${number}`);
    const space = document.getElementById(`space-taker-${number}`);
    Array.from(divElements).forEach((div) => {
      const currentVisibility = window.getComputedStyle(div).getPropertyValue('visibility');
      if (currentVisibility === 'visible') {
        this.renderer.setStyle(div, 'visibility', 'hidden');
        this.renderer.setStyle(space, 'display', 'none');
      } else {
        this.renderer.setStyle(div, 'visibility', 'visible');
        this.renderer.setStyle(space, 'display', 'block');
      }
    });

    const numberValue = parseInt(number, 10) - 1;
    if(this.isVisible.at(numberValue) == false)
      this.isVisible[numberValue] = true;
    else
      this.isVisible[numberValue] = false;
  }



}
