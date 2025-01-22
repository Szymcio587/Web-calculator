import { Injectable } from '@angular/core';
import { BaseData, IntegrationData, InterpolationData, InterpolationResult, SystemOfEquationsData } from '../../data/data.interface';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {


  private static result: any;
  private static resultType: String;
  private static interpolationData: InterpolationData;
  private static interpolationResult: InterpolationResult;
  private static integrationData: IntegrationData;
  private static systemOfEquationsData: SystemOfEquationsData;
  private static baseDataSubject: BehaviorSubject<BaseData[]> = new BehaviorSubject<BaseData[]>([]);
  public static baseData$: Observable<BaseData[]> = ResultDataService.baseDataSubject.asObservable();

  constructor() { }

  public static SetInterpolationResult(result: InterpolationResult, data: InterpolationData): void {
    ResultDataService.interpolationResult = result;
    ResultDataService.interpolationData = data;
  }

  public static SetIntegrationResult(result: number, data: IntegrationData): void {
    ResultDataService.result = result;
    ResultDataService.integrationData = data;
  }

  public static SetSystemOfEquationsResult(result: any, data: SystemOfEquationsData): void {
    ResultDataService.result = result;
    ResultDataService.systemOfEquationsData = data;
  }

  public static GetResult(): number {
    return ResultDataService.result;
  }

  public static GetInterpolationResult(): InterpolationResult {
    return ResultDataService.interpolationResult;
  }

  public static GetInterpolationData(): InterpolationData {
    return ResultDataService.interpolationData;
  }

  public static GetIntegrationData(): IntegrationData {
    return ResultDataService.integrationData;
  }

  public static GetSystemOfEquationsData(): SystemOfEquationsData {
    return ResultDataService.systemOfEquationsData;
  }

  public static GetResultType() : String {
    return this.resultType;
  }

  public static SetResultType(resultType: String) {
    ResultDataService.resultType = resultType;
  }

  public static SetBaseData(data: BaseData[]): void {
    ResultDataService.baseDataSubject.next(data);
  }

  public static GetBaseData(): BaseData[] {
    return ResultDataService.baseDataSubject.value;
  }

}
