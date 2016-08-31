
delimiter //
-- Funciones

create function getAesKey() returns varchar(8)
begin return 'powerxd7'; end//


create function isValidUser(_nick varchar(20), _passw varchar(40)) returns bit
begin
	--return (select count(id) from user where nick = _nick and (select AES_DECRYPT(code, (select getAesKey()))) = _passw) > 0;
	declare existsUser bit;
	set existsUser = (select count(id) from user where nick = _nick and (select AES_DECRYPT(code, (select getAesKey()))) = _passw) > 0;
	if existsUser = 0 then
		return existsUser;
	else
		return (select enabled from user where nick = _nick);
	end if;
end


create function isNickAvailable(_nick varchar(20)) returns bit
begin
	return (select count(id) from user where nick = _nick) = 0;
end//


create function getUsersCount() returns bigint unsigned
begin
	return (select count(id) from user);
end//


create function getAccountsCount() returns bigint unsigned
begin
	return (select count(id) from account);
end//


create function getUsedSpace(idAccount bigint unsigned) returns bigint
begin
	return (select usedSpace from account where id = idAccount);
end//

create function getTotalSpace(idAccount bigint unsigned) returns bigint
begin
	return (select totalSpace from account where id = idAccount);
end//


create function getFreeSpace(idAccount bigint unsigned) returns bigint
begin
	return (select getTotalSpace(idAccount)) - (select getUsedSpace(idAccount));
end//


create function hasAvailableSpace(fileSize bigint, idAccount bigint unsigned) returns bit
begin
	return (select getFreeSpace(idAccount)) >= fileSize;
end//

/*create function getAccountsCountByUser(_idUser bigint unsigned) returns bigint unsigned
begin
	return (select count(id) from userAccount where idUser = _idUser);
end//


create function getUsersCountByAccount(_idAccount bigint unsigned) returns bigint unsigned
begin
	return (select count(id) from userAccount where idAccount = _idAccount);
end//
*/

create function getAccountsCountByUser(_idUser bigint unsigned) returns bigint unsigned
begin
	return (select count(id) from account where idUser = _idUser);
end//
-- Funciones

delimiter ;