import { Component, Inject } from '@angular/core';
import { BaseData, IntegrationData, InterpolationData } from 'src/app/shared/data/data.interface';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

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
    return obj.dataType == "InterpolationData";
  }

  isIntegration(obj: any): obj is IntegrationData {
    return obj.dataType == "IntegrationData";
  }

  recalculate(record: any) {
    if(record.dataType == "IntegralData") {
      this.http.post<number>('http://localhost:8081/calculations/midpoint_integration', record).subscribe(
        (result: number) => {
          ResultDataService.SetIntegrationResult(result, record);
          ResultDataService.SetResultType('Integral');
          this.router.navigate(['/result']);
        },
        (error: any) => {
          console.error("Error:", error);
        }
      );
    }
    else if(record.dataType == "InterpolationData") {
      this.http.post<number>('http://localhost:8081/calculations/polynomial_interpolation',record).subscribe(
        (result: number) => {
          ResultDataService.SetInterpolationResult(result, record);
          ResultDataService.SetResultType('Interpolation');
          this.router.navigate(['/result']);
        },
        (error: any) => {
          console.error("Error:", error);
        }
      );
    }
  }
}
