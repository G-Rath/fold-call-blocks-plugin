package jones.foldcallblocks;

import org.jetbrains.annotations.NotNull;

public class JSBlockFoldingBuilderTest extends BaseBlockFoldingBuilderTest {
  @NotNull
  protected String getRootTestDataFolder() {
    return "js";
  }

  public void testFolding() {
    myFixture.testFolding(getTestDataPath() + "/FoldingTestData.ts");
  }
}
