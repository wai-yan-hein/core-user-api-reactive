alter table appuser
rename to  app_user;
alter table company_info
change column address comp_address text null default null after comp_email,
change column currency cur_code varchar(15) not null after short_code,
change column name comp_name text null default null ,
change column phone comp_phone text null default null ,
change column email comp_email text null default null ;

alter table sys_prop
rename to  system_property;

alter table role_prop
rename to  role_property;

alter table mac_prop
rename to  machine_property;

alter table role
rename to  app_role;

alter table app_user
change column user_name user_long_name varchar(255) not null ;


alter table currency
change column cur_name currency_name varchar(255) null default null ,
change column cur_symbol currency_symbol varchar(255) null default null ;

set sql_safe_updates =0;
delete from seq_table where option='menu';

alter table seq_table
drop column period,
change column option option varchar(15) not null first,
drop primary key,
add primary key (option);

create table notification (
  id varchar(50) not null,
  notification_date datetime default null,
  title text default null,
  message text default null,
  ref_no varchar(50) default null,
  primary key (id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci;

create table notification_user_join (
  notification_id varchar(50) not null,
  user_id varchar(15) not null,
  seen bit(1) not null default b'0',
  updated_date timestamp not null,
  primary key (notification_id,user_id)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_general_ci;