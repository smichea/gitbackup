package org.manaty.gitbackup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Backup {

    private static Logger log = LoggerFactory.getLogger("Backup");


    private static final String CONFIG_FILENAME = "./gitbackup_config.yml";

    private static AppConfiguration config;

    private static void loadConfiguration(){
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        Path configPath = Paths.get(CONFIG_FILENAME);
        if(Files.notExists(configPath)){
            log.error("cannot find configuration file: {} in {}",CONFIG_FILENAME,Paths.get("").toAbsolutePath());
            AppConfiguration c = new AppConfiguration();
            c.setBackupDestinationPath(Paths.get("").toAbsolutePath().toString());
            List<Organisation> orgs = new ArrayList<>();
            Organisation org=new Organisation();
            org.setName("manaty");
            org.setUsername("myusername");
            org.setPassword("mypassword");
            List<String> ignoredrepos=new ArrayList<>();
            ignoredrepos.add("https://github.com/meveo-org/mv-calendar.git");
            org.setGitIgnoredUrls(ignoredrepos);
            orgs.add(org);
            org=new Organisation();
            org.setName("meveo");
            org.setToken("mytocken");
            List<String> repos=new ArrayList<>();
            repos.add("https://github.com/meveo-org/mv-calendar.git");
            repos.add("https://github.com/meveo-org/mv-template.git");
            org.setGitRepoUrls(repos);
            orgs.add(org);
            c.setOrganisations(orgs);
            try {
                BufferedWriter out = Files.newBufferedWriter(configPath);
                om.writeValue(out,c);
                log.error("we created a dummy configuration file, please edit it.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(-1);
        }
        if(!Files.isRegularFile(configPath)){
            log.error("config file {} is not a regular file",configPath.toAbsolutePath());
            System.exit(-1);
        }
        if(!Files.isReadable(configPath)){
            log.error("config file {} is not a readable",configPath.toAbsolutePath());
            System.exit(-1);
        }
        try {
            config=om.readValue(configPath.toFile(),AppConfiguration.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static Path getBackupPath(){
        Path backupPath = Paths.get(config.getBackupDestinationPath());
        if(Files.notExists(backupPath)) {
            log.error("cannot find backup directory: {} ", backupPath.toAbsolutePath());
        }
        if(!Files.isDirectory(backupPath)){
            log.error("{} is not a directory",backupPath.toAbsolutePath());
            System.exit(-1);
        }
        if(!Files.isWritable(backupPath)){
            log.error("backup directory {} is not writeable",backupPath.toAbsolutePath());
            System.exit(-1);
        }
        return backupPath;
    }


    public static void main(String[] args) {
        BasicConfigurator.configure();
        loadConfiguration();
        Path backupPath = getBackupPath();
        log.info("Preparing to backup organisations {} on {}",
                config.getOrganisations().stream().map(o->o.getName()).collect(Collectors.joining(",")),
                backupPath.toAbsolutePath());
        config.getOrganisations().forEach(org -> { (new OrganisationBackupManager(org,backupPath)).backup();});
    }

}
