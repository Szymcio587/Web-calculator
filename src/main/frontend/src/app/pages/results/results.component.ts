import { ChangeDetectorRef, Component } from '@angular/core';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { IntegrationData, IntegrationResult, InterpolationData, InterpolationResult, SystemOfEquationsData, SystemOfEquationsResult } from 'src/app/shared/data/data.interface';
import { EChartsOption, LineSeriesOption, SeriesOption } from 'echarts';
import { MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { InterpolationOptionsDialogComponent } from '../home/dialog/interpolation-options-dialog/interpolation-options-dialog.component';
import { IntegrationOptionsDialogComponent } from '../home/dialog/integration-options-dialog/integration-options-dialog.component';
import { INTEGRATION, INTERPOLATION, JAVA_URL, SYSTEM_OF_EQUATIONS } from 'src/app/shared/data/data.constants';
import { MathjaxService } from 'src/app/shared/services/mathjax/mathjax.service';
import { CalculationsService } from 'src/app/shared/services/calculations/calculations.service';
import { evaluate } from 'mathjs';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent {

  type: String;
  chartOption!: EChartsOption;
  interpolationData!: InterpolationData;
  interpolationResult!: InterpolationResult;
  integrationData!: IntegrationData;
  integrationResult!: IntegrationResult;
  systemOfEquationsData!: SystemOfEquationsData;
  systemOfEquationsResult!: SystemOfEquationsResult;
  polynomialString: string = '';
  systemOfEquationsString: string = '';
  xCoordinate: number | null = null;
  yCoordinate: number | null = null;

  constructor(private dialog: MatDialog, private http: HttpClient, private mathjaxService: MathjaxService, private cdr: ChangeDetectorRef) {
    this.type = ResultDataService.GetResultType();

    console.log(this.type);
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

  private initializeIntegrationChart(): void {
    this.integrationData = ResultDataService.GetIntegrationData();
    this.integrationResult = ResultDataService.GetIntegrationResult();

    let xValues: number[] = [];
    let yValues: number[] = [];

    if (this.integrationData.customFunction) {
      ({ xValues, yValues } = this.generateFunctionData(this.integrationData.customFunction, this.integrationData.Xp, this.integrationData.Xk, this.integrationData.sections));
    } else {
      ({ xValues, yValues } = this.generateIntegrationData(this.integrationData.factors, this.integrationData.Xp, this.integrationData.Xk, this.integrationData.sections));
    }

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
          data: lineData,
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
      ]
    };
  }

  private initializeSystemOfEquations(): void {
    console.log(ResultDataService.GetSystemOfEquationsData());
    console.log(ResultDataService.GetSystemOfEquationsResult());
    this.systemOfEquationsData = ResultDataService.GetSystemOfEquationsData();
    this.systemOfEquationsResult = ResultDataService.GetSystemOfEquationsResult();
  }

  ngAfterViewInit() {
    if (this.type === INTERPOLATION) {
      this.generateInterpolationString(this.interpolationData, this.interpolationResult);
    } else if (this.type === INTEGRATION) {
      this.mathjaxService.renderMath();
    } else if (this.type === SYSTEM_OF_EQUATIONS) {
      this.generateSystemOfEquationsString(this.systemOfEquationsData);
    }
  }

  generateFunctionData(customFunction: String, start: number, end: number, parts: number): { xValues: number[], yValues: number[] } {
    const xValues: number[] = [];
    const yValues: number[] = [];
    const step = (end - start) / parts;
    var y = 0;
    for (let x = start; x <= end + 0.00001; x += step) {
        try {
            y = evaluate(customFunction.replace('ln', 'log').valueOf(), {x});
            xValues.push(x);
            yValues.push(y);
        } catch (error) {
            console.error(`Error evaluating function at x = ${x}: ${error}`);
            xValues.push(x);
            yValues.push(0);
        }
    }

    return { xValues, yValues };
}

  generateIntegrationData(factors: number[], start: number, end: number, parts: number): { xValues: number[], yValues: number[] } {
    const xValues: number[] = [];
    const yValues: number[] = [];
    const step = (end - start) / parts;
    const reversedFactors = [...factors].reverse();

    for (let x = start; x <= end; x += step) {
      const y = reversedFactors.reduce((sum, coef, index) => sum + coef * Math.pow(x, factors.length - 1 - index), 0);
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
        this.generateInterpolationString(this.interpolationData, newResult);
      },
      (error: any) => {
        console.error('Error recalculating interpolation:', error);
      }
    );
  }

  generateInterpolationString(data: InterpolationData, result: InterpolationResult): void {
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

  generateSystemOfEquationsString(data: SystemOfEquationsData): void {
    let latexString = '\\begin{cases}';

    data.coefficients.forEach((row: number[], rowIndex: number) => {
      let equation = "";
      row.forEach((coefficient: number, colIndex: number) => {
        equation += `${coefficient}x_{${colIndex + 1}}`;
        if (colIndex < row.length - 1) {
          equation += " + ";
        }
      });
      equation += ` = ${data.constants[rowIndex]}`;
      latexString += equation;
      if (rowIndex < data.coefficients.length - 1) {
        latexString += " \\\\ ";
      }
    });

    latexString += "\\end{cases}";
    this.systemOfEquationsString = latexString;

    this.cdr.detectChanges();
    this.mathjaxService.renderMath();
  }

}
