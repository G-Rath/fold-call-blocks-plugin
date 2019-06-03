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
import org.jetbrains.plugins.ruby.ruby.lang.psi.iterators.RBlockCall;

import java.util.Arrays;

// todo: should we be extending FoldingBuilderEx instead?
public class RubyTestBlockFoldingBuilder implements FoldingBuilder {
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
                 .filter(this::isTestBlockCall)
                 .filter(this::shouldFoldTestBlockCall)
                 .map(blockCall -> createFoldingDescriptor(blockCall, document))
                 .toArray(FoldingDescriptor[]::new)
    );
  }

  /**
   * Checks if the given {@code blockCall} is for a "test block".
   * <p>
   * A "test block" call is one whose command is in the list of {@code testBlockCommands}.
   *
   * @param blockCall the block call to check
   *
   * @return {@code true} if the given {@code blockCall} is for a "test block"; otherwise {@code false}
   */
  private boolean isTestBlockCall(@NotNull RBlockCall blockCall) {
    return Arrays.asList(RubyTestBlockFoldingBuilder.testBlockCommands).contains(blockCall.getCommand());
  }

  /**
   * Checks if the given {@code blockCall} should be folded.
   * <p>
   * Test block call expressions should be folded unless they're at the top of the tree.
   *
   * @param blockCall the call expression block to check
   *
   * @return {@code true} if the given {@code blockCall} should be folded; otherwise {@code false}
   */
  private boolean shouldFoldTestBlockCall(@NotNull RBlockCall blockCall) {
    /*
      if "fold top level blocks" is false
        then fold blockCall if it's not a top most call expression

      > return foldTopLevelBlocks || !isTopMostCallExpression(blockCall)
    */

    return true;
  }

  @NotNull
  private NamedFoldingDescriptor createFoldingDescriptor(@NotNull RBlockCall blockCall, @NotNull Document document) {
    return new NamedFoldingDescriptor(
      blockCall.getNode(),
      blockCall.getTextRange(),
      null,
      buildPlaceholderText(blockCall)
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
      return shouldFoldTestBlockCall((RBlockCall) psiElement);
    }

    return true;
  }
}
