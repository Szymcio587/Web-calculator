import { Component, OnInit } from '@angular/core';
import { ResultDataService } from 'src/app/shared/result-data.service';
import { InterpolationData } from 'src/app/shared/data.interface';
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
  data!: InterpolationData;
  X: number[];
  Y: number[];

  chartOption: EChartsOption;

  constructor(private resultDataService: ResultDataService) {
    this.result = this.resultDataService.GetInterpolationResult();
    this.data = this.resultDataService.GetInterpolationData();
    this.X = this.data.points.map((point) => point.x);
    this.Y = this.data.points.map((point) => point.y);
    this.X.push(this.data.searchedValue);
    this.Y.push(this.result);

    const minX = Math.min(...this.X);
    const maxX = Math.max(...this.X);

    const numberOfLabels = 5;
    const xStep = (maxX - minX) / (numberOfLabels - 1);
    const xAxisLabels = Array.from({ length: numberOfLabels }, (_, i) => (minX + i * xStep).toFixed(2));

    const lineData = this.Y.slice(0, this.Y.length - 1).map((y, index) => [this.X[index], y]);

    this.chartOption = {
      xAxis: {
        type: 'value',
        axisLabel: {
          formatter: (value: number) => xAxisLabels.find((label) => parseFloat(label) === value) || '',
        },
      },
      yAxis: {
        type: 'value',
      },
      series: [
        {
          data: this.Y.map((y, index) => [this.X[index], y]),
          type: 'scatter',
          symbolSize: 10,
          itemStyle: {
            color: 'blue',
          },
        },
        {
          data: lineData,
          type: 'line',
          smooth: true,
          lineStyle: {
            opacity: 0.5,
          },
          emphasis: {
            lineStyle: {
              opacity: 1,
            },
          },
        },
      ],
    };
  }
}
