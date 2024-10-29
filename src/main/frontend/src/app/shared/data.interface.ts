export interface InterpolationData {
  pointsNumber: number;
  searchedValue: number;
  points: Point[];
}

export interface IntegrationData {
  degree: number;
  factors: number[];
  sections: number;
  Xp: number;
  Xk: number;
}

export interface Result {
  result: number;
  name: String;
}

export interface Point {
  x: number;
  y: number;
}
