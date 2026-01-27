package net.daddel.novaBank.tabcompleter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class BankAdminTabcompleter implements TabCompleter {
   List<String> arg = new ArrayList();

   public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
      if (this.arg.isEmpty()) {
         this.arg.add("reset");
         this.arg.add("set");
         this.arg.add("add");
         this.arg.add("remove");
         this.arg.add("reload");
      }

      List<String> result = new ArrayList();
      if (args.length == 1) {
         Iterator var6 = this.arg.iterator();

         while(var6.hasNext()) {
            String a = (String)var6.next();
            if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
               result.add(a);
            }
         }

         return result;
      } else {
         return null;
      }
   }
}
