package jones.foldtestblocks;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

abstract public class BaseBlockFoldingBuilderTest extends LightCodeInsightFixtureTestCase {
  @NotNull
  abstract protected String getRootTestDataFolder();

  @Override
  protected String getTestDataPath() {
    return "testData/".concat(getRootTestDataFolder());
  }
}
