import { Component, OnInit } from '@angular/core';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { IntegrationData, InterpolationData, Response } from 'src/app/shared/data/data.interface';
import type { LineSeriesOption, SeriesOption, EChartsOption } from 'echarts';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent {

  result!: number;
  interpolationData!: InterpolationData;
  integrationData!: IntegrationData;
  X!: number[];
  Y!: number[];
  type: string;

  chartOption!: EChartsOption;

  constructor() {
    console.log(ResultDataService.GetResult());
    console.log(ResultDataService.GetResultType());
    console.log(ResultDataService.GetIntegrationData());
    this.type = ResultDataService.GetResultType();
    if(this.type == 'Interpolation') {
      this.result = ResultDataService.GetResult();
      this.interpolationData = ResultDataService.GetInterpolationData();

      this.X = this.interpolationData.points.map((point) => point.x);
      this.Y = this.interpolationData.points.map((point) => point.y);
      this.X.push(this.interpolationData.searchedValue);
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
    else if(this.type == 'Integral') {
      this.result = ResultDataService.GetResult();
      this.integrationData = ResultDataService.GetIntegrationData();
      const { xValues, yValues } = this.generatePolynomialData(this.integrationData.factors, this.integrationData.Xp,
        this.integrationData.Xk, this.integrationData.sections);
        console.log(xValues);
        console.log(yValues);
      const minX = Math.min(...xValues);
      const maxX = Math.max(...xValues);
      const numberOfLabels = 5;
      const xStep = (maxX - minX) / (numberOfLabels - 1);
      const xAxisLabels = Array.from({ length: numberOfLabels }, (_, i) => (minX + i * xStep).toFixed(2));
      console.log(xAxisLabels);
      const lineData = yValues.slice(0, yValues.length).map((y, index) => [xValues[index], y]);
      console.log(lineData);

      const verticalLines: LineSeriesOption[] = lineData.map(([x, y]) => ({
        data: [
          [x, y],
          [x, 0],
        ],
        type: 'line',
        lineStyle: {
          type: 'dashed',
          opacity: 0.5,
        },
        silent: true,
      }));

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
            data: yValues.map((y, index) => [xValues[index], y]),
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
          ...verticalLines,
        ] as SeriesOption[],
      };
    }
  }

  generatePolynomialData(coefficients: number[], start: number, end: number, parts: number): { xValues: number[], yValues: number[] } {
    const xValues: number[] = [];
    const yValues: number[] = [];
    const step = (end - start) / parts;

    for (let x = start; x <= end; x += step) {
      const y = coefficients.reduce((sum, coef, index) => sum + coef * Math.pow(x, coefficients.length - 1 - index), 0);
      xValues.push(x);
      yValues.push(y);
    }

    return { xValues, yValues };
  }

}
