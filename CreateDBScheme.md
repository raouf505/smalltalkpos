Предполагается, что на компьютере установлен PostgreSQL версии не ниже **8.4.8-1**.

  1. Создать базу данных (заменить `dbname` на имя вашей базы):
    * **Windows**: `createdb.exe -U postgres dbname`
    * **Ubuntu**:
      1. `su postgres`
      1. `createdb dbname`
  1. Запустить образ и выполнить следующий код (заменить `ProjectSessionClass` на нужный подкласс класса `SmallPOSSeasideSession`):
    * `ProjectSessionClass new glorpSession recreateTables`

Если все прошло без ошибок, то будет создана база данных с именем `dbname`, а также все таблицы и связи.