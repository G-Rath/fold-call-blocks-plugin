package jones.foldtestblocks;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.folding.NamedFoldingDescriptor;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.lang.javascript.psi.impl.JSCallExpressionImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

// todo: should we be extending FoldingBuilderEx instead?
public class JSTestBlockFoldingBuilder implements FoldingBuilder
{
  private static final Logger LOG = Logger.getInstance(JSTestBlockFoldingBuilder.class);
  private static final String[] testBlockNames = {
    "describe",
    "test",
    "it"
  };

  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document)
  {
    PsiElement nodePsi = node.getPsi();
//    PsiElement nodePsiNextSibling = nodePsi.getNextSibling();
//
//    if(nodePsiNextSibling instanceof JSCallExpressionImpl) {
//      JSCallExpressionImpl callExpression = (JSCallExpressionImpl) nodePsiNextSibling;
//
//      if(isTestBlockCallExpression(callExpression) && shouldFoldTestBlockCallExpression(callExpression)) {
//        return new NamedFoldingDescriptor[]{ createFoldingDescriptor(document, callExpression) };
//      }
//    }

    return (
      PsiTreeUtil.findChildrenOfType(nodePsi, JSCallExpressionImpl.class)
                 .stream()
                 .filter(this::isTestBlockCallExpression)
                 .filter(this::shouldFoldTestBlockCallExpression)
                 .map(callExpression -> createFoldingDescriptor(callExpression, document))
                 .toArray(FoldingDescriptor[]::new)
    );
  }

  /**
   * Checks if the given {@code expression} is a {@code CallExpression} for a "test block".
   * <p>
   * A "test block" call expression is one whose text is in the list of {@code testBlockNames}.
   *
   * @param expression the expression to check
   *
   * @return {@code true} if the given {@code expression} is for a "test block"; otherwise {@code false}
   */
  private boolean isTestBlockCallExpression(@Nullable JSExpression expression)
  {
    if(expression instanceof JSCallExpressionImpl) {
      return isTestBlockCallExpression((JSCallExpressionImpl) expression);
    }

    return false;
  }

  /**
   * Checks if the given {@code callExpression} is for a "test block".
   * <p>
   * A "test block" call expression is one whose text is in the list of {@code testBlockNames}.
   *
   * @param callExpression the call expression to check
   *
   * @return {@code true} if the given {@code callExpression} is for a "test block"; otherwise {@code false}
   */
  private boolean isTestBlockCallExpression(@NotNull JSCallExpressionImpl callExpression)
  {
    JSExpression methodExpression = callExpression.getMethodExpression();

    if(methodExpression == null) {
      return false;
    }

    return Arrays.asList(JSTestBlockFoldingBuilder.testBlockNames).contains(methodExpression.getText());
  }

  /**
   * Checks if the given {@code testBlockCallExp} should be folded.
   * <p>
   * Test block call expressions should be folded unless they're at the top of the tree.
   *
   * @param testBlockCallExp the call expression block to check
   *
   * @return {@code true} if the given {@code testBlockCallExp} should be folded; otherwise {@code false}
   */
  private boolean shouldFoldTestBlockCallExpression(@NotNull JSCallExpressionImpl testBlockCallExp)
  {
    /*
      if "fold top level blocks" is false
        then fold testBlockCallExp if it's not a top most call expression

      > return foldTopLevelBlocks || !isTopMostCallExpression(testBlockCallExp)
    */

    return !isTopMostCallExpression(testBlockCallExp);
  }

  /**
   * Checks if the given {@code callExpression} is th e top most call expression,
   * by checking if it's parents parent is an instance of {@code PsiFile}.
   *
   * @param callExpression the expression to check
   *
   * @return {@code true} if the given {@code testBlockCallExp} is the top most call expression; otherwise {@code false}
   */
  private boolean isTopMostCallExpression(@NotNull JSCallExpressionImpl callExpression)
  {
    return callExpression.getParent().getParent() instanceof PsiFile;
  }

  @NotNull
  private NamedFoldingDescriptor createFoldingDescriptor(@NotNull JSCallExpressionImpl callExpression, @NotNull Document document)
  {
    // JavaCodeFoldingSettings.getInstance().isCollapseEndOfLineComments()
    PsiElement expressionParent = callExpression.getParent();

    FoldingGroup group = FoldingGroup.newGroup("test-block");

    return new NamedFoldingDescriptor(
      expressionParent.getNode(),
      expressionParent.getTextRange(),
      group, // FoldingGroup.newGroup("Block comment " + comment.getTextRange().toString()),
      buildPlaceholderText(callExpression)
    );
  }

  /**
   * Builds the placeholder text to use when folding the given {@code callExpression}.
   *
   * @param callExpression the expression being folded
   *
   * @return the placeholder text to use when folding the given {@code callExpression}
   */
  private String buildPlaceholderText(@NotNull JSCallExpressionImpl callExpression)
  {
    JSLiteralExpression jsExpression = (
      Arrays.stream(callExpression.getArguments())
            .filter(expression -> expression instanceof JSLiteralExpression)
            .map(expression -> (JSLiteralExpression) expression)
            .findFirst()
            .orElse(null)
    );

    if(jsExpression == null) {
      return callExpression.getText();
    }

    String stringValue = jsExpression.getStringValue();

    if(stringValue != null) {
      return stringValue;
    }

    String textValue = jsExpression.getText();

    return textValue.substring(1, jsExpression.getText().length() - 1);
  }

  private int findOffsetOfLineForTextRange(@NotNull Document document, @NotNull TextRange range)
  {
    /*
    TextRange range = expressionParent.getTextRange();
    TextRange textRange = new TextRange(findOffsetOfLineForTextRange(document, range), range.getEndOffset());
     */
    return document.getLineStartOffset(document.getLineNumber(range.getStartOffset()));
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node)
  {
    return null;
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node)
  {
    /*
      if "collapse by default setting" is false
        then return false
     */

    PsiElement psiElement = node.getPsi();

    if(psiElement instanceof JSCallExpressionImpl) {
      return shouldFoldTestBlockCallExpression((JSCallExpressionImpl) psiElement);
    }

    return true;
  }
}
