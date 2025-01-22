import { ChangeDetectorRef, Component } from '@angular/core';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { IntegrationData, InterpolationData, InterpolationResult, SystemOfEquationsData } from 'src/app/shared/data/data.interface';
import type { LineSeriesOption, SeriesOption, EChartsOption } from 'echarts';
import {MatDialog} from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { InterpolationOptionsDialogComponent } from '../home/dialog/interpolation-options-dialog/interpolation-options-dialog.component';
import { IntegrationOptionsDialogComponent } from '../home/dialog/integration-options-dialog/integration-options-dialog.component';
import { INTEGRATION, INTERPOLATION, SYSTEM_OF_EQUATIONS } from 'src/app/shared/data/data.constants';
import { MathjaxService } from 'src/app/shared/services/mathjax/mathjax.service';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent {

  result!: number;
  interpolationData!: InterpolationData;
  interpolationResult!: InterpolationResult;
  integrationData!: IntegrationData;
  systemOfEquationsData!: SystemOfEquationsData;
  X!: number[];
  Y!: number[];
  type: String;
  chartOption!: EChartsOption;
  polynomialString: string = '';

  newResult?: number;
  newInterpolationData!: InterpolationData;
  newIntegrationData!: IntegrationData;
  newSystemOfEquationsData!: SystemOfEquationsData;
  newX!: number[];
  newY!: number[];
  newChartOption!: EChartsOption;

  constructor(private dialog: MatDialog, private http: HttpClient, private mathjaxService: MathjaxService, private cdr: ChangeDetectorRef) {
    console.log(ResultDataService.GetResult());
    console.log(ResultDataService.GetResultType());
    this.type = ResultDataService.GetResultType();
    if(this.type == INTERPOLATION) {
      this.interpolationResult = ResultDataService.GetInterpolationResult();
      this.interpolationData = ResultDataService.GetInterpolationData();
      this.X = this.interpolationData.points.map((point) => point.x);
      this.Y = this.interpolationData.points.map((point) => point.y);
      this.X.push(this.interpolationData.searchedValue);
      this.Y.push(this.interpolationResult.result);
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
    else if(this.type == INTEGRATION) {
      this.result = ResultDataService.GetResult();
      this.integrationData = ResultDataService.GetIntegrationData();
      const { xValues, yValues } = this.generatePolynomialData(this.integrationData.factors, this.integrationData.Xp,
        this.integrationData.Xk, this.integrationData.sections);
      const minX = Math.min(...xValues);
      const maxX = Math.max(...xValues);
      const numberOfLabels = 5;
      const xStep = (maxX - minX) / (numberOfLabels - 1);
      const xAxisLabels = Array.from({ length: numberOfLabels }, (_, i) => (minX + i * xStep).toFixed(2));
      const lineData = yValues.slice(0, yValues.length).map((y, index) => [xValues[index], y]);

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
    else if(this.type == SYSTEM_OF_EQUATIONS) {
      this.result = ResultDataService.GetResult();
      console.log(this.result);
      this.systemOfEquationsData = ResultDataService.GetSystemOfEquationsData();
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

  ngAfterViewInit() {
    this.generatePolynomialString(this.interpolationData, this.interpolationResult);
  }

  generatePolynomialString(data: InterpolationData, result: InterpolationResult): void {
    let terms = [];
    for (let i = 0; i <= data.pointsNumber - 1; i++) {
      const exponent = data.pointsNumber - i - 1;
      const coefficient = result.coefficients[data.pointsNumber - i - 1];
      if(coefficient != 0) {
        const term = exponent > 0 ? `${coefficient}x^{${exponent}}` : `${coefficient}`;
        terms.push(term);
      }
    }
    const polynomial = `f(x) = ${terms.join(' + ')}`;
    this.polynomialString = `$$ ${polynomial} $$`;

    this.cdr.detectChanges();
    this.mathjaxService.renderMath();
  }

  openInterpolationOptions(): void {
    const dialogRef = this.dialog.open(InterpolationOptionsDialogComponent);

    dialogRef.afterClosed().subscribe((result: string) => {
      if (result) {
        this.callInterpolation(result);
      }
    });
  }

  openIntegrationOptions(): void {
    const dialogRef = this.dialog.open(IntegrationOptionsDialogComponent);

    dialogRef.afterClosed().subscribe((result: string) => {
      if (result) {
        this.callIntegration(result);
      }
    });
  }

  private callInterpolation(type: string): void {
    const interpolationEndpoint = type === 'polynomial' ? '/polynomial_interpolation' : '/trigonometric_interpolation';
    const url = `http://localhost:8081/calculations${interpolationEndpoint}`;
    this.http.post<number>(url, this.interpolationData).subscribe(
      (newResult: number) => {
        this.setNewValues(newResult);
      },
      (error: any) => {
        console.error('Error fetching interpolation:', error);
      }
    );
  }

  private callIntegration(type: string): void {
    const integrationEndpoint = type === 'midpoint' ? '/midpoint_integration' : type === 'simpson' ? '/simpson_integration' : '/trapezoidal_integration';
    const url = `http://localhost:8081/calculations${integrationEndpoint}`;
    this.http.post<number>(url, this.integrationData).subscribe(
      (newResult: number) => {
        this.setNewValues(newResult);
      },
      (error: any) => {
        console.error('Error fetching interpolation:', error);
      }
    );
  }

  private setNewValues(result: number) {
    if(this.type == INTERPOLATION) {
      this.newResult = result;
      this.newInterpolationData = ResultDataService.GetInterpolationData();

      this.newX = this.newInterpolationData.points.map((point) => point.x);
      this.newY = this.newInterpolationData.points.map((point) => point.y);
      this.newX.push(this.newInterpolationData.searchedValue);
      this.newY.push(this.newResult);

      const minX = Math.min(...this.X);
      const maxX = Math.max(...this.X);

      const numberOfLabels = 5;
      const xStep = (maxX - minX) / (numberOfLabels - 1);
      const xAxisLabels = Array.from({ length: numberOfLabels }, (_, i) => (minX + i * xStep).toFixed(2));

      const lineData = this.newY.slice(0, this.newY.length - 1).map((y, index) => [this.newX[index], y]);

      this.newChartOption = {
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
            data: this.newY.map((y, index) => [this.newX[index], y]),
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
    else if(this.type == INTEGRATION) {
      this.newResult = result;
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

      this.newChartOption = {
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

}
