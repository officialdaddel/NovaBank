package net.daddel.novaBank.utilities.misc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateChecker {
   private final JavaPlugin plugin;
   private final int resourceId;

   public UpdateChecker(JavaPlugin plugin, int resourceId) {
      this.plugin = plugin;
      this.resourceId = resourceId;
   }

   public void getVersion(Consumer<String> consumer) {
      Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
         try {
            InputStream is = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~")).openStream();

            try {
               Scanner scann = new Scanner(is);

               try {
                  if (scann.hasNext()) {
                     consumer.accept(scann.next());
                  }
               } catch (Throwable var8) {
                  try {
                     scann.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }

                  throw var8;
               }

               scann.close();
            } catch (Throwable var9) {
               if (is != null) {
                  try {
                     is.close();
                  } catch (Throwable var6) {
                     var9.addSuppressed(var6);
                  }
               }

               throw var9;
            }

            if (is != null) {
               is.close();
            }
         } catch (IOException var10) {
            this.plugin.getLogger().severe("Error: Unable to check for updates: " + var10.getMessage());
         }

      });
   }
}
