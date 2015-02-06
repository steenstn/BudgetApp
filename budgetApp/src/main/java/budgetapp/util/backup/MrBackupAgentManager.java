package budgetapp.util.backup;

import java.io.File;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

public class MrBackupAgentManager
    extends BackupAgentHelper {

    private static final String dbName = "budget.db";
    private static final String backupKey = "budgetBackup";

    @Override
    public void onCreate() {
        FileBackupHelper helper = new FileBackupHelper(this, dbName);
        addHelper(backupKey, helper);
    }

    @Override
    public File getFilesDir() {
        File path = getDatabasePath(dbName);
        return path.getParentFile();
    }
}
