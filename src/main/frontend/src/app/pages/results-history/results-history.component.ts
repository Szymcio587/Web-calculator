import { Component, OnInit } from '@angular/core';
import { BaseData, IntegrationData, InterpolationData } from 'src/app/shared/data/data.interface';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';

@Component({
  selector: 'app-results-history',
  templateUrl: './results-history.component.html',
  styleUrls: ['./results-history.component.css']
})
export class ResultsHistoryComponent {

  records: BaseData[] = [];

  constructor() {
    console.log(ResultDataService.GetBaseData());
    this.records = ResultDataService.GetBaseData();
  }

  ngOnInit(): void {
  }

  isInterpolation(obj: any) {
    console.log(obj.dataType);
    return obj instanceof InterpolationData;
  }

  isIntegration(obj: any) {
    console.log(obj instanceof IntegrationData);
    return obj instanceof IntegrationData;
  }
}
