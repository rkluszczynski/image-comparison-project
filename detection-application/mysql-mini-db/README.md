
1. Database local setup
=======================

- Download ZIP file from:

http://sourceforge.net/projects/miniserver/files/MiniServer/MiniServer_%20MySQL%205.0.67%20Portable/

- Unzip it for example to folder: D:\PortableApps\MiniServer-MySQL-5.0.67-Portable
- Enter that folder
- Run command: mysql_start.bat

There will be run additional virtual console for mysql commands.


2. Setup database
=================

- To enter MySQL database, please enter command:
```
Z:\bin> mysql --user=root --password=root
```

- Create database and a user for that database:
```
mysql> CREATE DATABASE picardio_db;
Query OK, 1 row affected (0.00 sec)

mysql> GRANT ALL PRIVILEGES ON picardio_db.* TO 'pica'@'localhost' IDENTIFIED BY 'rdi0';
Query OK, 0 rows affected (0.00 sec)
```


3. Initialize tables and data
=============================

- First set environment variable for directory with SQL files:
```
Z:\bin> set SQLSDIR=D:\WorkPlace\image-detection-project\detection-application\src\test\resources\mysql-test-scripts
```

- Then for every file enter command to crate tables and insert example data:
```
Z:\bin> mysql --user=pica --password=rdi0 picardio_db < %SQLSDIR%\create-tables.sql
Z:\bin> mysql --user=pica --password=rdi0 picardio_db < %SQLSDIR%\insert-data.sql
```

