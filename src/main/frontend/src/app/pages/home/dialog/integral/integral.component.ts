import { Component, OnInit, Inject } from '@angular/core';
import { IntegrationData } from 'src/app/shared/data/data.interface';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { MatDialogRef } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-integral',
  templateUrl: './integral.component.html',
  styleUrls: ['./integral.component.css']
})
export class IntegralComponent implements OnInit {

  data: IntegrationData = {
    degree: 0,
    factors: [],
    sections: 0,
    Xp: 0,
    Xk: 0
  };

  integralName = '';

  constructor(private http: HttpClient, private router: Router,public dialogRef: MatDialogRef<IntegralComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: { name: string }
  ) {
    console.log('Received data:', dialogData.name);
    this.integralName = dialogData.name;
  }

  ngOnInit(): void {}

  FactorsArray() {
    const range = Array.from({ length: this.data.degree + 1}, (_, index) => index);
    return range.reverse();
  }

  UpdateDegree(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const newNumber = +inputElement.value;
    this.data.degree = newNumber;
  }

  Submit() {
    const data: IntegrationData = {
      degree: this.data.degree,
      factors: this.data.factors,
      sections: this.data.sections,
      Xp: this.data.Xp,
      Xk: this.data.Xk
    };
    this.http.post<number>('http://localhost:8081/calculations/'+this.integralName+'_integration', data).subscribe(
      (response: number) => {
        ResultDataService.SetIntegrationResult(response, data);
        this.router.navigate(['/result']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
    this.dialogRef.close();
  }

}
