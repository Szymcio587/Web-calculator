import { Component, OnInit, Inject } from '@angular/core';
import { InterpolationData } from 'src/app/shared/data/data.interface';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { MatDialogRef } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/shared/services/user/user.service';

@Component({
  selector: 'app-interpolation',
  templateUrl: './interpolation.component.html',
  styleUrls: ['./interpolation.component.css']
})
export class InterpolationComponent implements OnInit {

  data: InterpolationData = {
    dataType: '',
    username: '',
    pointsNumber: 0,
    searchedValue: 0,
    points: []
  };

  interpolationName = '';

  constructor(private http: HttpClient, private router: Router, public dialogRef: MatDialogRef<InterpolationComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: { name: string }
  ) {
    this.interpolationName = dialogData.name;
  }

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
      dataType: 'InterpolationData',
      username: UserService.getUsername(),
      points: this.data.points,
      searchedValue: this.data.searchedValue,
      pointsNumber: this.data.pointsNumber,
    };
    this.http.post<number>('http://localhost:8081/calculations/'+this.interpolationName+'_interpolation', data).subscribe(
      (result: number) => {
        ResultDataService.SetInterpolationResult(result, data);
        ResultDataService.SetResultType('Interpolation');
        this.router.navigate(['/result']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
    this.dialogRef.close();
  }

}
