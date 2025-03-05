import { Injectable } from '@angular/core';
import { BaseData, IntegrationData, IntegrationResult, InterpolationData, InterpolationResult, SystemOfEquationsData, SystemOfEquationsResult } from '../../data/data.interface';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {


  private static resultType: String;
  private static interpolationData: InterpolationData;
  private static interpolationResult: InterpolationResult;
  private static integrationData: IntegrationData;
  private static integrationResult: IntegrationResult;
  private static systemOfEquationsData: SystemOfEquationsData;
  private static systemofEquationsResult: SystemOfEquationsResult;
  private static baseDataSubject: BehaviorSubject<BaseData[]> = new BehaviorSubject<BaseData[]>([]);
  public static baseData$: Observable<BaseData[]> = ResultDataService.baseDataSubject.asObservable();

  constructor() { }

  public static SetInterpolationResult(result: InterpolationResult, data: InterpolationData): void {
    ResultDataService.interpolationResult = result;
    ResultDataService.interpolationData = data;
  }

  public static SetIntegrationResult(result: IntegrationResult, data: IntegrationData): void {
    ResultDataService.integrationResult = result;
    ResultDataService.integrationData = data;
  }

  public static SetSystemOfEquationsResult(result: SystemOfEquationsResult, data: SystemOfEquationsData): void {
    ResultDataService.systemofEquationsResult = result;
    ResultDataService.systemOfEquationsData = data;
  }

  public static SetResultType(resultType: String) {
    ResultDataService.resultType = resultType;
  }

  public static SetBaseData(data: BaseData[]): void {
    ResultDataService.baseDataSubject.next(data);
  }

  public static GetInterpolationResult(): InterpolationResult {
    return ResultDataService.interpolationResult;
  }

  public static GetInterpolationData(): InterpolationData {
    return ResultDataService.interpolationData;
  }

  public static GetIntegrationResult(): IntegrationResult {
    return ResultDataService.integrationResult;
  }

  public static GetIntegrationData(): IntegrationData {
    return ResultDataService.integrationData;
  }

  public static GetSystemOfEquationsData(): SystemOfEquationsData {
    return ResultDataService.systemOfEquationsData;
  }

  public static GetSystemOfEquationsResult(): SystemOfEquationsResult {
    return ResultDataService.systemofEquationsResult;
  }

  public static GetResultType() : String {
    return this.resultType;
  }

  public static GetBaseData(): BaseData[] {
    return ResultDataService.baseDataSubject.value;
  }

}
