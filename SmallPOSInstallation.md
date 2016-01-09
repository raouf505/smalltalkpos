# Ограничения #

  1. СУБД - PostgreSQL (протестировано с версией **8.4.8-1**, скачать которую для Windows можно [здесь](http://www.enterprisedb.com/products-services-training/pgdownload#windows)).
  1. СУБД и образ с системой устанавливаются на один и тот же компьютер.
  1. Smalltalk-код приводится для проекта **ПолиСтальГрупп**.

# Дано #

  1. Образ со SmallPOS'ом.
  1. Дамп базы данных (он же бэкап).
  1. PostgreSQL.

# Установка #

<ol>
<li>PostgreSQL: установка и восстановление базы</li>

<b>ВНИМАНИЕ. Для ПолиСтальГрупп база данных должна называться <code>pmgroup</code></b>.<br>
<br>
<h4>Windows</h4>

<ol><li>Установить PostgreSQL. В процессе установки все параметры оставить по умолчанию. В качестве пароля суперпользователя ввести <b>password</b>.<br>
</li><li>Открыть консоль и перейти в папку <code>C:\Program Files\PostgreSQL\8.4\bin</code>. Все консольные утилиты, используемые ниже, находятся в этой папке.<br>
</li><li>Создать базу данных (заменить <code>dbname</code> на имя вашей базы):<br>
<ul><li><code>createdb.exe -U postgres dbname</code>
</li></ul></li><li>Восстановить базу из дампа (путь указать в кавычках, если он содержит пробелы):<br>
<ul><li><code>pg_restore.exe -U postgres -d dbname "путь к дампу"</code></li></ul></li></ol>

<h4>Ubuntu</h4>

<ol><li>Установить PostgreSQL и задать суперпользователю пароль <b>password</b>. Существует масса инструкций по данной теме, поэтому не вижу смысла писать свою.<br>
</li><li>Открыть терминал с правами пользователя <code>postgres</code>:<br>
<ul><li><code>su postgres</code>
</li></ul></li><li>Создать базу данных (заменить <code>dbname</code> на имя вашей базы):<br>
<ul><li><code>createdb dbname</code>
</li></ul></li><li>Восстановить базу из дампа:<br>
<ul><li><code>pg_restore -d dbname путь_к_дампу</code></li></ul></li></ol>

<li>Настройка образа</li>

<ol><li>Скопировать и запустить образ со SmallPOS'ом.<br>
</li><li>В образе (см. раздел <b>Дополнительно</b>):<br>
<ul><li>Выполнить код: <code>SmallPOSInterface initRoot: PMGroupGeneral session: PMGroupSession</code>.<br>
</li><li>Обновить список шрифтов.<br>
</li><li>Сохранить сделанные изменения.<br>
</li></ul></li><li>Сделать так, чтобы образ загружался при включении компьютера.<br>
</ol></li></ol>

Если все было сделано правильно, то проект будет доступен по адресу: http://127.0.0.1:8080/seaside/SmallPOS.

# Перенос на другой компьютер #

<ol>
<li>Сделать бэкап базы данных на старом компьютере.</li>

<b>ВНИМАНИЕ. Для ПолиСтальГрупп база данных должна называться <code>pmgroup</code></b>.<br>
<br>
<h4>Windows</h4>

<ol><li>Открыть консоль и перейти в папку <code>C:\Program Files\PostgreSQL\8.4\bin</code>.<br>
</li><li>Выполнить команду, заменив <code>dump_name</code> и <code>dbname</code> на имя дампа и имя базы, соответственно:<br>
<ul><li><code>pg_dump.exe --host localhost --port 5432 --username postgres --format custom --blobs --file dump_name dbname</code></li></ul></li></ol>

В случае с Windows 7 может быть такое, что команда отработает без ошибок, но дамп в указанной папке так и не появится. Он будет создан в папке <code>C:\Users\username\AppData\Local\VirtualStore</code>. Поэтому, чтобы этого избежать необходимо запустить консоль с правами администратора и повторно выполнить команду.<br>
<br>
<h4>Ubuntu</h4>

<ol><li>Выполнить команду, заменив <code>dump_name</code> и <code>dbname</code> на имя дампа и имя базы, соответственно:<br>
<ul><li><code>pg_dump --host localhost --port 5432 --username postgres --format custom --blobs --file dump_name dbname</code></li></ul></li></ol>

<li>Выполнить все шаги раздела <b>Установка</b>.</li>
</ol>


---


# Дополнительно #

Всюду далее **World menu** - это меню, которое появится, если щелкнуть левой кнопкой мыши на сером фоне.

## Как выполнить Smalltalk-код ##

World menu -> Workspace -> скопировать код в появившееся поле -> выделить код и нажать правую кнопку мыши -> Do it

## Как обновить шрифты ##

Обновление шрифтов необходимо в том случае, когда вместо русскоязычного текста отображаются вопросики.

World menu -> System -> Settings -> Appearance -> Standard fonts -> нажать кнопку Launch напротив 'Install bitmap Dejavu fonts' -> нажать кнопку Launch напротив 'Update fonts from system'.

World menu -> Workspace -> попробовать напечатать русскоязычный текст. Если вместо букв все равно продолжают отображаться вопросики, то в настройках, в Standard fonts для Default выбрать шрифт Arial. Заново открыть Workspace и проверить.

## Как сохранить образ ##

World menu -> Save

## PostgreSQL: бэкап по расписанию ##

#### Windows ####

  1. Создать файл `pgpass.conf` и поместить его в защищенную директорию, доступную только суперпользователю (скорее всего этот файл появится после установки PostgreSQL в папке `...\AppData\Roaming\postgresql\`). О том какой формат должен иметь данный файл можно почитать [тут](http://www.postgresql.org/docs/9.0/static/libpq-pgpass.html).
  1. Создать `bat-`файл, содержащий следующий код:
```
# Если в пути к файлу встречается пробел, то путь необходимо 
# указать в двойных кавычках. В противном случае кавычки не нужны.
# ----------------------------------------------------------------
# Заменить <pgpass.conf> на полный путь к файлу pgpass.conf.
# Заменить <pg_dump.exe> на полный путь к утилите pg_dump.
# Заменить <backup_folder> на полный путь к защищенной папке, 
# в которой будут храниться бэкапы.
# Заменить dbname на название вашей базы данных.

@echo off
  SET PGPASSFILE=<pgpass.conf>

  For /f "tokens=1-3 delims=/." %%a in ('date /t') do (set mydate=%%a_%%b_%%c)
  set mydate=%mydate: =%

  set BACKUP_FILE=%mydate%.backup

  <pg_dump.exe> --host localhost --port 5432 --username postgres --format custom --blobs --no-password --file <backup_folder>\dbname_%BACKUP_FILE% dbname
```
  1. Выполнить в консоли (путь указать в кавычках, если он содержит пробелы):
    * `at 01:00 /every:M,T,W,Th,F "полный путь к bat-файлу"`

#### Ubuntu ####

  1. Создать скрипт с правами `700` (можно и не так жестоко в принципе), содержащий следующий код:
```
#! /bin/sh

# Заменить <backup_folder> на полный путь к защищенной папке, 
# в которой будут храниться бэкапы.

export PGPASSWORD=password
dbname=$1
dumpdate=`date +%d_%m_20%y`
dash="_"

/usr/bin/pg_dump --host localhost --username postgres --format custom --blobs --file "<backup_folder>/$dbname$dash$dumpdate.backup" $dbname
```
  1. `sudo crontab -u root -e`
  1. Добавить строку (заменить `dbname` на имя вашей базы):
    * `0 1 * * 1-5 полный_путь_к_скрипту dbname`

Таким образом, каждую неделю с понедельника по пятницу в час ночи будет выполняться наш скрипт. В результате будут появляться бэкапы с названиями типа **`mybase_05_09_2011`**.