package org.manaty.gitbackup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.BasicAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class OrganisationBackupManager {
    private static Logger log = LoggerFactory.getLogger("OrganisationBackupManager");
    private Organisation organisation;
    private Path backupPath;

    public OrganisationBackupManager(Organisation organisation, Path backupPath){
        this.organisation=organisation;
        this.backupPath=backupPath;
    }

    public void retrieveRepoUrls(){
        List<String> repoUrls= new ArrayList<>();
        Client client = ClientBuilder.newClient();
        ResteasyWebTarget resteasyWebTarget = (ResteasyWebTarget)client.target("https://api.github.com/orgs/"+organisation.getName()+"/repos");
        if(organisation.getUsername()!=null && organisation.getPassword()!=null)
        resteasyWebTarget.register(new BasicAuthentication(organisation.getUsername(), organisation.getPassword()));
        Response response = resteasyWebTarget.request().get();
        String value = response.readEntity(String.class);
        response.close();
        client.close();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode parent = null;
        try {
            parent = mapper.readTree(value);
            for (Iterator<JsonNode> i = parent.iterator(); i.hasNext(); ) {
                JsonNode repo  = i.next();
                String url = repo.get("clone_url").asText();
                if(organisation.getGitIgnoredUrls()==null || !organisation.getGitIgnoredUrls().contains(url)) {
                    log.info("add repo url: {}", url);
                    repoUrls.add(url);
                } else {
                    log.info("repo url ignored: {}", url);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        organisation.setGitRepoUrls(repoUrls);
    }

    public void backup(){
        log.info("Backing up {}",organisation.getName());
        if(organisation.getGitRepoUrls()==null){
            retrieveRepoUrls();
        }
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
            Path repoPath = backupPath.resolve(organisation.getName());

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
