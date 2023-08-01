import { Injectable } from '@angular/core';
import { IntegralData, InterpolationData } from './data.interface';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {

  private static result: number = 0;
  private static data: InterpolationData;

  constructor() { }

  SetInterpolationResult(result: number, data: InterpolationData) {
    ResultDataService.result = result;
    ResultDataService.data = data;
    console.log("SetResult: " + ResultDataService.result);
    console.log("SetData: " + ResultDataService.data);
  }

  SetIntegralResult(result: number, data: IntegralData) {

  }

  GetInterpolationResult(): number {
    console.log("GetResult: " + ResultDataService.result);
    return ResultDataService.result;
  }

  GetInterpolationData(): InterpolationData {
    console.log("GetData: " + ResultDataService.data);
    return ResultDataService.data;
  }

}
