package jones.foldcallblocks.config;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ConfigForm implements SearchableConfigurable {
  @Nullable
  private BlockMatchersEditor blockMatchersEditor;

  @NotNull
  private Project project;

  public ConfigForm(@NotNull Project project) {
    this.project = project;
  }

  @NotNull
  @Override
  public String getId() {
    return "jones.foldcallblocks.fold-call-blocks";
  }

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Fold Call Blocks";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    if(blockMatchersEditor == null) {
      blockMatchersEditor = new BlockMatchersEditor(project);
    }

    return blockMatchersEditor.getComponent();
  }

  @Override
  public boolean isModified() {
    return blockMatchersEditor != null && blockMatchersEditor.isModified();
  }

  @Override
  public void reset() {
    if(blockMatchersEditor != null) {
      blockMatchersEditor.reset();
    }
  }

  @Override
  public void apply() {
    if(blockMatchersEditor != null) {
      blockMatchersEditor.apply();
    }
  }
}
