package jones.foldtestblocks;

import org.jetbrains.annotations.NotNull;

public class RubyBlockFoldingBuilderTest extends BaseBlockFoldingBuilderTest {
  @NotNull
  protected String getRootTestDataFolder() {
    return "ruby";
  }

  public void testFolding() {
    myFixture.testFolding(getTestDataPath() + "/FoldingTestData.rb");
  }
}
