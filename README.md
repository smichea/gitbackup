# gitbackup
backup all organisation git repo on a USB key to be able to recover from a nasty virus.

In case a nasty virus spread on major OS and decide to `rm -rf` all volumes on let say.. first on next month,
you'd better have a copy of your git repositories (as they store all intangible the production of your organisation)
on a removable storage.

## get started
[download gitbackup jar](https://github.com/smichea/gitbackup/raw/master/target/gitbackup-1.0-SNAPSHOT-jar-with-dependencies.jar) on a removable storage, e.g. a USB key and rename gitbackup.jar.
Plug your PC in a device that has java installed (v11+) and execute the file gitbackup.jar either by double clicking it or by running from the USB root directory
```
java -jar gitbackup.jar
```

### configure your repositories
If gitbackup run without a configuration file in the directory it is run from, for instance the first time you execute it
then it will create a default `gitbackup_config.yml` file that you fill with the list of repo you want to backup.

```
---
backupDestinationPath: "E:\\Documents\\developpement\\webdrone\\gitbackup"
organisations:
- name: "manaty"
  token: "my github tocken"
  username: null
  password: null
  gitRepoUrls: null
- name: "meveo"
  token: null
  username: "smichea"
  password: "Mypassword"
  gitRepoUrls:
  - "https://github.com/meveo-org/mv-calendar.git"
  - "https://github.com/meveo-org/mv-template.git"
```
