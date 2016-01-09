### Script text ###
```
SET PGPASSWORD=%1%
SET BACKUPPATH=C:\SmallPOS.backups\
SET BACKUPFILE=%DATE:~6,4%-%DATE:~3,2%-%DATE:~0,2%

"C:\Program Files\PostgreSQL\9.0\bin\pg_dump.exe" --host localhost --port 5432 --username "postgres" --format custom --blobs --verbose --file "%BACKUPPATH%dongarant-%BACKUPFILE%.backup" "dongarant"
```

It's expected that you'll add (through the control panel or whichever) this script to be periodically executed.

NB: you should add password to your database as the command-line parameter to this script. I just don't like to show this password in plain text in backup script, where anybody can read it.