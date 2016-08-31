
-- Procedimientos almacenados
delimiter //

create procedure addUser(in _nick varchar(20), in _passw varchar(40))
begin
	insert into user values(null, _nick, (select AES_ENCRYPT(_passw, (select getAesKey()))), 1);
end//


create procedure getUsers()
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) from user;
end//


create procedure getEnabledUsers()
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) from user where enabled = 1;
end//


create procedure getDisabledUsers()
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) from user where enabled = 0;
end//

create procedure getUser(in _id bigint unsigned)
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) from user where id = _id;
end//


create procedure getUserByNick(in _nick varchar(20))
begin
	select id, nick, (select AES_DECRYPT(code, (select getAesKey()))) from user where nick = _nick;
end//


create procedure addAccount(_idUser bigint unsigned, _rootDirName varchar(40), _usedSpace bigint unsigned,
	_totalSpace bigint unsigned, _creationDate datetime)
begin
	insert into account values(null, _idUser, _rootDirName, _usedSpace, _totalSpace, _creationDate);
end//


create procedure addFile(in _fileSize, in _idAccount bigint unsigned)
begin
	update account set usedSpace = usedSpace+_fileSize where id = _idAccount;
end//


create procedure removeFile(in _fileSize, in _idAccount bigint unsigned)
begin
	update account set usedSpace = usedSpace-_fileSize where id = _idAccount;
end//


create procedure getAccounts()
begin
	select * from account;
end//


create procedure getAccount(in _id bigint unsigned)
begin
	select * from account where id = _id;
end//


create procedure getAccountsByUser(in _idUser bigint unsigned)
begin
	select * from account where idUser = _idUser;
end//


create procedure removeAccount(in _idAccount)
begin
	update account set enabled = 0 where id = _idAccount;
end//


create procedure removeAccountByUser(in _idUser)
begin
	update account set enabled = 0 where idUser = _idUser;
end//


create procedure removeAllAccounts()
begin
	update account set enabled = 0 where enabled = 1;
end//


create procedure removeUser(in _idUser)
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



-- Procedimientos almacenados
delimiter ;
