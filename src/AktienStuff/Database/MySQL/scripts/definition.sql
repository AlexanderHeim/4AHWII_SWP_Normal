create database if not exists aktienstuff collate utf8mb4_general_ci;
use aktienstuff;

create user if not exists 'java'@'localhost' identified by 'password';
grant all on aktienstuff.* to 'java'@'localhost';

#Example Stock Table
create table if not exists stock_time_series_tsla (
	day date not null primary key,
    open decimal(11,2) not null,
    close decimal(11,2) not null,
    adjusted_close decimal(11,2) not null,
    high decimal(11,2) not null,
    low decimal(11,2) not null,
    volume int not null,
    dividend_amount decimal(12,2) not null,
    split_coefficient decimal(4,2) not null
);

select * from stock_time_series_test1;
select * from stock_time_series_test1 where DATE(day) = '2020-07-24';
