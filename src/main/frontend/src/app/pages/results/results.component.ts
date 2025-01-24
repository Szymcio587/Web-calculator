import { ChangeDetectorRef, Component } from '@angular/core';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { IntegrationData, InterpolationData, InterpolationResult, SystemOfEquationsData } from 'src/app/shared/data/data.interface';
import { EChartsOption } from 'echarts';
import { MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { InterpolationOptionsDialogComponent } from '../home/dialog/interpolation-options-dialog/interpolation-options-dialog.component';
import { IntegrationOptionsDialogComponent } from '../home/dialog/integration-options-dialog/integration-options-dialog.component';
import { INTEGRATION, INTERPOLATION, JAVA_URL, SYSTEM_OF_EQUATIONS } from 'src/app/shared/data/data.constants';
import { MathjaxService } from 'src/app/shared/services/mathjax/mathjax.service';
import { CalculationsService } from 'src/app/shared/services/calculations/calculations.service';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent {

  type: String;
  result!: number;
  newResult?: number;
  chartOption!: EChartsOption;
  newChartOption!: EChartsOption;
  interpolationData!: InterpolationData;
  interpolationResult!: InterpolationResult;
  integrationData!: IntegrationData;
  systemOfEquationsData!: SystemOfEquationsData;
  polynomialString: string = '';
  xCoordinate: number | null = null;
  yCoordinate: number | null = null;

  constructor(private dialog: MatDialog, private http: HttpClient, private mathjaxService: MathjaxService, private cdr: ChangeDetectorRef) {
    this.type = ResultDataService.GetResultType();

    if (this.type === INTERPOLATION) {
      this.initializeInterpolationChart();
    } else if (this.type === INTEGRATION) {
      this.initializeIntegrationChart();
    } else if (this.type === SYSTEM_OF_EQUATIONS) {
      this.initializeSystemOfEquations();
    }
  }

  private initializeInterpolationChart(): void {
    this.interpolationResult = ResultDataService.GetInterpolationResult();
    this.interpolationData = ResultDataService.GetInterpolationData();
    const X = this.interpolationData.points.map((point) => point.x);
    const Y = this.interpolationData.points.map((point) => point.y);
    const scatterData = Y.map((y, index) => [X[index], y]);
    scatterData.push([this.interpolationData.searchedValue, this.interpolationResult.result]);
    const lineData = Y.map((y, index) => [X[index], y]);
    lineData.sort((a, b) => a[0] - b[0]);

    this.chartOption = {
      tooltip: {
        trigger: 'item',
        formatter: (params: any) => `x: ${params.data[0]}, y: ${params.data[1]}`
      },
      grid: {
        top: 40,
        left: 50,
        right: 40,
        bottom: 50
      },
      xAxis: {
        name: 'x',
        minorTick: {
          show: true
        },
        minorSplitLine: {
          show: true
        }
      },
      yAxis: {
        name: 'y',
        minorTick: {
          show: true
        },
        minorSplitLine: {
          show: true
        }
      },
      dataZoom: [
        {
          show: true,
          type: 'inside',
          filterMode: 'none',
          xAxisIndex: [0],
          startValue: -20,
          endValue: 20
        },
        {
          show: true,
          type: 'inside',
          filterMode: 'none',
          yAxisIndex: [0],
          startValue: -20,
          endValue: 20
        }
      ],
      series: [
        {
          data: scatterData,
          type: 'scatter',
          symbolSize: 10,
          itemStyle: {
            color: 'blue'
          }
        },
        {
          data: lineData,
          type: 'line',
          smooth: true,
          lineStyle: {
            opacity: 0.5
          }
        }
      ]
    };
  }

  ngAfterViewInit() {
    this.generatePolynomialString(this.interpolationData, this.interpolationResult);
  }

  private initializeIntegrationChart(): void {
    this.integrationData = ResultDataService.GetIntegrationData();
    const { xValues, yValues } = this.generatePolynomialData(
      this.integrationData.factors,
      this.integrationData.Xp,
      this.integrationData.Xk,
      this.integrationData.sections
    );
    const lineData = xValues.map((x, i) => [x, yValues[i]]);

    this.chartOption = {
      tooltip: {
        trigger: 'item',
        formatter: (params: any) => `x: ${params.data[0]}, y: ${params.data[1]}`
      },
      xAxis: {
        type: 'value'
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: lineData,
          type: 'scatter',
          symbolSize: 10,
          itemStyle: {
            color: 'blue'
          }
        },
        {
          data: lineData,
          type: 'line',
          smooth: true,
          lineStyle: {
            opacity: 0.5
          }
        }
      ]
    };
  }

  private initializeSystemOfEquations(): void {
    this.systemOfEquationsData = ResultDataService.GetSystemOfEquationsData();
    console.log('System of equations data:', this.systemOfEquationsData);
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

  addPointFromInput(): void {

    if (this.xCoordinate !== null && this.yCoordinate !== null) {
      this.addPointAndRecalculate(CalculationsService.parseInput(this.xCoordinate), CalculationsService.parseInput(this.yCoordinate));
      this.xCoordinate = null;
      this.yCoordinate = null;
    }
  }

  private addPointAndRecalculate(x: number, y: number): void {
    this.interpolationData.points.push({ x, y });
    this.interpolationData.pointsNumber += 1;
    this.recalculateInterpolation();
  }

  private recalculateInterpolation(): void {
    const url = JAVA_URL+`polynomial_interpolation`;
    this.http.post<InterpolationResult>(url, this.interpolationData).subscribe(
      (newResult: InterpolationResult) => {
        ResultDataService.SetInterpolationResult(newResult, this.interpolationData);
        this.initializeInterpolationChart();
        this.generatePolynomialString(this.interpolationData, newResult);
      },
      (error: any) => {
        console.error('Error recalculating interpolation:', error);
      }
    );
  }

  generatePolynomialString(data: InterpolationData, result: InterpolationResult): void {
    let terms = [];
    for (let i = 0; i <= data.pointsNumber - 1; i++) {
      const exponent = data.pointsNumber - i - 1;
      const coefficient = result.coefficients[data.pointsNumber - i - 1];
      if (coefficient != 0) {
        const term = exponent > 0 ? `${coefficient}x^{${exponent}}` : `${coefficient}`;
        terms.push(term);
      }
    }
    const polynomial = `f(x) = ${terms.join(' + ')}`;
    this.polynomialString = `$$ ${polynomial} $$`;

    this.cdr.detectChanges();
    this.mathjaxService.renderMath();
  }

  setNewValues(result: number): void {
    if (this.type === INTERPOLATION) {
      this.newResult = result;
      const updatedX = this.interpolationData.points.map((point) => point.x);
      const updatedY = this.interpolationData.points.map((point) => point.y);
      const lineData = updatedY.map((y, index) => [updatedX[index], y]);
      const scatterData = updatedY.map((y, index) => [updatedX[index], y]);
      scatterData.push([this.interpolationData.searchedValue, result]);

      this.newChartOption = {
        ...this.chartOption,
        series: [
          {
            data: lineData,
            type: 'scatter',
            symbolSize: 10,
            itemStyle: {
              color: 'blue'
            }
          },
          {
            data: lineData,
            type: 'line',
            smooth: true,
            lineStyle: {
              opacity: 0.5
            }
          }
        ]
      };
    } else if (this.type === INTEGRATION) {
      this.newResult = result;
      const { xValues, yValues } = this.generatePolynomialData(
        this.integrationData.factors,
        this.integrationData.Xp,
        this.integrationData.Xk,
        this.integrationData.sections
      );
      const lineData = xValues.map((x, i) => [x, yValues[i]]);

      this.newChartOption = {
        ...this.chartOption,
        series: [
          {
            data: lineData,
            type: 'scatter',
            symbolSize: 10,
            itemStyle: {
              color: 'blue'
            }
          },
          {
            data: lineData,
            type: 'line',
            smooth: true,
            lineStyle: {
              opacity: 0.5
            }
          }
        ]
      };
    }
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
    const interpolationEndpoint = type === 'polynomial' ? 'polynomial_interpolation' : 'trigonometric_interpolation';
    const url = JAVA_URL+`${interpolationEndpoint}`;
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
    const integrationEndpoint = type === 'midpoint' ? 'midpoint_integration' : type === 'simpson' ? 'simpson_integration' : 'trapezoidal_integration';
    const url = JAVA_URL+`${integrationEndpoint}`;
    this.http.post<number>(url, this.integrationData).subscribe(
      (newResult: number) => {
        this.setNewValues(newResult);
      },
      (error: any) => {
        console.error('Error fetching integration:', error);
      }
    );
  }
}
