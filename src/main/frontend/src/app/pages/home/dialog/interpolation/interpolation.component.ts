import { Component, OnInit } from '@angular/core';
import { InterpolationData } from 'src/app/shared/data.interface';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ResultDataService } from 'src/app/shared/result-data.service';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-interpolation',
  templateUrl: './interpolation.component.html',
  styleUrls: ['./interpolation.component.css']
})
export class InterpolationComponent implements OnInit {

  data: InterpolationData = {
    pointsNumber: 0,
    searchedValue: 0,
    points: []
  };

  constructor(private http: HttpClient, private router: Router, public dialogRef: MatDialogRef<InterpolationComponent>) {}

  ngOnInit(): void {
    this.CreatePointsArray();
  }

  private CreatePointsArray() {
    this.data.points = Array.from({ length: this.data.pointsNumber }, () => ({ x: 0, y: 0 }));
  }

  PointsRange() {
    return Array.from({ length: this.data.pointsNumber }, (_, index) => index);
  }

  UpdatePoints(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const newNumber = +inputElement.value;
    this.data.pointsNumber = newNumber;
    this.CreatePointsArray();
  }

  Submit() {
    const data: InterpolationData = {
      points: this.data.points,
      searchedValue: this.data.searchedValue,
      pointsNumber: this.data.pointsNumber,
    };
    this.http.post<number>('http://localhost:8080/calculations/interpolation', data).subscribe(
      (response: number) => {
        ResultDataService.SetInterpolationResult(response, data);
        this.router.navigate(['/result']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
    this.dialogRef.close();
  }

}
