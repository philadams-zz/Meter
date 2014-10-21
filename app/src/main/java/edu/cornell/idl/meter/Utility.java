package edu.cornell.idl.meter;

/**
 * Various utility functions.
 */
public class Utility {

  public static float linearlyScale(float x, float oldMin, float oldMax, float newMin,
      float newMax) {
    float oldRange = oldMax - oldMin;
    float newRange = newMax - newMin;
    return (((x - oldMin) * newRange) / oldRange) + newMin;
  }
}
