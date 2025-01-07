package ProjetLatest2;
import java.util.*;

abstract class Command {
    public abstract void execute(Player player, Board board, Game game);

    public static List<Command> allCommands = Arrays.asList(
        new ExpandCommand(),
        new ExploreCommand(),
        new ExterminateCommand()
    );
}

