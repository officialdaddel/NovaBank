package net.daddel.novaBank.manager;

import net.daddel.novaBank.files.BankDataFile;

import java.util.UUID;

public class BankFileManager {
    private final BankDataFile bankDataFile;

    public BankFileManager(BankDataFile bankDataFile) {
        this.bankDataFile = bankDataFile;
    }

    public void setAmount(UUID uuid, int amount) {
        bankDataFile.setInteger(uuid.toString(), amount);
    }

    public void removeAmount(UUID uuid, int amount) {
        int oldAmount = bankDataFile.getInt(uuid.toString());
        int newAmount = oldAmount - amount;

        bankDataFile.setInteger(uuid.toString(), newAmount);
    }

    public int getAmount(UUID uuid) {
        return bankDataFile.getInt(uuid.toString());
    }

    public void resetMoney(UUID uuid){
        bankDataFile.setInteger(uuid.toString(), 0);
    }
}
