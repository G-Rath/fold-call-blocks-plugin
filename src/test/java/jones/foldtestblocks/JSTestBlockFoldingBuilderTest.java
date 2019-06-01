package jones.foldtestblocks;

import org.jetbrains.annotations.NotNull;

public class JSTestBlockFoldingBuilderTest extends BaseTestBlockFoldingBuilderTest {
  @NotNull
  protected String getRootTestDataFolder() {
    return "js";
  }

  public void testFolding() {
    myFixture.testFolding(getTestDataPath() + "/FoldingTestData.ts");
  }
}
