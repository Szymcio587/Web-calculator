import { Component, OnInit, SimpleChanges, Input, ViewChild, ElementRef, ViewContainerRef, ComponentFactoryResolver, ViewEncapsulation, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {MatDialog, MatDialogRef, MatDialogConfig, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { InterpolationComponent } from './dialog/interpolation/interpolation.component';
import { AproximationComponent } from './dialog/aproximation/aproximation.component';
import { IntegralComponent } from './dialog/integral/integral.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {

  dialogInterpolation: MatDialogRef<InterpolationComponent> | null = null;
  dialogIntegral: MatDialogRef<IntegralComponent> | null = null;
  @Inject(MAT_DIALOG_DATA) public data: any =  { };

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

  constructor(public dialog: MatDialog, private componentFactoryResolver: ComponentFactoryResolver,
    private viewContainerRef: ViewContainerRef) {}

  ngOnInit(): void {}

  OpenInterpolation(): void {
    this.dialogInterpolation = this.dialog.open(InterpolationComponent, this.config);
    this.dialogInterpolation.addPanelClass('dialog');
  }

  OpenAproximation(): void {
    this.dialog.open(AproximationComponent, this.config);
 }

 OpenIntegral(): void {
  this.dialogIntegral = this.dialog.open(IntegralComponent, this.config);
  this.dialogIntegral.addPanelClass('dialog');
 }

}
