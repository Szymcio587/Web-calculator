import { Component, OnInit } from '@angular/core';
import { ResultDataService } from 'src/app/shared/result-data.service';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {

  result: number = 0;

  constructor(private resultDataService: ResultDataService) { }

  ngOnInit(): void {
    this.result = this.resultDataService.GetResult();
  }
}
