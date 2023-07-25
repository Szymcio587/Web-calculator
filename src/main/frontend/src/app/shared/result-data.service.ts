import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ResultDataService {

  private result: number = 0;

  constructor() { }

  setResult(result: number) {
    this.result = result;
  }

  getResult(): number {
    return this.result;
  }

}
