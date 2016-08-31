
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


create procedure addAccount(_idUser bigint unsigned, _rootDirName varchar(40), _usedSpace bigint unsigned,
	_totalSpace bigint unsigned, _creationDate datetime)
begin
	insert into account values(null, _idUser, _rootDirName, _usedSpace, _totalSpace, _creationDate);
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
-- Procedimientos almacenados

delimiter ;
