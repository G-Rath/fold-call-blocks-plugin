package jones.foldtestblocks;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.ruby.ruby.lang.psi.RPsiElement;
import org.jetbrains.plugins.ruby.ruby.lang.psi.RubyElementType;
import org.jetbrains.plugins.ruby.ruby.lang.psi.expressions.RLiteral;
import org.jetbrains.plugins.ruby.ruby.lang.psi.iterators.RBlockCall;

import java.util.Arrays;

// todo: should we be extending FoldingBuilderEx instead?
public class RubyBlockFoldingBuilder implements FoldingBuilder {
  private static final String[] testBlockCommands = {
    "describe",
    "context",
    "it"
  };

  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
    PsiElement nodePsi = node.getPsi();

    return (
      PsiTreeUtil.findChildrenOfType(nodePsi, RBlockCall.class)
                 .stream()
                 .filter(this::shouldFoldBlock)
                 .map(blockCall -> createFoldingDescriptor(blockCall, document))
                 .toArray(FoldingDescriptor[]::new)
    );
  }

  /**
   * Checks if the given {@code blockCall} should be folded
   *
   * @param blockCall the block call to check
   *
   * @return {@code true} if the given {@code callExpression} should be folded; otherwise {@code false}
   */
  private boolean shouldFoldBlock(@NotNull RBlockCall blockCall) {
    if(blockCall.getArguments().isEmpty()) {
      return false;
    }

    return Arrays.asList(RubyBlockFoldingBuilder.testBlockCommands).contains(blockCall.getCommand());
  }

  @NotNull
  private FoldingDescriptor createFoldingDescriptor(@NotNull RBlockCall blockCall, @NotNull Document document) {
    return new FoldingDescriptor(
      blockCall.getNode(),
      calculateFoldingTextRange(blockCall, document),
      null,
      buildPlaceholderText(blockCall)
    );
  }

  private boolean shouldIgnoreSibling(@NotNull PsiElement sibling) {
    if(sibling instanceof PsiWhiteSpace) {
      return true;
    }

    if(sibling instanceof LeafPsiElement) {
      LeafPsiElement leaf = (LeafPsiElement) sibling;

      IElementType leafElementType = leaf.getElementType();

      if(leafElementType instanceof RubyElementType) {
        RubyElementType rubyElementType = (RubyElementType) leafElementType;

        /*
          couldn't find "end of line" type defined anywhere, so using this for now.
          suspect "2210" is a bad bet to rely on, as it might change if other types are registered.
         */
        return rubyElementType.getRealText().equals("end of line") || rubyElementType.getIndex() == 2210;
      }
    }

    return false;
  }

  /**
   * Finds the immediate next sibling of the given `blockCall` that is considered a test block, ignoring whitespace blocks.
   *
   * @param blockCall the call expression whose next sibling to find
   *
   * @return the next {@code RBlockCall} considered to be a test block, or {@code null} if there isn't one
   */
  @Nullable
  private RBlockCall findImmediateNextTestBlockSibling(@NotNull RBlockCall blockCall) {
    for(PsiElement sibling = blockCall.getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {
      if(shouldIgnoreSibling(sibling)) {
        continue;
      }

      if(!(sibling instanceof RBlockCall)) {
        return null;
      }

      if(shouldFoldBlock((RBlockCall) sibling)) {
        return (RBlockCall) sibling;
      }
    }

    return null;
  }

  /**
   * Calculates the `TextRange` to use for folding the given `blockCall`.
   *
   * @param blockCall the `RBlockCall` who's `TextRange` to calculate
   * @param document  the `document` the `blockCall` is in
   *
   * @return the `TextRange` to fold over
   */
  private TextRange calculateFoldingTextRange(@NotNull RBlockCall blockCall, @NotNull Document document) {
    TextRange range = blockCall.getTextRange();

    RBlockCall nextTestBlockSibling = findImmediateNextTestBlockSibling(blockCall);

    if(nextTestBlockSibling == null) {
      return range;
    }

    // line number that the *start* of the next test block is on
    int nextTestBlockStartOffset = nextTestBlockSibling.getTextRange().getStartOffset();
    int nextTestBlockStartLineNum = document.getLineNumber(nextTestBlockStartOffset);

    // offset of the start of the line before the start of the next test block
    int offsetOfLineBeforeNextTestBlock = document.getLineEndOffset(nextTestBlockStartLineNum - 1);

    if(range.getEndOffset() > offsetOfLineBeforeNextTestBlock) {
      offsetOfLineBeforeNextTestBlock = range.getEndOffset();
    }

    return new TextRange(
      range.getStartOffset(),
      offsetOfLineBeforeNextTestBlock
    );
  }

  /**
   * Builds the placeholder text to use when folding the given {@code blockCall}.
   *
   * @param blockCall the {@code RBlockCall} being folded
   *
   * @return the placeholder text to use when folding the given {@code blockCall}
   */
  private String buildPlaceholderText(@NotNull RBlockCall blockCall) {
    RPsiElement firstCallArgument = blockCall.getArguments().get(0);

    if(firstCallArgument instanceof RLiteral) {
      return ((RLiteral) firstCallArgument).getContent();
    }

    return blockCall.getText();
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    return null;
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    PsiElement psiElement = node.getPsi();

    if(psiElement instanceof RBlockCall) {
      return shouldFoldBlock((RBlockCall) psiElement);
    }

    return true;
  }
}
