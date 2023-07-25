import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Data } from './dialog/calculation.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CalculationService {
  private apiUrl = 'http://localhost:8080/result';

  constructor(private http: HttpClient) { }

  GetCalculationResult(): Observable<{ result: number }> {
    return this.http.get<{ result: number }>(this.apiUrl);
  }
}
