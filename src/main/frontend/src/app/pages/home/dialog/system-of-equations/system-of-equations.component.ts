import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { JAVA_URL, SYSTEM_OF_EQUATIONS, SYSTEM_OF_EQUATIONS_DATA } from 'src/app/shared/data/data.constants';
import { SystemOfEquationsData, SystemOfEquationsResult } from 'src/app/shared/data/data.interface';
import { CalculationsService } from 'src/app/shared/services/calculations/calculations.service';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { UserService } from 'src/app/shared/services/user/user.service';

@Component({
  selector: 'app-system-of-equations',
  templateUrl: './system-of-equations.component.html',
  styleUrls: ['./system-of-equations.component.css']
})
export class SystemOfEquationsComponent implements OnInit {

  data: SystemOfEquationsData = {
    dataType: '',
    username: '',
    coefficients: [[0, 0], [0, 0]],
    constants: [0, 0],
    isTest: false
  };

  variablesNumber: number;

  systemOfEquationsName = '';

  constructor(private http: HttpClient, private router: Router, public dialogRef: MatDialogRef<SystemOfEquationsComponent>, private cdr: ChangeDetectorRef,
    @Inject(MAT_DIALOG_DATA) public dialogData: { name: string }
  ) {
    this.variablesNumber = 2;
    this.systemOfEquationsName = dialogData.name;
  }

  ngOnInit(): void {
  }

  UpdateVariablesNumber(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const newNumber = +inputElement.value;
    this.variablesNumber = newNumber;

    this.data.coefficients = Array.from({ length: newNumber }, () =>
      Array(newNumber).fill(0)
    );

    this.data.constants = Array(newNumber).fill(0);
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
      const lines = content.split('\n').map((line) => line.trim()).filter(line => line !== '');

      this.variablesNumber = lines.length;
      this.data.coefficients = [];
      this.data.constants = [];

      if (lines.length === 0) {
        throw new Error('The file is empty or incorrectly formatted.');
      }

      lines.forEach((line) => {
        const values = line.split(' ').map((value) => parseFloat(value.trim()));
        if (values.length < 2) {
          throw new Error('Invalid file format, each line must contain at least two numbers.');
        }

        const lastValue = values.pop();
        if (lastValue === undefined || isNaN(lastValue)) {
          throw new Error('Invalid data in the file.');
        }

        this.data.coefficients.push(values);
        this.data.constants.push(lastValue);
      });

      console.log(this.data.coefficients);
      console.log(this.data.constants);

      this.cdr.detectChanges();
    } catch (error) {
      console.error('Error parsing soe.txt file:', error);
      alert('Niepoprawny format pliku. Przeczytaj wskazówki w tooltipie, i wprowadź dane jeszcze raz.');
    }
  }

  Submit() {
    this.data.coefficients = this.data.coefficients.map(coefficients1d => coefficients1d.map(coefficient => CalculationsService.parseInput(coefficient)));
    this.data.constants = this.data.constants.map(constant => CalculationsService.parseInput(constant));

    console.log(this.data.coefficients);
    console.log(this.data.constants);

    const data: SystemOfEquationsData = {
      dataType: SYSTEM_OF_EQUATIONS_DATA,
      username: UserService.getUsername(),
      coefficients: this.data.coefficients,
      constants: this.data.constants,
      isTest: false
    };
    this.http.post<SystemOfEquationsResult>(JAVA_URL+this.systemOfEquationsName+'_system_of_equations', data).subscribe(
      (result: SystemOfEquationsResult) => {
        ResultDataService.SetSystemOfEquationsResult(result, data)
        ResultDataService.SetResultType(SYSTEM_OF_EQUATIONS);
        this.router.navigate(['/result']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
    this.dialogRef.close();
  }
}
