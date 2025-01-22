import { Injectable } from '@angular/core';

declare const MathJax: any;

@Injectable({
  providedIn: 'root'
})
export class MathjaxService {
  renderMath() {
    if (MathJax && MathJax.typesetPromise) {
      MathJax.typesetPromise();
    }
  }
}

