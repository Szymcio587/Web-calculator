import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-interpolation-options-dialog',
  templateUrl: './interpolation-options-dialog.component.html',
  styleUrls: ['./interpolation-options-dialog.component.css']
})
export class InterpolationOptionsDialogComponent {
  constructor(public dialogRef: MatDialogRef<InterpolationOptionsDialogComponent>) {}

  chooseOption(type: string): void {
    this.dialogRef.close(type);
  }
}

