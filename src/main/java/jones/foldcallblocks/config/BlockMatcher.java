package jones.foldcallblocks.config;

public class BlockMatcher {
  private String blockIdentifier;
  private boolean prefixWhenFolding;
  private boolean shouldAddNewline;

  @SuppressWarnings("unused")
  public BlockMatcher() {
    this("", true, false);
  }

  public BlockMatcher(BlockMatcher existingRule) {
    this(existingRule.blockIdentifier, existingRule.prefixWhenFolding, false);
  }

  public BlockMatcher(String blockIdentifier, boolean prefixWhenFolding) {
    this(blockIdentifier, prefixWhenFolding, false);
  }

  public BlockMatcher(String blockIdentifier, boolean prefixWhenFolding, boolean shouldAddNewline) {
    this.blockIdentifier = blockIdentifier;
    this.prefixWhenFolding = prefixWhenFolding;
    this.shouldAddNewline = shouldAddNewline;
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

  public boolean shouldAddNewline() {
    return shouldAddNewline;
  }

  public void setShouldAddNewline(boolean shouldAddNewline) {
    this.shouldAddNewline = shouldAddNewline;
  }
}
