import { Component, OnInit, SimpleChanges, Input, ViewChild, ElementRef, ViewContainerRef, ComponentFactoryResolver, ViewEncapsulation, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {MatDialog, MatDialogRef, MatDialogConfig, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { InterpolationComponent } from './dialog/interpolation/interpolation.component';
import { AproximationComponent } from './dialog/aproximation/aproximation.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {

  dialogRef: MatDialogRef<InterpolationComponent> | null = null;
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

  OpenInter(): void {
    this.dialogRef = this.dialog.open(InterpolationComponent, this.config);
    this.dialogRef.addPanelClass('dialog');
  }

  OpenApro(): void {
    this.dialog.open(AproximationComponent, this.config);
 }

}
