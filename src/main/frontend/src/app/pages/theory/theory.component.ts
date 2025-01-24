import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { INTERPOLATION, JAVA_URL } from 'src/app/shared/data/data.constants';
import { InterpolationData, InterpolationResult, Point } from 'src/app/shared/data/data.interface';
import { MathjaxService } from 'src/app/shared/services/mathjax/mathjax.service';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';

@Component({
  selector: 'app-theory',
  templateUrl: './theory.component.html',
  styleUrls: ['./theory.component.css']
})
export class TheoryComponent {

lagrangePolynomial = `<p> Wielomian Lagrange'a <i>\( P(x) \)</i> stopnia <i>\( n \)</i> można zapisać w postaci: </p>
<div> $$ P(x) = \\sum_{i=0}^{n} y_i \\prod_{\\substack{j=0 \\\\ j \\neq i}}^{n} \\frac{x - x_j}{x_i - x_j} $$ </div>
<p> Gdzie: </p>
<ul>
  <li> \( <i>y_i</i> \) Stanowią wartości funkcji w węzłach</li>
  <li>\( <i>x_i, x_j</i> \) Kolejne węzły</li>
  <li><i>n + 1</i> stanowi liczbę podanych węzłów</li>
  <li>Dla <i>j = i</i> pomijamy obliczenia, gdyż wartość w liczniku ułamka będzie równa 0</li>
</ul>`;

trigonometricInterpolation = `<p> Interpolacja trygonometryczna funkcji może być zapisana za pomocą szeregu Fouriera w postaci: </p>
<div> $$ f(x) = \\frac{a_0}{2} + \\sum_{k=1}^{n} \\left( a_k \\cos(\\frac{2\\pi}{T}nx) + b_k \\sin(\\frac{2\\pi}{T}nx) \\right) $$ </div>
<div> $$ a_k = \\frac{2}{T} \\int_{-T/2}^{T/2} f(x) \\cos(\\frac{2\\pi}{T}nx) dx $$ </div>
<div> $$ b_k = \\frac{2}{T} \\int_{-T/2}^{T/2} f(x) \\sin(\\frac{2\\pi}{T}nx) dx $$ </div>
<p> Gdzie: </p>
<ul>
  <li>\( <i>a_0</i> \) to wyraz wolny obliczony jako średnia wartość funkcji na przedziale</li>
  <li>\( <i>a_k</i>, <i>b_k</i> \) to współczynniki Fouriera, obliczone według powyższych wzorów</li>
  <li>\( <i>T</i> \) to okres analizowanej funkcji</li>
  <li>\( <i>n</i> \) to liczba wyrazów szeregu trygonometrycznego</li>
</ul>`;

midpointIntegration = `<p> Metoda całkowania punktu środkowego może być zapisana w postaci: </p>
<div> $$ I \\approx \\sum_{i=1}^{n} f\\left(x_i^*\\right) \\Delta x $$ </div>
<div> $$ x_i^* = x_{i-1} + \\frac{\\Delta x}{2} $$ </div>
<p> Gdzie: </p>
<ul>
  <li>\( I \) to przybliżona wartość całki </li>
  <li>\( x_i^* \) to punkt środkowy przedziału \( [x_{i-1}, x_i] \) </li>
  <li>\( \Delta x \) to długość przedziału podziału, obliczona jako \( \Delta x = \\frac{b-a}{n} \) </li>
  <li>\( n \) to liczba podprzedziałów całkowania </li>
  <li>\( a, b \) to granice całkowania </li>
</ul>`

isProcessing: boolean = false;

  ngAfterViewInit() {
    this.mathjaxService.renderMath();
  }

  constructor(private mathjaxService: MathjaxService, private http: HttpClient, private router: Router){}

  async showExample(type: String) {
    if (this.isProcessing) {
      return;
    }

    this.isProcessing = true;

    if(type = 'Polynomial') {
      const data = new InterpolationData(4, [new Point(1, 3), new Point(2, 6), new Point(4, -2), new Point(6, 4)], 3, true);

      try {
        const result: InterpolationResult | undefined = await this.http.post<InterpolationResult>(
          JAVA_URL+'polynomial_interpolation',
          data
        ).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        console.log(data.isTest);
        ResultDataService.SetInterpolationResult(result as InterpolationResult, data);
        ResultDataService.SetResultType(INTERPOLATION);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
    if(type = 'Trigonometric') {
      const data = new InterpolationData(5, [new Point(0, 1), new Point(1, 2.5), new Point(2, -1), new Point(3, 0), new Point(4, 1)], 2.5, true);

      try {
        const result: InterpolationResult | undefined = await this.http.post<InterpolationResult>(
          JAVA_URL+'trigonometric_interpolation',
          data
        ).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        console.log(data.isTest);
        ResultDataService.SetInterpolationResult(result as InterpolationResult, data);
        ResultDataService.SetResultType(INTERPOLATION);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
  }
}
