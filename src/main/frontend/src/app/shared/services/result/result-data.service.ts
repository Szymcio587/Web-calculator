import { Injectable } from '@angular/core';
import { IntegrationData, InterpolationData } from '../../data/data.interface';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {


  private static result: any;
  private static resultType: string;
  private static interpolationData: InterpolationData;
  private static integrationData: IntegrationData;

  constructor() { }

  public static SetInterpolationResult(result: number, data: InterpolationData): void {
    ResultDataService.result = result;
    ResultDataService.interpolationData = data;
  }

  public static SetIntegrationResult(result: number, data: IntegrationData): void {
    ResultDataService.result = result;
    ResultDataService.integrationData = data;
  }

  public static SetResult(result: number) {
    ResultDataService.result = result;
  }

  public static GetResult(): number {
    return ResultDataService.result;
  }

  public static GetInterpolationData(): InterpolationData {
    return ResultDataService.interpolationData;
  }

  public static GetIntegrationData(): IntegrationData {
    return ResultDataService.integrationData;
  }

  public static SetInterpolationData(data: InterpolationData): void {
    ResultDataService.interpolationData = data;
  }

  public static GetResultType() : string {
    return this.resultType;
  }

  public static SetResultType(resultType: string) {
    ResultDataService.resultType = resultType;
  }

}
