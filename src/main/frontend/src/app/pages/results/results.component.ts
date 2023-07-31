import { Component, OnInit } from '@angular/core';
import { ResultDataService } from 'src/app/shared/result-data.service';
import { Data } from 'src/app/shared/data.interface';
import { ChartType } from 'chart.js';
import { Point } from 'src/app/shared/data.interface';
import { EChartsOption } from 'echarts';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent {

  result: number = 0;
  data!: Data;

  chartOption: EChartsOption;

  constructor(private resultDataService: ResultDataService) {
    this.result = this.resultDataService.GetResult();
    this.data = this.resultDataService.GetData();
    this.chartOption = {
      xAxis: {
        type: 'category',
        data: this.data.points.map((point) => point.x),
      },
      yAxis: {
        type: 'value',
      },
      series: {
          data: this.data.points.map((point) => point.y),
          type: 'line',
      },
    };
  }
}
