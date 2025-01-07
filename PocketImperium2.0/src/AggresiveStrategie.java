package ProjetLatest2;

class AggressiveStrategy implements Strategy {
    
    @Override
    public void decideCommandOrder(Player player, Game game) {
        System.out.println("AI is using an Aggressive Strategy.");
    
        player.getCommands().clear();

        // Add commands in aggressive order: Exterminate -> Expand -> Explore
        player.getCommands().add(new ExterminateCommand());
        player.getCommands().add(new ExpandCommand());
        player.getCommands().add(new ExploreCommand());

        System.out.println("Command order decided: Exterminate -> Expand -> Explore");

    }

    public void execute(Player player, Game game) {
        for (Command command : player.getCommands()) {
            command.execute(player, game);  // Execute the command
        }
    }
}