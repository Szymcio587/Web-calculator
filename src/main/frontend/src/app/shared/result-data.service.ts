import { Injectable } from '@angular/core';
import { IntegrationData, InterpolationData, Result } from './data.interface';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {

  private static result: any;
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

  public static GetInterpolationResult(): number {
    return ResultDataService.result;
  }

  public static GetInterpolationData(): InterpolationData {
    return ResultDataService.interpolationData;
  }

}
