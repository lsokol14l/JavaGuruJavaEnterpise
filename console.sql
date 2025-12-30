-- 1 Часть. Создание таблиц и заполнение их данными.

create database first_db_application;

create table books
(
    id                  serial PRIMARY KEY,
    title               varchar(128),
    year_of_publication int,
    number_of_pages     int,
    author_id           int references authors (id) on delete cascade not null
);

create table authors
(
    id          serial PRIMARY KEY,
    first_name  varchar(128),
    second_name varchar(128)
);

drop table books;
drop table authors;

insert into authors (first_name, second_name)
values ('Andrzej', 'Sapkowski'),
       ('Platon', ''),
       ('David', 'Goggins');

-- 2 Часть. Заполнение данными

select id
from authors
where first_name = 'Andrzej'
  and second_name = 'Sapkowski';


-- Книги Анджея Сапковского в хронологическом порядке
insert into books (title, year_of_publication, number_of_pages, author_id)
values ('Последнее желание', 1993, 2000, 1),
       ('Меч предназначения', 1992, 1000, 1),
       ('Кровь эльфов', 1994, 1500, 1),
       ('Час презрения', 1995, 2500, 1),
       ('Крещение огнем', 1996, 3000, 1),
       ('Башня ласточки', 1997, 3500, 1),
       ('Владычица озера', 1998, 4000, 1)
;

select id
from authors
where first_name = 'Platon'
  and second_name IS NULL;

-- Книга Платона 'Государство'
insert into books (title, year_of_publication, number_of_pages, author_id)
values ('Republic', -370, 480, 2);

select id
from authors
where first_name = 'David'
  and second_name = 'Goggins';

insert into books (title, year_of_publication, number_of_pages, author_id)
values ('Can''t Hurt Me', 2018, 480, 3);

select *
from books;

update books
set number_of_pages = 365
where author_id = (select id from authors where first_name = 'David' and second_name = 'Goggins');

-- 3 Часть. Запросы на выборку данных.
-- 3.1. Вывести название книги, год издания и ФИО автора (имя и фамилию), отсортировав результат по году издания по убыванию.
select b.title, b.year_of_publication, (a.first_name || ' ' || a.second_name) fio
from books b
         join authors a on b.author_id = a.id
order by b.year_of_publication desc;

update authors
set second_name = ' '
where second_name = 'unknown';

-- 3.2. Вывести все книги заданного автора по его имени и фамилии.
select *
from books b
         join authors a on b.author_id = a.id
where a.first_name in ('David', 'Platon');

-- Часть 4: Аналитические запросы
-- 4.1. Вывести книги, у которых количество страниц больше, чем среднее количество страниц у всех книг в базе.
with avg_pages as (select avg(books.number_of_pages) avg
                   from books)
select *
from books b
         join avg_pages on true
where b.number_of_pages >= avg_pages.avg;

-- 4.2. Вывести 3 самые старые книги и посчитать суммарное количество страниц в них.
with old_chart as (select id
                   from books
                   order by year_of_publication
                   limit 3)
select *,
       (select sum(books.number_of_pages)
        from old_chart
                 join books on old_chart.id = books.id)
from books b
         join old_chart oc on b.id = oc.id;

-- Задание 5. Запросы на изменение и удаление.
-- 5.1. Написать запрос, который изменяет год издания на текущий год для одной самой маленькой (по количеству страниц)
-- книги каждого автора.
-- 5.1.0. Найти одну из самых маленьких книг у каждого автора.
with smallest as (select id,
                         row_number() over (partition by author_id order by number_of_pages asc, id asc) as rn
                  from books)
update books
set year_of_publication = date_part('year', now())::int
from smallest s
where books.id = s.id
  and s.rn = 1;

-- Задание 5.2. Написать запрос, который удаляет автора, написавшего самую большую (по количеству страниц) книгу.
select *
from books;

with biggest as (select id, max(number_of_pages) max
                 from books
                 group by id
                 order by max desc
                 limit 1)
delete
from authors
where authors.id = (select books.author_id
                    from books
                             join biggest on biggest.id = books.id
                    where books.id = biggest.id)
