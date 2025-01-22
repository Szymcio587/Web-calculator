import { Component, OnInit, Inject } from '@angular/core';
import { InterpolationData, InterpolationResult } from 'src/app/shared/data/data.interface';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { MatDialogRef } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/shared/services/user/user.service';
import { INTERPOLATION, INTERPOLATION_DATA } from 'src/app/shared/data/data.constants';

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
    points: [],
    isTest: false
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

  LoadDataFromFile(event: Event) {
    const fileInput = event.target as HTMLInputElement;
    const file = fileInput.files?.[0];
    if (!file) {
      return;
    }

    const reader = new FileReader();
    reader.onload = (e) => {
      const fileContent = e.target?.result as string;
      this.ParseFileContent(fileContent);
    };
    reader.readAsText(file);
  }

  private ParseFileContent(content: string) {
    try {
      const lines = content.split('\n').map((line) => line.trim());
      if (lines.length < 2) {
        throw new Error('Invalid file format');
      }

      this.data.pointsNumber = parseInt(lines[0], 10);

      this.data.searchedValue = parseFloat(lines[1]);

      this.data.points = lines.slice(2).map((line) => {
        const [x, y] = line.split(',').map((value) => parseFloat(value.trim()));
        return { x, y };
      });
    } catch (error) {
      console.error('Error parsing file:', error);
      alert('Błędy rodzaj pliku. Przeczytaj podpowiedź zamieszczoną w tooltipie i wprowadź zmieniony plik.');
    }
  }

  Submit() {
    const data: InterpolationData = {
      dataType: INTERPOLATION_DATA,
      username: UserService.getUsername(),
      points: this.data.points,
      searchedValue: this.data.searchedValue,
      pointsNumber: this.data.pointsNumber,
      isTest: false
    };
    this.http.post<InterpolationResult>('http://localhost:8081/calculations/'+this.interpolationName+'_interpolation', data).subscribe(
      (result: InterpolationResult) => {
        ResultDataService.SetInterpolationResult(result, data);
        ResultDataService.SetResultType(INTERPOLATION);
        this.router.navigate(['/result']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
    this.dialogRef.close();
  }

}
