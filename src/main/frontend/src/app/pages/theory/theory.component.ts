import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { INTERPOLATION } from 'src/app/shared/data/data.constants';
import { InterpolationData, InterpolationResult, Point } from 'src/app/shared/data/data.interface';
import { MathjaxService } from 'src/app/shared/services/mathjax/mathjax.service';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';

@Component({
  selector: 'app-theory',
  templateUrl: './theory.component.html',
  styleUrls: ['./theory.component.css']
})
export class TheoryComponent {
lagrangePolynomial = `<p> Wielomian Lagrange'a \( P(x) \) stopnia \( n \) można zapisać w postaci: </p>
<div> $$ P(x) = \\sum_{i=0}^{n} y_i \\prod_{\\substack{j=0 \\\\ j \\neq i}}^{n} \\frac{x - x_j}{x_i - x_j} $$ </div>
<p> Gdzie: </p>
<ul>
  <li> \( <i>y_i</i> \) Stanowią wartości funkcji w węzłach</li>
  <li>\( <i>x_i, x_j</i> \) Kolejne węzły</li>
  <li><i>n</i> stanowi liczbę podanych węzłów</li>
  <li>Dla <i>j = i</i> pomijamy obliczenia, gdyż wartość w liczniku ułamka będzie równa 0</li>
</ul>`;

isProcessing: boolean = false;

  ngAfterViewInit() {
    this.mathjaxService.renderMath();
  }

  constructor(private mathjaxService: MathjaxService, private http: HttpClient, private router: Router){}

  async showExample() {
    if (this.isProcessing) {
      return;
    }

    this.isProcessing = true;

    const data = new InterpolationData(2, [new Point(1, 3), new Point(2, 6)], 5, true);

    try {
      const result: InterpolationResult | undefined = await this.http.post<InterpolationResult>(
        'http://localhost:8081/calculations/polynomial_interpolation',
        data
      ).toPromise();

      if(typeof result === undefined) {
        throw new Error('result is undefined');
      }
      console.log(data.isTest);
      ResultDataService.SetInterpolationResult(result as InterpolationResult, data);
      ResultDataService.SetResultType(INTERPOLATION);

      // Navigate to the /result route
      this.router.navigate(['/result']);
    } catch (error) {
      console.error('Error:', error);
    }
  }
}
