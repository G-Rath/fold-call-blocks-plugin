package jones.foldcallblocks;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SimpleModificationTracker;
import jones.foldcallblocks.config.BlockMatcher;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(name = "BlockMatcherManagerConfiguration", storages = @Storage("block-folding-matchers.xml"))
public class BlockMatchersManager extends SimpleModificationTracker implements PersistentStateComponent<BlockMatchersManager> {
  private List<BlockMatcher> blockMatchers = buildDefaultMatchers();

  private static List<BlockMatcher> buildDefaultMatchers() {
    return (
      Stream.of(
        Stream.of(
          "describe",
          "test",
          "it"
        ).flatMap(s -> Stream.of(
          s,
          s.concat(".skip"),
          s.concat(".only")
        )),
        Stream.of(
          "xdescribe",
          "fdescribe",
          "xtest",
          "xit",
          "fit"
        )
      )
            .flatMap(o -> o)
            .flatMap(s -> Stream.of(s, s.concat(".each")))
            .map(block -> new BlockMatcher(block, !block.contains("describe")))
            .collect(Collectors.toList())
    );
  }

  public static BlockMatchersManager getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, BlockMatchersManager.class);
  }

  @Override
  public BlockMatchersManager getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull BlockMatchersManager state) {
    blockMatchers = state.blockMatchers;
  }

  public List<BlockMatcher> getBlockMatchers() {
    return blockMatchers;
  }

  public void setBlockMatchers(@NotNull List<BlockMatcher> blockMatchers) {
    this.blockMatchers = blockMatchers;
    incModificationCount();
  }

  public List<BlockMatcher> getRules() {
    return blockMatchers;
  }

  public void setRules(@NotNull List<BlockMatcher> rules) {
    this.blockMatchers = rules;
    incModificationCount();
  }
}
