# gitbackup
backup all organisation git repo on a USB key to be able to recover from a nasty virus.

In case a nasty virus spread on major OS and decide to `rm -rf` all volumes on let say.. first on next month,
you'd better have a copy of your git repositories (as they store all intangible the production of your organisation)
on a removable storage.

## Get started

### Prepare a storage device 

In a pc running latest java runtime.

Format a removable storage device, e.g. a USB key or a sd card.

[download gitbackup zip file](https://github.com/smichea/gitbackup/releases/tag/1.0.0) and unzip it on a removable storage.

execute `gitbackup.bat` (if you are on windows) or in a command line execute 
```
java -jar gitbackup-1.0.jar
```
this will create a `gitbackup_config.yml` template configuration file

### configure your repositories
When gitbackup run without a configuration file in the directory it is run from,  
it will create a default `gitbackup_config.yml` file that you fill with the list of repo you want to backup.

Since for now there is no much security precaution taken in the code, you should either delete this configuration file from the storage after the backup or at least remove the values of the passwords in it.

For each organisation (whether a real github organisation or just one not related to github you can list explecitely the urls of the git repo you want to clone and the ones you want to ignore)
if the list of repo to clone is null, then gitbackup will assume you want to clone all the git repositories of a github organisation.


### Example

```
---
backupDestinationPath: "D:\\"
organisations:
- name: "manaty"
  token: null
  username: "myUsername"
  password: "myPassword"
  gitRepoUrls: null
  gitIgnoredUrls:
    - "https://github.com/manaty/MixedRealityToolkit.git"
    - "https://github.com/manaty/RestComm.git"
- name: "meveo"
  token: null
  username: "myUsername"
  password: "Mypassword"
  gitRepoUrls:
  - "https://github.com/meveo-org/mv-calendar.git"
  - "https://github.com/meveo-org/mv-template.git"
```

in this example we will clone all the repositories of the manaty organisation excluding 2 of them
and we will clone 2 repositories oif the "meveo" organisation by providing their explicit urls (hence it doesnt matter if they are on github or somewhere else)

### Execute the backup
Once you configuration file is complete, either double click on  `gitbackup.bat` (if you are on windows) or in a command line execute
```
java -jar gitbackup-1.0.jar
```
as soon as the backup starts you can use another window and delete the passwords in the configuration files
