import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {

  private static result: number = 0;

  constructor() { }

  setResult(result: number) {
    ResultDataService.result = result;
  }

  getResult(): number {
    return ResultDataService.result;
  }

}
