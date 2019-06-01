package jones.foldtestblocks;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.folding.NamedFoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.ruby.ruby.lang.psi.RPsiElement;
import org.jetbrains.plugins.ruby.ruby.lang.psi.expressions.RLiteral;
import org.jetbrains.plugins.ruby.ruby.lang.psi.methodCall.RCall;

import java.util.Arrays;

// todo: should we be extending FoldingBuilderEx instead?
public class RubyTestBlockFoldingBuilder implements FoldingBuilder
{
  private static final String[] testBlockCommands = {
    "describe",
    "context",
    "it"
  };

  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document)
  {
    PsiElement nodePsi = node.getPsi();

    return (
      PsiTreeUtil.findChildrenOfType(nodePsi, RCall.class)
                 .stream()
                 .filter(this::isTestBlockRCall)
                 .filter(this::shouldFoldTestBlockRCall)
                 .map(this::createFoldingDescriptor)
                 .toArray(FoldingDescriptor[]::new)
    );
  }

  /**
   * Checks if the given {@code rCall} is for a "test block".
   * <p>
   * A "test block" r call is one whose command is in the list of {@code testBlockCommands}.
   *
   * @param rCall the r call to check
   *
   * @return {@code true} if the given {@code rCall} is for a "test block"; otherwise {@code false}
   */
  private boolean isTestBlockRCall(@NotNull RCall rCall)
  {
    return Arrays.asList(RubyTestBlockFoldingBuilder.testBlockCommands).contains(rCall.getCommand());
  }

  /**
   * Checks if the given {@code rCall} should be folded.
   * <p>
   * Test block call expressions should be folded unless they're at the top of the tree.
   *
   * @param rCall the call expression block to check
   *
   * @return {@code true} if the given {@code rCall} should be folded; otherwise {@code false}
   */
  private boolean shouldFoldTestBlockRCall(@NotNull RCall rCall)
  {
    /*
      if "fold top level blocks" is false
        then fold rCall if it's not a top most call expression

      > return foldTopLevelBlocks || !isTopMostCallExpression(rCall)
    */

    return true;
  }

  @NotNull
  private NamedFoldingDescriptor createFoldingDescriptor(@NotNull RCall rCall)
  {
    PsiElement expressionParent = rCall.getParent();

    return new NamedFoldingDescriptor(
      expressionParent.getNode(),
      expressionParent.getTextRange(),
      null,
      buildPlaceholderText(rCall)
    );
  }

  /**
   * Builds the placeholder text to use when folding the given {@code rCall}.
   *
   * @param rCall the RCall being folded
   *
   * @return the placeholder text to use when folding the given {@code rCall}
   */
  private String buildPlaceholderText(@NotNull RCall rCall)
  {
    RPsiElement firstCallArgument = rCall.getArguments().get(0);

    if(!(firstCallArgument instanceof RLiteral)) {
      return rCall.getText();
    }

    RLiteral rLiteral = (RLiteral) firstCallArgument;

    String stringValue = rLiteral.getContent();

    if(stringValue != null) {
      return stringValue;
    }

    String textValue = rLiteral.getText();

    return textValue.substring(1, rLiteral.getText().length() - 1);
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
    PsiElement psiElement = node.getPsi();

    if(psiElement instanceof RCall) {
      return shouldFoldTestBlockRCall((RCall) psiElement);
    }

    return true;
  }
}
