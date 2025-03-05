import { Component, Inject } from '@angular/core';
import { BaseData, IntegrationData, IntegrationRecord, IntegrationResult, InterpolationData, InterpolationRecord, InterpolationResult, SystemOfEquationsData, SystemOfEquationsRecord, SystemOfEquationsResult } from 'src/app/shared/data/data.interface';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { INTEGRATION, INTEGRATION_DATA, INTERPOLATION, INTERPOLATION_DATA, JAVA_URL, SYSTEM_OF_EQUATIONS, SYSTEM_OF_EQUATIONS_DATA } from 'src/app/shared/data/data.constants';

@Component({
  selector: 'app-results-history',
  templateUrl: './results-history.component.html',
  styleUrls: ['./results-history.component.css']
})
export class ResultsHistoryComponent {

  records: BaseData[] = [];

  constructor(private http: HttpClient, private router: Router) {
    this.records = ResultDataService.GetBaseData();
  }

  ngOnInit(): void {
  }

  isInterpolation(obj: any): obj is InterpolationRecord{
    return obj.dataType == INTERPOLATION_DATA;
  }

  isIntegration(obj: any): obj is IntegrationData {
    return obj.dataType == INTEGRATION_DATA;
  }

  isSystemOfEquations(obj: any): obj is SystemOfEquationsData {
    return obj.dataType == SYSTEM_OF_EQUATIONS_DATA;
  }

  recalculate(record: any) {
    if(record.dataType == INTERPOLATION_DATA) {
      var tmp = record as InterpolationRecord
      var data: InterpolationData = {
        dataType: '',
        pointsNumber: tmp.pointsNumber,
        points: tmp.points,
        searchedValue: tmp.searchedValue,
        isTest: false,
        username: tmp.username
      }
      this.http.post<InterpolationResult>(JAVA_URL+'polynomial_interpolation',data).subscribe(
        (result: InterpolationResult) => {
          ResultDataService.SetInterpolationResult(result, data);
          ResultDataService.SetResultType(INTERPOLATION);
          this.router.navigate(['/result']);
        },
        (error: any) => {
          console.error("Error:", error);
        }
      );
    }
    else if(record.dataType == INTEGRATION_DATA) {
      var tmp2 = record as IntegrationRecord
      var data2: IntegrationData = {
        dataType: '',
        degree: tmp2.degree,
        factors: tmp2.factors,
        customFunction: tmp2.customFunction,
        sections: tmp2.sections,
        Xp: tmp2.Xp,
        Xk: tmp2.Xk,
        isTest: false,
        username: tmp2.username
      }
      this.http.post<IntegrationResult>(JAVA_URL+'midpoint_integration', data2).subscribe(
        (result: IntegrationResult) => {
          ResultDataService.SetIntegrationResult(result, data2);
          ResultDataService.SetResultType(INTEGRATION);
          this.router.navigate(['/result']);
        },
        (error: any) => {
          console.error("Error:", error);
        }
      );
    }
    else if(record.dataType == SYSTEM_OF_EQUATIONS_DATA) {
      var tmp3 = record as SystemOfEquationsRecord
      var data3: SystemOfEquationsData = {
        dataType: '',
        username: tmp3.username,
        coefficients: tmp3.coefficients,
        constants: tmp3.constants,
        isTest: false
      }
      this.http.post<SystemOfEquationsResult>(JAVA_URL+'cramer_system_of_equations',data3).subscribe(
        (result: SystemOfEquationsResult) => {
          ResultDataService.SetSystemOfEquationsResult(result, data3);
          ResultDataService.SetResultType(SYSTEM_OF_EQUATIONS);
          this.router.navigate(['/result']);
        },
        (error: any) => {
          console.error("Error:", error);
        }
      );
    }
  }

  exportRecord(record: any, type: string): void {
    let content = '';

    if (type === INTERPOLATION) {
      content = `Interpolacja:\nLiczba punktów: ${record.pointsNumber}\nPoszukiwana wartość: ${record.searchedValue}\nPunkty:\n`;
      record.points.forEach((point: any, index: number) => {
        content += `  Punkt ${index + 1}: X = ${point.x}, Y = ${point.y}\n`;
      });
      content += `  Obliczony wynik: ${record.result}\n`;
      content += `  Wzór funkcji: f(x) = `;
      var length = record.coefficients.length;
      for(var i = 0; i < length; i++) {
        if(record.coefficients[length - i - 1] != 0) {
          if(i = length - 1) {
            content += `${record.coefficients[length - i - 1]}`;
          }
          else {
            content += `${record.coefficients[length - i - 1]}x^${length - i - 1} + `;
          }
        }
      }
      content += `\n\n`;
      if(record.customFunction != '')
        content += record.explanation;
    } else if (type === INTEGRATION) {
      if(record.customFunction != '')
        content = `Wzór funkcji: ${record.customFunction}\n`;
      else
        content = `Całkowanie:\nStopień wielomianu: ${record.degree}\nWspółczynniki: ${record.factors.join(', ')}\n`;
      content += `Liczba przedziałów: ${record.sections}\nPunkt początkowy: ${record.Xp}\nPunkt końcowy: ${record.Xk}\n\n${record.explanation}`;
    } else if (type === SYSTEM_OF_EQUATIONS) {
      content = `Układ równań:\n`;
      record.coefficients.forEach((row: number[], index: number) => {
        content += row.map((coef, i) => `${coef}x${i + 1}`).join(' + ') + ` = ${record.constants[index]}\n`;
      });
      if(record.customFunction != '')
        content += `\n${record.explanation}`;
    }

    const blob = new Blob([content], { type: 'text/plain' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${type}-record.txt`;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
