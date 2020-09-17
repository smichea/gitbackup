# gitbackup
backup all organisation git repo on a USB key to be able to recover from a nasty virus.

In case a nasty virus spread on major OS and decide to `rm -rf` all volumes on let say.. first on next month,
you'd better have a copy of your git repositories (as they store all intangible the production of your organisation)
on a removable storage.

## get started
download gitbackup for your system and execute it

### configure your organisation
gitbackup will backup all the git repo of all your organisations.

The organisation configuration can be stored in a configuration file.

In case you want to store your passwords in the configuration files, you will need to use a master password to use gitbackup.
This password will only be used to encrypt/decrypt the organisation passwords stored in the configuration file.

the configuration file itself could be stored on the removable storage
