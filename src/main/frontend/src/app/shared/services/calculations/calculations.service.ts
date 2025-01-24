import { Injectable } from '@angular/core';
import { CONSTANTS, LN, SQRT } from '../../data/data.constants';

@Injectable({
  providedIn: 'root'
})
export class CalculationsService {

  constructor() { }

  public static parseInput(input: string | number): number {
    if (typeof input === 'number') {
      return input;
    }

    const upperInput = input.toUpperCase();

    const sqrtReplacedInput = upperInput.replace(SQRT, (_, inside) => CalculationsService.handleSqrt(inside));

    const lnReplacedInput = sqrtReplacedInput.replace(LN, (_, inside) => CalculationsService.handleLn(inside));

    const constantsPattern = new RegExp(Object.keys(CONSTANTS).join('|'), 'g');

    const replacedInput = lnReplacedInput.replace(constantsPattern, (match) => CalculationsService.handleConstants(match));

    try {
      const result = eval(replacedInput);
      if (!isNaN(result)) {
        return parseFloat(result.toFixed(5));
      }
    } catch (error) {
      throw new Error(`Invalid input: ${input}`);
    }

    throw new Error(`Invalid input: ${input}`);
  }

  private static handleSqrt(inside: any) {
    const value = this.parseInput(inside);
    if (value < 0) {
      throw new Error(`Invalid input in SQRT: Cannot take square root of a negative number (${inside})`);
    }
    return Math.sqrt(value).toFixed(5);
  }

  private static handleLn(inside: any) {
    const value = this.parseInput(inside);
    if (value <= 0) {
      throw new Error(`Invalid input in LN: Cannot take the natural logarithm of a non-positive number (${inside})`);
    }
    return Math.log(value).toFixed(5);
  }

  private static handleConstants(match: any) {
    return CONSTANTS[match].toString();
  }
}
