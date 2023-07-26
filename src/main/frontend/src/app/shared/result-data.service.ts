import { Injectable } from '@angular/core';
import { Data } from './data.interface';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {

  private static result: number = 0;
  private static data: Data;

  constructor() { }

  SetResult(result: number, data: Data) {
    ResultDataService.result = result;
    ResultDataService.data = data;
    console.log("SetResult: " + ResultDataService.result);
    console.log("SetData: " + ResultDataService.data);
  }

  GetResult(): number {
    console.log("GetResult: " + ResultDataService.result);
    return ResultDataService.result;
  }

  GetData(): Data {
    console.log("GetData: " + ResultDataService.data);
    return ResultDataService.data;
  }

}
