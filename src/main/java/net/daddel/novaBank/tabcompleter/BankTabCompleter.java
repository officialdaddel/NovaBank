package net.daddel.novaBank.tabcompleter;

import net.daddel.novaBank.files.ConfigFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public class BankTabCompleter implements TabCompleter {

    private final ConfigFile configFile;

    private static final List<String> SUBCOMMANDS = List.of("balance", "deposit", "withdraw");

    public BankTabCompleter(ConfigFile configFile) {
        this.configFile = configFile;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return SUBCOMMANDS.stream().filter(sub -> sub.startsWith(input)).collect(Collectors.toList());
        }

        if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            List<String> suggestions = switch (subCommand) {
                case "deposit" -> configFile.getStringList("suggestions.deposit");
                case "withdraw" -> configFile.getStringList("suggestions.withdraw");
                default -> List.of();
            };

            String input = args[1].toLowerCase();
            return suggestions.stream()
                    .filter(s -> s.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}