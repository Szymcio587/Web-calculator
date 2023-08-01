export interface InterpolationData {
  pointsNumber: number;
  searchedValue: number;
  points: Point[];
}

export interface IntegralData {
  degree: number;
  factors: number[];
  sections: number;
  Xp: number;
  Xk: number;
}

export interface Point {
  x: number;
  y: number;
}
