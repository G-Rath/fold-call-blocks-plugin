package jones.foldtestblocks;

import org.jetbrains.annotations.NotNull;

public class RubyTestBlockFoldingBuilderTest extends BaseTestBlockFoldingBuilderTest {
  @NotNull
  protected String getRootTestDataFolder() {
    return "ruby";
  }

  public void testFolding() {
    myFixture.testFolding(getTestDataPath() + "/FoldingTestData.rb");
  }
}
