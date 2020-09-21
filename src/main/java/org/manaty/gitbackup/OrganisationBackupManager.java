package org.manaty.gitbackup;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class OrganisationBackupManager {
    private static Logger log = LoggerFactory.getLogger("OrganisationBackupManager");
    private Organisation organisation;
    private Path backupPath;

    public OrganisationBackupManager(Organisation organisation, Path backupPath){
        this.organisation=organisation;
        this.backupPath=backupPath;
    }


    public void backup(){
        log.info("Backing up {}",organisation.getName());
        if(organisation.getGitRepoUrls()!=null){
            organisation.getGitRepoUrls().forEach(repo -> this.backupRepo(repo));
        }
    }

    private void backupRepo(String repoUrl){
        try {
            CloneCommand cloneCommand = Git.cloneRepository()
                    .setURI(repoUrl);
            if(organisation.getPassword()!=null && organisation.getPassword().trim().length()>0){
                cloneCommand.setCredentialsProvider(
                        new UsernamePasswordCredentialsProvider(organisation.getUsername().trim(),
                                organisation.getPassword().trim()));
            }
            Path repoPath = backupPath;

            log.info("repoPath {} ,repoUrl {}",backupPath,repoUrl);
            if(repoUrl.indexOf("/")>0 && repoUrl.endsWith(".git")) {
                String gitRepoPathname = repoUrl.substring(repoUrl.lastIndexOf("/")+1,repoUrl.length()-4);
                log.info("gitRepoPathname {}",gitRepoPathname);
                repoPath=repoPath.resolve(gitRepoPathname);
            } else {
                String gitRepoPathname = repoUrl.substring(repoUrl.lastIndexOf("/")+1);
                log.info("gitRepoPathname {}",gitRepoPathname);
                repoPath=repoPath.resolve(gitRepoPathname);
            }
            log.info("Backing up {} to {}",repoUrl,repoPath);
            if(Files.exists(repoPath)){
                Files.walk(repoPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
            cloneCommand.setDirectory(repoPath.toFile())
                    .call();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }
}
