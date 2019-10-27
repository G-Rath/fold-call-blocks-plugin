package jones.foldtestblocks;

import org.jetbrains.annotations.NotNull;

public class JSBlockFoldingBuilderTest extends BaseTestBlockFoldingBuilderTest {
  @NotNull
  protected String getRootTestDataFolder() {
    return "js";
  }

  public void testFolding() {
    myFixture.testFolding(getTestDataPath() + "/FoldingTestData.ts");
  }
}
