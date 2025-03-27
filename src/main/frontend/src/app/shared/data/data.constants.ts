export const INTERPOLATION_DATA = "InterpolationData";
export const INTEGRATION_DATA = "IntegrationData";
export const SYSTEM_OF_EQUATIONS_DATA = "SystemOfEquationsData";

export const INTERPOLATION = "Interpolation";
export const INTEGRATION = "Integration";
export const SYSTEM_OF_EQUATIONS = "SystemOfEquations";

export const SQRT = /SQRT\(([^)]+)\)/gi;
export const LN = /LN\(([^)]+)\)/gi;

export const JAVA_URL = "http://localhost:8081/calculations/";

export const CONSTANTS: { [key: string]: number } = {
  PI: parseFloat(Math.PI.toFixed(5)),
  E: parseFloat(Math.E.toFixed(5))
};
