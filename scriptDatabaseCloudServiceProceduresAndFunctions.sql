delimiter //

create function isValidRegistration(_nick varchar(20)) returns bit
begin
	return (select count(id) from user where nick = _nick) = 0;
end//

create function isValidRegistration(_nick varchar(20)) returns bit
begin
	return (select count(id) from user where nick = _nick) > 0;
end//

/*
Retornar ID del ultimo:
	select id from user order by id desc limit 1; 
*/

create procedure addUser(in _nick varchar(20), in _code varchar(20), in _cloud);

create function addUserAndReturnID(_nick varchar(20), _passw varchar(20), _cloud bigint) returns bigint
begin
	declare lastId bigint set default as (select id from user order by id desc limit 1);
	insert into user values((lastId+1));
end//


create function getKey() returns varchar(8)
begin
	return 'power7xd';
end//


-- Users count
create function getUsersCount() returns bigint
begin
	return (select count(id) from user);
end//


create procedure addUser(in _nick varchar(20), in _code varchar(), in _idCloud)
begin
	insert into user values(null, );
end//


-- getUser --> id
create procedure getUserById(in _id bigint unsigned)
begin
	select * from user where id = _id;
end//


-- getUser --> nick
create procedure getUserByNick(in _nick varchar(20))
begin
	select * from user where nick = _nick;
end//


-- getUsers
create procedure getUsers()
begin
	select * from user;
end//


-- removeUser --> id
create procedure removeUserById(in _id bigint unsigned)
begin
	update user set enabled = 0 where id = _id;
end//

delimiter ;


-- removeUser --> nick
create procedure removeUserByNick(in _nick varchar(20))
begin
	update user set enabled = 0 where nick = _nick;
end//


-- removeAllUsers
create procedure removeAllUsers()
begin
	update user set enabled = 0;
end//


-- addRootDirectory
create procedure addRootDir(in _name varchar(50))
begin
	insert into rootDirectory values(null, _name);
end//


-- getRootDirectory
create procedure getRootDir(in _id bigint unsigned)
begin
	select * from rootDirectory where id = _id;
end//


-- getRootDirectories
create procedures getRootDirs()
begin
	select * from rootDirectory;
end//


--addCloudInfo
create procedure addCloudInfo(in _usedSpace bigint unsigned, in _totalSpace bigint unsigned, in _creationDate datetime)
begin
	insert into cloudInfo values(null, _usedSpace, _totalSpace, _creationDate);
end//


--getCloudInfo --> id
create procedure getCloudInfo(in _id bigint unsigned)
begin
	select * from cloudInfo where id = _id;
end//


