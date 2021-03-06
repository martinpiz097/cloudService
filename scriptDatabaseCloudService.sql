delimiter ;
drop database if exists dbCloud;
create database dbCloud;
use dbCloud;

create table user(
	id bigint auto_increment not null primary key,
	nick varchar(20),
	code blob, -- Contraseña
	enabled bit
);


create table account(
	id bigint not null auto_increment primary key,
	idUser bigint not null,
	rootDirName varchar(255),
	usedSpace bigint unsigned,
	totalSpace bigint unsigned,
	creationDate datetime,
	enabled bit,
	foreign key(idUser) references user(id)
);
/*
create table userAccount(
	id bigint unsigned not null auto_increment primary key,
	idUser bigint unsigned not null,
	idAccount bigint unsigned not null,
	foreign key(idUser) references user(id),
	foreign key(idAccount) references account(id)
);
*/

create table command(
	id tinyint unsigned not null auto_increment primary key,
	order varchar(20),
	cantOptions tinyint unsigned not null,
	enabled bit not null
);


