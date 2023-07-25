import { Component, OnInit } from '@angular/core';
import { ResultDataService } from 'src/app/shared/result-data.service';
import { CalculationService } from '../home/calculation.service';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {

  result: number = 0;

  constructor(private calculationService: CalculationService) { }

  ngOnInit(): void {
    this.calculationService.GetCalculationResult()
      .subscribe(
        response => {
          this.result = response.result;
        },
        error => {
          console.error("Error:", error);
        }
      );
  }
}
