package jones.foldcallblocks.config;

public class BlockMatcher {
  private String blockIdentifier;
  private boolean prefixWhenFolding;

  @SuppressWarnings("unused")
  public BlockMatcher() {
    this("", true);
  }

  public BlockMatcher(BlockMatcher existingRule) {
    this(existingRule.blockIdentifier, existingRule.prefixWhenFolding);
  }

  public BlockMatcher(String blockIdentifier, boolean prefixWhenFolding) {
    this.blockIdentifier = blockIdentifier;
    this.prefixWhenFolding = prefixWhenFolding;
  }

  public String getBlockIdentifier() {
    return blockIdentifier;
  }

  public void setBlockIdentifier(String blockIdentifier) {
    this.blockIdentifier = blockIdentifier;
  }

  public boolean isPrefixWhenFolding() {
    return prefixWhenFolding;
  }

  public void setPrefixWhenFolding(boolean prefixWhenFolding) {
    this.prefixWhenFolding = prefixWhenFolding;
  }
}
