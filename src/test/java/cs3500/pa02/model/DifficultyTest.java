package cs3500.pa02.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 *  Tests the elements of the Difficulty enum.
 */
class DifficultyTest {

  @Test
  public void diffTest() {
    Difficulty e = Difficulty.EASY;
    Difficulty h = Difficulty.HARD;

    assertEquals(e, Difficulty.EASY);
    assertEquals(h, Difficulty.HARD);
    assertNotEquals(e, h);
  }

}