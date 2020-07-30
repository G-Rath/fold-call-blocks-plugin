package jones.foldcallblocks;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.javascript.psi.JSCallExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSExpressionStatement;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import jones.foldcallblocks.config.BlockMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class JSBlockFoldingBuilder implements FoldingBuilder {
  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
    PsiElement nodePsi = node.getPsi();

    return (
      PsiTreeUtil.findChildrenOfType(nodePsi, JSCallExpression.class)
                 .stream()
                 .filter(this::shouldFoldBlock)
                 .map(callExpression -> createFoldingDescriptor(callExpression, document))
                 .toArray(FoldingDescriptor[]::new)
    );
  }

  /**
   * Checks if the given {@code expression} is "supported", by comparing it against the configured {@code BlockMatcher}s.
   * <p>
   * Only {@code expression}s that are instances of {@code JSCallExpression} can be supported blocks,
   * but not all {@code JSCallExpression}s are supported blocks.
   *
   * @param expression the expression to check
   *
   * @return {@code true} if the given {@code expression} is considered "supported"; otherwise {@code false}
   */
  private boolean isSupportedBlock(@Nullable JSExpression expression) {
    if(expression instanceof JSCallExpression) {
      return shouldFoldBlock((JSCallExpression) expression);
    }

    return false;
  }

  private List<BlockMatcher> listBlockMatchers(Project project) {
    return BlockMatchersManager.getInstance(project).getRules();
  }

  /**
   * Finds the first matching {@code BlockMatcher} for the given {@code callExpression}
   * <p>
   * If no matchers match, {@code null} is returned instead.
   *
   * @param callExpression the call expression to find a matching {@code BlockMatcher} for
   *
   * @return the first {@code TestBlockRule} that matches the given {@code callExpression} if any;
   *   otherwise {@code null}
   */
  @Nullable
  private BlockMatcher findMatcherForBlock(JSCallExpression callExpression) {
    List<BlockMatcher> blockMatchers = listBlockMatchers(callExpression.getProject());

    JSExpression methodExpression = callExpression.getMethodExpression();

    if(methodExpression == null) {
      throw new UnsupportedOperationException();
    }

    return blockMatchers
             .stream()
             .filter(blockMatcher -> blockMatcher.getBlockIdentifier().equals(methodExpression.getText()))
             .findFirst().orElse(null);
  }

  /**
   * Checks if the given {@code callExpression} should be folded
   *
   * @param callExpression the call expression to check
   *
   * @return {@code true} if the given {@code callExpression} should be folded; otherwise {@code false}
   */
  private boolean shouldFoldBlock(@NotNull JSCallExpression callExpression) {
    if(callExpression.getArguments().length == 0) {
      return false;
    }

    JSExpression methodExpression = callExpression.getMethodExpression();

    if(methodExpression == null || isTopMostCallExpression(callExpression)) {
      return false;
    }

    return findMatcherForBlock(callExpression) != null;
  }

  /**
   * Checks if the given {@code callExpression} is the top most call expression,
   * by checking if it's parents parent is an instance of {@code PsiFile}.
   *
   * @param callExpression the expression to check
   *
   * @return {@code true} if the given {@code testBlockCallExp} is the top most call expression; otherwise {@code false}
   */
  private boolean isTopMostCallExpression(@NotNull JSCallExpression callExpression) {
    return callExpression.getParent().getParent() instanceof PsiFile;
  }

  @NotNull
  private FoldingDescriptor createFoldingDescriptor(@NotNull JSCallExpression callExpression, @NotNull Document document) {
    // JavaCodeFoldingSettings.getInstance().isCollapseEndOfLineComments()
    PsiElement expressionParent = callExpression.getParent();

    FoldingGroup group = FoldingGroup.newGroup("test-block");

    BlockMatcher matcherForBlock = Objects.requireNonNull(
      findMatcherForBlock(callExpression),
      "A folding descriptor should not be constructed for a JSCallExpression without a BlockMatcher!"
    );

    String placeholderText = buildPlaceholderText(callExpression);

    if(matcherForBlock.isPrefixWhenFolding()) {
      placeholderText = matcherForBlock.getBlockIdentifier() + " " + placeholderText;
    }

    return new FoldingDescriptor(
      expressionParent.getNode(),
      calculateFoldingTextRange(callExpression, document, matcherForBlock.isShouldAddNewline()),
      group, // FoldingGroup.newGroup("Block comment " + comment.getTextRange().toString()),
      placeholderText
    );
  }

  /**
   * Finds the immediate next sibling of the given {@code callExpression} that is considered to be a supported block.
   * <p>
   * When searching for such blocks, whitespace blocks are ignored.
   *
   * @param callExpression the call expression whose next sibling to find
   *
   * @return the next {@code JSCallExpression} considered to be a supported block, or {@code null} if there isn't one
   */
  @Nullable
  private JSCallExpression findImmediateNextSupportedBlockSibling(@NotNull JSCallExpression callExpression) {
    for(PsiElement sibling = callExpression.getParent().getNextSibling(); sibling != null; sibling = sibling.getNextSibling()) {
      if(sibling instanceof PsiWhiteSpace) {
        continue; // ignore whitespace
      }

      if(!(sibling instanceof JSExpressionStatement)) {
        return null;
      }

      JSExpression expression = ((JSExpressionStatement) sibling).getExpression();

      if(isSupportedBlock(expression)) {
        return (JSCallExpression) expression;
      }
    }

    return null;
  }

  /**
   * Calculates the `TextRange` to use for folding the given `callExpression`.
   *
   * @param callExpression the `JSCallExpression` who's `TextRange` to calculate
   * @param document       the `document` the `callExpression` is in
   * @param shouldAddNewline whether to add a newline between folding blocks
   *
   * @return the `TextRange` to fold over
   */
  private TextRange calculateFoldingTextRange(@NotNull JSCallExpression callExpression,
                                              @NotNull Document document,
                                              boolean shouldAddNewline) {
    TextRange range = callExpression.getParent().getTextRange();

    JSCallExpression nextTestBlockSibling = findImmediateNextSupportedBlockSibling(callExpression);

    if(nextTestBlockSibling == null) {
      return range;
    }

    // If the next block is requiring a newline, this block should behave as if it has addNewline enabled
    BlockMatcher matcherForBlock = findMatcherForBlock(nextTestBlockSibling);
    boolean shouldAddLineBefore = false;

    if (matcherForBlock != null && matcherForBlock.isShouldAddNewline()) {
      shouldAddLineBefore = true;
    }

    JSExpressionStatement nextTestBlockExpression = (JSExpressionStatement) nextTestBlockSibling.getParent();

    // line number that the *start* of the next test block is on
    int nextTestBlockStartOffset = nextTestBlockExpression.getTextRange().getStartOffset();
    int nextTestBlockStartLineNum = document.getLineNumber(nextTestBlockStartOffset);

    // offset of the start of the line before the start of the next test block
    int newlineOffset = (shouldAddNewline || shouldAddLineBefore) ? 2 : 1;
    int offsetOfLineBeforeNextTestBlock = document.getLineStartOffset(nextTestBlockStartLineNum - newlineOffset);

    if(range.getEndOffset() > offsetOfLineBeforeNextTestBlock) {
      offsetOfLineBeforeNextTestBlock = range.getEndOffset();
    }

    return new TextRange(
      range.getStartOffset(),
      offsetOfLineBeforeNextTestBlock
    );
  }

  /**
   * Builds the placeholder text to use when folding the given {@code callExpression}.
   *
   * @param callExpression the expression being folded
   *
   * @return the placeholder text to use when folding the given {@code callExpression}
   */
  @NotNull
  private String buildPlaceholderText(@NotNull JSCallExpression callExpression) {
    JSExpression[] callExpressionArguments = callExpression.getArguments();

    if(callExpressionArguments.length == 0) {
      return "<UNABLE TO BUILD PLACEHOLDER TEXT>";
    }

    if(!(callExpressionArguments[0] instanceof JSLiteralExpression)) {
      return callExpressionArguments[0].getText();
    }

    JSLiteralExpression firstCallArgument = ((JSLiteralExpression) callExpressionArguments[0]);
    String stringValue = firstCallArgument.getStringValue();

    if(stringValue != null) {
      return stringValue;
    }

    String textValue = firstCallArgument.getText();

    if(firstCallArgument.isQuotedLiteral()) {
      return textValue.substring(1, textValue.length() - 1);
    }

    return textValue;
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    return null;
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    /*
      if "collapse by default setting" is false
        then return false
     */

    PsiElement psiElement = node.getPsi();

    if(psiElement instanceof JSCallExpression) {
      return shouldFoldBlock((JSCallExpression) psiElement);
    }

    return true;
  }
}
