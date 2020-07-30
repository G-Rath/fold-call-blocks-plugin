package jones.foldcallblocks.config;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.CollectionItemEditor;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.table.TableModelEditor;
import jones.foldcallblocks.BlockMatchersManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BlockMatchersEditor {
  private static final TableModelEditor.EditableColumnInfo<BlockMatcher, String> COLUMN_INFO_BLOCK_IDENTIFIER = new TableModelEditor.EditableColumnInfo<BlockMatcher, String>(
    "Block Identifier"
  ) {
    @Override
    public Class getColumnClass() {
      return String.class;
    }

    @Override
    public String valueOf(BlockMatcher rule) {
      return rule.getBlockIdentifier();
    }

    @Override
    public void setValue(BlockMatcher rule, String identifier) {
      rule.setBlockIdentifier(identifier);
    }
  };
  private static final TableModelEditor.EditableColumnInfo<BlockMatcher, Boolean> COLUMN_INFO_SHOULD_PREFIX = new TableModelEditor.EditableColumnInfo<BlockMatcher, Boolean>(
    "Prefix Fold Text With Identifier?"
  ) {
    @Override
    public Class getColumnClass() {
      return Boolean.class;
    }

    @Override
    public Boolean valueOf(BlockMatcher rule) {
      return rule.isPrefixWhenFolding();
    }

    @Override
    public void setValue(BlockMatcher rule, Boolean value) {
      rule.setPrefixWhenFolding(value);
    }
  };
  private static final TableModelEditor.EditableColumnInfo<BlockMatcher, Boolean> COLUMN_INFO_NEW_LINE = new TableModelEditor.EditableColumnInfo<BlockMatcher, Boolean>(
    "Add newline between foldings?"
  ) {
    @Override
    public Class getColumnClass() {
      return Boolean.class;
    }

    @Override
    public Boolean valueOf(BlockMatcher rule) {
      return rule.isShouldAddNewline();
    }

    @Override
    public void setValue(BlockMatcher rule, Boolean value) {
      rule.setShouldAddNewline(value);
    }
  };
  private static final ColumnInfo[] COLUMNS = { COLUMN_INFO_BLOCK_IDENTIFIER, COLUMN_INFO_SHOULD_PREFIX, COLUMN_INFO_NEW_LINE };

  private TableModelEditor<BlockMatcher> blockMatchersEditor;
  private JComponent blockMatchersTable;

  private JPanel root;

  @NotNull
  private Project project;

  public BlockMatchersEditor(@NotNull Project project) {
    this.project = project;
  }

  private void createUIComponents() {
    CollectionItemEditor<BlockMatcher> ruleEditor = new CollectionItemEditor<BlockMatcher>() {
      @NotNull
      @Override
      public Class<BlockMatcher> getItemClass() {
        return BlockMatcher.class;
      }

      @Override
      public BlockMatcher clone(@NotNull BlockMatcher item, boolean forInPlaceEditing) {
        return new BlockMatcher(item);
      }
    };

    blockMatchersEditor = new TableModelEditor<>(COLUMNS, ruleEditor, "No block matchers configured");
    blockMatchersTable = blockMatchersEditor.createComponent();
  }

  @NotNull
  public JPanel getComponent() {
    return root;
  }

  public boolean isModified() {
    return blockMatchersEditor.isModified();
  }

  private BlockMatchersManager getBlockMatchersManager() {
    return BlockMatchersManager.getInstance(project);
  }

  public void reset() {
    blockMatchersEditor.reset(getBlockMatchersManager().getRules());
  }

  public void apply() {
    getBlockMatchersManager().setRules(blockMatchersEditor.apply());
  }
}
