import { Component, OnInit, Inject, ChangeDetectorRef } from '@angular/core';
import { IntegrationData, IntegrationResult } from 'src/app/shared/data/data.interface';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { MatDialogRef } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/shared/services/user/user.service';
import { INTEGRATION, INTEGRATION_DATA, JAVA_URL } from 'src/app/shared/data/data.constants';
import { CalculationsService } from 'src/app/shared/services/calculations/calculations.service';

@Component({
  selector: 'app-integral',
  templateUrl: './integral.component.html',
  styleUrls: ['./integral.component.css']
})
export class IntegralComponent implements OnInit {

  data: IntegrationData = {
    dataType: '',
    username: '',
    degree: 0,
    factors: [],
    sections: 0,
    Xp: 0,
    Xk: 0,
    isTest: false,
    customFunction: ''
  };

  integralName = '';

  constructor(private http: HttpClient, private router: Router,public dialogRef: MatDialogRef<IntegralComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: { name: string }, private cdr: ChangeDetectorRef
  ) {
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
      if (lines.length < 3) {
        throw new Error('Invalid file format');
      }

      this.data.degree = parseInt(lines[0]);

      this.data.factors = lines[1].split(' ').map((value) => parseFloat(value.trim())).filter((value) => !isNaN(value));

      this.data.sections = parseInt(lines[2]);

      this.data.Xp = parseFloat(lines[3]);

      this.data.Xk = parseFloat(lines[4]);

      this.cdr.detectChanges();
    } catch (error) {
      console.error('Error parsing file:', error);
      alert('Błędy rodzaj pliku. Przeczytaj podpowiedź zamieszczoną w tooltipie i wprowadź zmieniony plik.');
    }
  }

  Submit() {
    this.data.factors = this.data.factors.map(factor => CalculationsService.parseInput(factor));
    this.data.sections = CalculationsService.parseInput(this.data.sections);
    this.data.Xp = CalculationsService.parseInput(this.data.Xp);
    this.data.Xk = CalculationsService.parseInput(this.data.Xk);

    const data: IntegrationData = {
      dataType: INTEGRATION_DATA,
      username: UserService.getUsername(),
      degree: this.data.degree,
      factors: this.data.factors,
      sections: this.data.sections,
      Xp: this.data.Xp,
      Xk: this.data.Xk,
      isTest: false,
      customFunction: this.data.customFunction
    };
    this.http.post<IntegrationResult>(JAVA_URL+this.integralName+'_integration', data).subscribe(
      (result: IntegrationResult) => {
        ResultDataService.SetIntegrationResult(result, data);
        ResultDataService.SetResultType(INTEGRATION);
        this.router.navigate(['/result']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
    this.dialogRef.close();
  }

}
