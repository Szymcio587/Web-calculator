import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-integration-options-dialog',
  templateUrl: './integration-options-dialog.component.html',
  styleUrls: ['./integration-options-dialog.component.css']
})
export class IntegrationOptionsDialogComponent {
  constructor(public dialogRef: MatDialogRef<IntegrationOptionsDialogComponent>) {}

  chooseOption(type: string): void {
    this.dialogRef.close(type);
  }
}
