
-- Procedimientos almacenados
delimiter //

create procedure addUser(in _nick varchar(20), in _passw varchar(40))
begin
	insert into user values(null, _nick, (select AES_ENCRYPT(_passw, (select getAesKey()))), 1);
end//

create procedure getUsers()
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) as code from user;
end//


create procedure getEnabledUsers()
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) as code from user where enabled = 1;
end//


create procedure getDisabledUsers()
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) as code from user where enabled = 0;
end//

create procedure getUser(in _id bigint unsigned)
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) as code from user where id = _id;
end//


create procedure getLastUser()
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) as code from user order by id desc limit 1;
end//


create procedure getUserByNick(in _nick varchar(20))
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) as code from user where nick = _nick;
end//


create procedure addAccount(_idUser bigint, _rootDirName varchar(40), _usedSpace bigint,
	_totalSpace bigint)
begin
	insert into account values(null, _idUser, _rootDirName, _usedSpace, _totalSpace, (select now()), 1);
end//


create procedure addFile(in _fileSize bigint, in _idAccount bigint)
begin
	update account set usedSpace = usedSpace+_fileSize where id = _idAccount;
end//


create procedure removeFile(in _fileSize bigint, in _idAccount bigint)
begin
	update account set usedSpace = usedSpace-_fileSize where id = _idAccount;
end//


create procedure getAccounts()
begin
	select * from account;
end//


create procedure getAccount(in _id bigint)
begin
	select * from account where id = _id;
end//


create procedure getAccountsByUser(in _idUser bigint)
begin
	select * from account where idUser = _idUser;
end//


create procedure removeAccount(in _idAccount bigint)
begin
	update account set enabled = 0 where id = _idAccount;
end//


create procedure removeAccountByUser(in _idUser bigint)
begin
	update account set enabled = 0 where idUser = _idUser;
end//


create procedure removeAllAccounts()
begin
	update account set enabled = 0 where enabled = 1;
end//


create procedure removeUser(in _idUser bigint)
begin
	update user set enabled = 0 where id = _idUser;
	call removeAccountByUser(_idUser);
end//


create procedure removeUserByNick(in _nick varchar(20))
begin
	update user set enabled = 0 where enabled = 1 and nick = _nick;
end//

create procedure removeAllUsers()
begin
	update user set enabled = 0 where enabled = 1;
end//


create procedure addCommand(in _order varchar(20), in _cantOptions tinyint unsigned)
begin
	insert into command values(null, _order, _cantOptions, 1);
end//

create procedure getCommands()
begin
	select * from command;
end//


create procedure getCommand(in _idCommand tinyint unsigned)
begin
	select * from command where id = _idCommand;
end//

create procedure removeCommand(in _idCommand tinyint unsigned)
begin
	update command set enabled = 0 where id = _idCommand;
end//

-- Procedimientos almacenados
delimiter ;
