import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Data } from './dialog/calculation.interface';

@Injectable({
  providedIn: 'root'
})
export class CalculationService {
  constructor(private http: HttpClient) {}

  calculateSumAndProcess(data: Data) {
    return this.http.post<number>('/api/calculate-sum-and-process', data);
  }
}
