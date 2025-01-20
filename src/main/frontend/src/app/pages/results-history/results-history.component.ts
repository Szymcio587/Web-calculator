import { Component, Inject } from '@angular/core';
import { BaseData, IntegrationData, InterpolationData, SystemOfEquationsData, SystemOfEquationsResponse } from 'src/app/shared/data/data.interface';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { INTEGRATION, INTEGRATION_DATA, INTERPOLATION, INTERPOLATION_DATA, SYSTEM_OF_EQUATIONS, SYSTEM_OF_EQUATIONS_DATA } from 'src/app/shared/data/data.constants';

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

  isInterpolation(obj: any): obj is InterpolationData{
    return obj.dataType == INTERPOLATION_DATA;
  }

  isIntegration(obj: any): obj is IntegrationData {
    return obj.dataType == INTEGRATION_DATA;
  }

  isSystemOfEquations(obj: any): obj is SystemOfEquationsData {
    return obj.dataType == SYSTEM_OF_EQUATIONS_DATA;
  }

  recalculate(record: any) {
    if(record.dataType == INTEGRATION_DATA) {
      this.http.post<number>('http://localhost:8081/calculations/midpoint_integration', record).subscribe(
        (result: number) => {
          ResultDataService.SetIntegrationResult(result, record);
          ResultDataService.SetResultType(INTEGRATION);
          this.router.navigate(['/result']);
        },
        (error: any) => {
          console.error("Error:", error);
        }
      );
    }
    else if(record.dataType == INTERPOLATION_DATA) {
      this.http.post<number>('http://localhost:8081/calculations/polynomial_interpolation',record).subscribe(
        (result: number) => {
          ResultDataService.SetInterpolationResult(result, record);
          ResultDataService.SetResultType(INTERPOLATION);
          this.router.navigate(['/result']);
        },
        (error: any) => {
          console.error("Error:", error);
        }
      );
    }
    else if(record.dataType == SYSTEM_OF_EQUATIONS_DATA) {
      this.http.post<SystemOfEquationsResponse>('http://localhost:8081/calculations/system_of_equations',record).subscribe(
        (result: SystemOfEquationsResponse) => {
          ResultDataService.SetSystemOfEquationsResult(result.solutions, record);
          ResultDataService.SetResultType(SYSTEM_OF_EQUATIONS);
          this.router.navigate(['/result']);
        },
        (error: any) => {
          console.error("Error:", error);
        }
      );
    }
  }
}
