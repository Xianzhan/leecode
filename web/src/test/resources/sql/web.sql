create table user
(
  id          int auto_increment primary key,
  account     varchar(20)            not null  comment '登录帐号',
  nickname    varchar(50) default '' null  comment '昵称',
  phone       char(11)               null  comment '手机号',
  email       varchar(50)            null  comment '邮箱',
  password    char(32)               not null  comment 'md5 加密',
  create_time date                   not null  comment '创建日期',
  constraint user_account_uindex
  unique (account)
)  comment '登录用户';