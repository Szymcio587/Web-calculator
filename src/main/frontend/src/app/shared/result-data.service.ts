import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {

  private static result: number = 0;

  constructor() { }

  SetResult(result: number) {
    ResultDataService.result = result;
    console.log("SetResult: " + ResultDataService.result);
  }

  GetResult(): number {
    console.log("GetResult: " + ResultDataService.result);
    return ResultDataService.result;
  }

}
