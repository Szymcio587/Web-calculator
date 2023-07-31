import { Component, OnInit } from '@angular/core';
import { ResultDataService } from 'src/app/shared/result-data.service';
import { Data } from 'src/app/shared/data.interface';
import { ChartType } from 'chart.js';
import { Point } from 'src/app/shared/data.interface';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {

  result: number = 0;
  data!: Data;

  constructor(private resultDataService: ResultDataService) { }

  ngOnInit(): void {
    this.result = this.resultDataService.GetResult();
    this.data = this.resultDataService.GetData();
  }
}
