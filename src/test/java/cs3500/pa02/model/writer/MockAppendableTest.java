package cs3500.pa02.model.writer;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  Tests the elements (constructors and methods) of the MockAppendable class, which also
 *  serves to test all related helper methods.
 */
class MockAppendableTest {

  MockAppendable mock;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initData() {
    this.mock = new MockAppendable();
  }

  @Test
  public void append() {
    assertThrows(IOException.class, () -> mock.append("hello"));
  }

  @Test
  public void testAppend() {
    assertThrows(IOException.class, () -> mock.append("hello", 0, 2));
  }

  @Test
  public void testAppend1() {
    assertThrows(IOException.class, () -> mock.append('c'));
  }
}