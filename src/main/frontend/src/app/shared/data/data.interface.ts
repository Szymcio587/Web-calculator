export class InterpolationData {
  dataType: String;
  username: String;
  pointsNumber: number;
  searchedValue: number;
  points: Point[];
  isTest: boolean;

  constructor(pointsNumber: number, points: Point[], searchedValue: number, isTest: boolean, username?: String) {
    this.dataType = 'Interpolation';
    this.pointsNumber = pointsNumber;
    this.searchedValue = searchedValue;
    this.points = points;
    this.isTest = isTest;
    if(username) {
      this.username = username;
    }
    else {
      this.username = '';
    }
  }
}

export class InterpolationResult {
  result: number;
  coefficients: number[];
  prompt: String;
  explanation: String;

  constructor(result: number, coefficients: number[], explanation: String, prompt?: String) {
    this.result = result;
    this.coefficients = coefficients;
    this.explanation = explanation;
    if(prompt) {
      this.prompt = prompt;
    }
    else {
      this.prompt = '';
    }
  }
}

export class InterpolationRecord implements BaseData {
  dataType: String;
  username: String;
  pointsNumber: number;
  searchedValue: number;
  points: Point[];
  result: number;
  coefficients: number[];
  explanation: String;
  constructor(username: String, pointsNumber: number, points: Point[], searchedValue: number, isTest: boolean, result: number, coefficients: number[], explanation: String) {
    this.username = username;
    this.dataType = 'Interpolation';
    this.pointsNumber = pointsNumber;
    this.searchedValue = searchedValue;
    this.points = points;
    this.result = result;
    this.coefficients = coefficients;
    this.explanation = explanation;
  }
}

export class IntegrationData {
  dataType: String;
  username: String;
  degree: number;
  factors: number[];
  sections: number;
  Xp: number;
  Xk: number;
  isTest: boolean;
  customFunction: String;

  constructor(degree: number, factors: number[], sections: number, Xp: number, Xk: number, isTest: boolean, customFunction?: String, username?: String) {
    this.dataType = 'Integration';
    this.degree = degree;
    this.factors = factors;
    this.sections = sections;
    this.Xp = Xp;
    this.Xk = Xk;
    this.isTest = isTest;
    if(customFunction) {
      this.customFunction = customFunction;
    }
    else {
      this.customFunction = '';
    }
    if(username) {
      this.username = username;
    }
    else {
      this.username = '';
    }
  }
}

export class IntegrationResult {
  result: number;
  prompt: String;
  explanation: String;

  constructor(result: number, explanation: String, prompt?: String) {
    this.result = result;
    this.explanation = explanation;
    if(prompt) {
      this.prompt = prompt;
    }
    else {
      this.prompt = '';
    }
  }
}

export class IntegrationRecord implements BaseData {
  dataType: String;
  username: String;
  degree: number;
  factors: number[];
  sections: number;
  Xp: number;
  Xk: number;
  result: number;
  explanation: String;
  customFunction: String;
  constructor(username: String, degree: number, factors: number[], sections: number, Xp: number, Xk: number, result: number, explanation: String, customFunction?: String) {
    this.username = username;
    this.dataType = 'Integration';
    this.degree = degree;
    this.factors = factors;
    this.sections = sections;
    this.Xp = Xp;
    this.Xk = Xk;
    this.result = result;
    this.explanation = explanation;
    if(customFunction) {
      this.customFunction = customFunction;
    }
    else {
      this.customFunction = '';
    }
  }
}

export class SystemOfEquationsData {
  dataType: String;
  username: String;
  coefficients: number[][];
  constants: number[];
  isTest: boolean;

  constructor(coefficients: number[][], constants: number[], isTest: boolean, username?: String) {
    this.dataType = 'SystemOfEquations';
    this.coefficients = coefficients;
    this.constants = constants;
    this.isTest = isTest;
    if(username) {
      this.username = username;
    }
    else {
      this.username = '';
    }
  }
}

export class SystemOfEquationsResult {
  solutions: number[];
  prompt: String;
  explanation: String;

  constructor(solutions: number[], explanation: String, prompt?: String) {
    this.solutions = solutions;
    this.explanation = explanation;
    if(prompt) {
      this.prompt = prompt;
    }
    else {
      this.prompt = '';
    }
  }
}

export class SystemOfEquationsRecord implements BaseData {
  dataType: String;
  username: String;
  coefficients: number[][];
  constants: number[];
  solutions: number[];
  explanation: String;

  constructor(username: String, coefficients: number[][], constants: number[], solutions: number[], explanation: String) {
    this.dataType = 'SystemOfEquations';
    this.username = username;
    this.coefficients = coefficients;
    this.constants = constants;
    this.solutions = solutions;
    this.explanation = explanation;
  }
}

export class Point {
  x: number;
  y: number;

  constructor(x: number, y: number) {
    this.x = x;
    this.y = y;
  }
}

export interface BaseData {
  dataType: String;
}
