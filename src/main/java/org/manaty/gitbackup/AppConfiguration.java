package org.manaty.gitbackup;

import java.util.List;

public class AppConfiguration {
    private String backupDestinationPath;
    private List<Organisation> organisations;

    public String getBackupDestinationPath() {
        return backupDestinationPath;
    }

    public void setBackupDestinationPath(String backupDestinationPath) {
        this.backupDestinationPath = backupDestinationPath;
    }

    public List<Organisation> getOrganisations() {
        return organisations;
    }

    public void setOrganisations(List<Organisation> organisations) {
        this.organisations = organisations;
    }
}
