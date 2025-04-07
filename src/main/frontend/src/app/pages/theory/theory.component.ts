import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { INTEGRATION, INTERPOLATION, JAVA_URL, SYSTEM_OF_EQUATIONS } from 'src/app/shared/data/data.constants';
import { IntegrationData, IntegrationResult, InterpolationData, InterpolationResult, Point, SystemOfEquationsData, SystemOfEquationsResult } from 'src/app/shared/data/data.interface';
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
<div> $$ a_k = \\frac{2}{T} \\int_\\frac{-T}{2}^\\frac{T}{2} f(x) \\cos(\\frac{2\\pi}{T}nx) dx $$ </div>
<div> $$ b_k = \\frac{2}{T} \\int_\\frac{-T}{2}^\\frac{T}{2} f(x) \\sin(\\frac{2\\pi}{T}nx) dx $$ </div>
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
<div> $$ \\Delta x = \\frac{b-a}{n} $$ </div>
<p> Gdzie: </p>
<ul>
  <li>\ <i>( I \)</i> to przybliżona wartość całki </li>
  <li>\ <i>( x_i* \)</i> to punkt środkowy przedziału <i>[x_{i-1}, x_i]</i> </li>
  <li>\ <i>( delta x \)</i> to długość przedziału podziału, gdzie <i>[a, b]</i> to granice całkowania</li>
  <li>\ <i>( n \)</i> to liczba podprzedziałów całkowania </li>
</ul>`;

simpsonsIntegration = `<p> Metoda całkowania Simpsona może być zapisana w postaci: </p>
<div> $$ I \\approx \\frac{\\Delta x}{3} \\left[f(a) + 4 \\sum_{i=1, \\text{ i nieparzyste}}^{n-1} f(x_i) + 2 \\sum_{i=2, \\text{ i parzyste}}^{n-2} f(x_i) + f(b)\\right] $$ </div>
<div> $$ \\Delta x = \\frac{b-a}{n} $$ </div>
<p> Gdzie: </p>
<ul>
  <li>\ <i>( I )</i> to przybliżona wartość całki </li>
  <li>\ <i>( delta x )</i> to długość przedziału podziału, gdzie <i>[a, b]</i> to granice całkowania </li>
  <li>\ <i>( n )</i> to liczba podprzedziałów całkowania, która musi być liczbą parzystą </li>
</ul>`;

trapezoidalIntegration = `<p> Metoda całkowania trapezów może być zapisana w postaci: </p>
<div> $$ I \\approx \\frac{\\Delta x}{2} \\left[f(a) + 2 \\sum_{i=1}^{n-1} f(x_i) + f(b)\\right] $$ </div>
<div> $$ \\Delta x = \\frac{b-a}{n} $$ </div>
<p> Gdzie: </p>
<ul>
  <li>\ <i>( I )</i> to przybliżona wartość całki </li>
  <li>\ <i>( delta x )</i> to długość przedziału podziału, gdzie <i>[a, b]</i> to granice całkowania </li>
  <li>\ <i>( n )</i> to liczba podprzedziałów całkowania </li>
</ul>`;

gaussKronrodIntegration = `<p> Metoda całkowania Gaussa-Kronroda może być zapisana w postaci: </p>
<div> $$ I \\approx \\sum_{i=1}^{n} w_i f(x_i) $$ </div>
<p> Gdzie: </p>
<ul>
  <li>\ <i>( I )</i> to przybliżona wartość całki </li>
  <li>\ <i>( w_i )</i> to wagi obliczane w metodzie Gaussa-Kronroda </li>
  <li>\ <i>( x_i )</i> to węzły całkowania, czyli punkty, w których funkcja jest obliczana </li>
  <li>\ Wagi i węzły są dobierane w taki sposób, aby zapewnić wysoką dokładność metody </li>
</ul>`;

cramerMethod = `<div>
$$
\\begin{bmatrix}
a_{11} & a_{12} & \\cdots & a_{1n} \\\\
a_{21} & a_{22} & \\cdots & a_{2n} \\\\
\\vdots & \\vdots & \\ddots & \\vdots \\\\
a_{n1} & a_{n2} & \\cdots & a_{nn}
\\end{bmatrix}
\\cdot
\\begin{bmatrix}
x_1 \\\\
x_2 \\\\
\\vdots \\\\
x_n
\\end{bmatrix}
=
\\begin{bmatrix}
b_1 \\\\
b_2 \\\\
\\vdots \\\\
b_n
\\end{bmatrix}
$$
</div>
<p> Kroki metody Kramera: </p>
<ol>
  <li> Obliczenie wyznacznika macierzy głównej:
  <div>
  $$
  \\text{det}(A) =
  \\begin{vmatrix}
  a_{11} & a_{12} & \\cdots & a_{1n} \\\\
  a_{21} & a_{22} & \\cdots & a_{2n} \\\\
  \\vdots & \\vdots & \\ddots & \\vdots \\\\
  a_{n1} & a_{n2} & \\cdots & a_{nn}
  \\end{vmatrix}
  $$
  </div>
  </li>
  <li> Dla każdej niewiadomej \\(x_i\\), zastąpienie \\(i\\)-tej kolumny macierzy głównej wektorem wyrazów wolnych \\(b\\) i obliczenie wyznacznika nowej macierzy:
  <div>
  $$
  \\text{det}(A_i) =
  \\begin{vmatrix}
  a_{11} & \\cdots & b_1 & \\cdots & a_{1n} \\\\
  a_{21} & \\cdots & b_2 & \\cdots & a_{2n} \\\\
  \\vdots & \\ddots & \\vdots & \\ddots & \\vdots \\\\
  a_{n1} & \\cdots & b_n & \\cdots & a_{nn}
  \\end{vmatrix}
  $$
  </div>
  </li>
  <li> Obliczenie wartości każdej niewiadomej jako ilorazu wyznaczników:
  <div>
  $$
  x_i = \\frac{\\text{det}(A_i)}{\\text{det}(A)}
  $$
  </div>
  </li>
</ol>
<p> Metoda Kramera jest prostym narzędziem teoretycznym, ale jej zastosowanie jest ograniczone do małych układów ze względu na czasochłonność obliczania wyznaczników. </p>`;


multigridMethod = `
<div>
$$
A \\cdot x = b, \\quad
\\begin{bmatrix}
a_{11} & a_{12} & \\cdots & a_{1n} \\\\
a_{21} & a_{22} & \\cdots & a_{2n} \\\\
\\vdots & \\vdots & \\ddots & \\vdots \\\\
a_{n1} & a_{n2} & \\cdots & a_{nn}
\\end{bmatrix}
\\cdot
\\begin{bmatrix}
x_1 \\\\
x_2 \\\\
\\vdots \\\\
x_n
\\end{bmatrix}
=
\\begin{bmatrix}
b_1 \\\\
b_2 \\\\
\\vdots \\\\
b_n
\\end{bmatrix}
$$
</div>
<p> Kroki metody: </p>
<ol>
  <li> Sprowadzenie macierzy współczynników do postaci przekątniowo dominującej, czyli postaci w której wartości bezwzględne elementów na głównej przekątnej
  są większe od sumy wartości bezwzględnych pozostałych elementów w wierszach</li>
  <li> Rozwiązanie przybliżone problemu na siatce o małej gęstości (pre-smoothing)</li>
  <li> Obliczenie reszty:
  <div>
  $$
  r = b - A \\cdot x
  $$
  </div>
  i projekcja reszty na siatkę o mniejszej rozdzielczości (restrykcja)</li>
  <li> Rozwiązanie problemu reszty na siatce o mniejszej rozdzielczości</li>
  <li> Interpolacja poprawki na siatkę o większej rozdzielczości (prolongacja)</li>
  <li> Korekta rozwiązania:
  <div>
  $$
  x \\gets x + \\text{poprawka}
  $$
  </div>
</ol>
<p> Kroki występujące w punktach 2-6 należy wykonywać iteracyjnie, do momentu uzyskania zadowalającego przybliżenia. </p>`;

  isProcessing: boolean = false;
  sectionTitles: string[] = [];
  filteredTitles: string[] = [];
  filteredSections: string[] = [];
  searchQuery: string = '';
  visibleTooltip: string | null = null;

  sections = [
    'Interpolacja wielomianowa',
    'Interpolacja trygonometryczna',
    'Całkowanie metodą punktu środkowego',
    'Całkowanie metodą trapezów',
    'Całkowanie metodą Simpsona',
    'Całkowanie metodą Gaussa-Kronroda',
    'Wzory Kramera',
    'Metoda wielosiatkowa'
  ];

  @ViewChild('searchInput') searchInput!: ElementRef;

  constructor(private elRef: ElementRef, private mathjaxService: MathjaxService, private http: HttpClient, private router: Router) {
  }

  ngAfterViewInit(): void {
    this.mathjaxService.renderMath();
  }

  filterSections(): void {
    const query = this.searchQuery.trim().toLowerCase();
    if (query.length > 0) {
      this.filteredSections = this.sections.filter(section =>
        section.toLowerCase().includes(query)
      );
    } else {
      this.filteredSections = [];
    }
  }

  selectSection(section: string): void {
    this.searchQuery = section;
    this.filteredSections = [];
    this.navigateToSection();
  }

  navigateToSection(): void {
    const formattedQuery = this.searchQuery.trim().toLowerCase();
    const matchingSection = this.sections.find(title => title.toLowerCase() === formattedQuery);

    console.log(formattedQuery);
    console.log(matchingSection);
    if (matchingSection) {
      const sectionElement = document.getElementById(matchingSection);

      console.log(sectionElement);
      if (sectionElement) {
        sectionElement.scrollIntoView({ behavior: 'smooth' });
      }
    }
  }

  async showExample(type: String) {
    if (this.isProcessing) {
      return;
    }
    this.isProcessing = true;

    if(type == 'Polynomial') {
      const data = new InterpolationData(4, [new Point(1, 3), new Point(2, 6), new Point(4, -2), new Point(6, 4)], 3, true);

      try {
        const result: InterpolationResult | undefined = await this.http.post<InterpolationResult>(JAVA_URL+'polynomial_interpolation',data).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        ResultDataService.SetInterpolationResult(result as InterpolationResult, data);
        ResultDataService.SetResultType(INTERPOLATION);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
    else if(type == 'Trigonometric') {
      const data = new InterpolationData(4, [new Point(1, 3), new Point(2, 6), new Point(4, -2), new Point(6, 4)], 3, true);

      try {
        const result: InterpolationResult | undefined = await this.http.post<InterpolationResult>(JAVA_URL+'trigonometric_interpolation',data).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        ResultDataService.SetInterpolationResult(result as InterpolationResult, data);
        ResultDataService.SetResultType(INTERPOLATION);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
    else if(type == 'Midpoint') {
      const data = new IntegrationData(2, [4, 1, 1], 20, 0, 5, true);

      try {
        const result: IntegrationResult | undefined = await this.http.post<IntegrationResult>(JAVA_URL+'midpoint_integration',data).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        ResultDataService.SetIntegrationResult(result as IntegrationResult, data);
        ResultDataService.SetResultType(INTEGRATION);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
    else if(type == 'Simpsons') {
      const data = new IntegrationData(2, [4, 2, 1], 20, 0, 5, true);

      try {
        const result: IntegrationResult | undefined = await this.http.post<IntegrationResult>(JAVA_URL+'simpson_integration',data).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        ResultDataService.SetIntegrationResult(result as IntegrationResult, data);
        ResultDataService.SetResultType(INTEGRATION);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
    else if(type == 'Trapezoidal') {
      const data = new IntegrationData(2, [4, 3, 1], 20, 0, 5, true);

      try {
        const result: IntegrationResult | undefined = await this.http.post<IntegrationResult>(JAVA_URL+'trapezoidal_integration',data).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        ResultDataService.SetIntegrationResult(result as IntegrationResult, data);
        ResultDataService.SetResultType(INTEGRATION);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
    else if(type == 'GaussKronrod') {
      const data = new IntegrationData(2, [4, 4, 1], 20, 0, 5, true);

      try {
        const result: IntegrationResult | undefined = await this.http.post<IntegrationResult>(JAVA_URL+'gauss_kronrod_integration',data).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        ResultDataService.SetIntegrationResult(result as IntegrationResult, data);
        ResultDataService.SetResultType(INTEGRATION);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
    else if(type == 'Cramer') {
      const data = new SystemOfEquationsData([[4, -1, 0], [-1, 4, -1], [0, -1, 4]], [2, 6, 2], true);

      try {
        const result: SystemOfEquationsResult | undefined = await this.http.post<SystemOfEquationsResult>(
          JAVA_URL+'cramer_system_of_equations',
          data
        ).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        console.log(data.isTest);
        ResultDataService.SetSystemOfEquationsResult(result as SystemOfEquationsResult, data);
        ResultDataService.SetResultType(SYSTEM_OF_EQUATIONS);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
    else if(type == 'Multigrid') {
      const data = new SystemOfEquationsData([[4, -1, 0], [-1, 4, -1], [0, -1, 4]], [2, 6, 2], true);

      try {
        const result: SystemOfEquationsResult | undefined = await this.http.post<SystemOfEquationsResult>(
          JAVA_URL+'multigrid_system_of_equations',
          data
        ).toPromise();

        if(typeof result === undefined) {
          throw new Error('result is undefined');
        }
        console.log(data.isTest);
        ResultDataService.SetSystemOfEquationsResult(result as SystemOfEquationsResult, data);
        ResultDataService.SetResultType(SYSTEM_OF_EQUATIONS);

        this.router.navigate(['/result']);
      } catch (error) {
        console.error('Error:', error);
      }
    }
  }

  toggleTooltip() {
    this.visibleTooltip = this.visibleTooltip === null ? 'pressed' : null;
  }
}
