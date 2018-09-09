use web;

drop table if exists `user`;

create table `user`(
  `id` int unsigned auto_increment,
  `account` varchar(50) not null,
  `nickname` varchar(50) not null default '',
  `phone` varchar(20) not null default '',
  `email` varchar(50) not null default '',
  `password` char(32) not null,
  `create_time` datetime not null,
  primary key (id),
  unique (account)
) engine = innodb default character set utf8mb4 collate utf8mb4_bin;