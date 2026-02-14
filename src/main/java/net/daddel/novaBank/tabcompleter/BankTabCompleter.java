package net.daddel.novaBank.tabcompleter;

import net.daddel.novaBank.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankTabCompleter implements TabCompleter {
    private final Utilities utilities;
    public BankTabCompleter(Utilities utilities) {
        this.utilities = utilities;
    }

    private static final List<String> SUBCOMMANDS = Arrays.asList("balance", "deposit", "withdraw");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (!utilities.isAdmin(player)) {
            return List.of();
        }

        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            String input = args[0].toLowerCase();

            for (String sub : SUBCOMMANDS) {
                if (sub.startsWith(input)) {
                    result.add(sub);
                }
            }
            return result;
        }

        return List.of();
    }
}