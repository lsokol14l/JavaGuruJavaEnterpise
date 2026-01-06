-- 3. Тренируемся писать запросы
-- 3.1 Кто летел рейсом Минск (MNK) - Лондон (LDN) 2020-07-28 на месте B1?
-- Такие тестовые данные получились из-за cross join при заполнении
select *
from ticket t
         join flight f on f.id = t.flight_id
         join seat s on t.seat_no = s.seat_no
where departure_date::date = '2020-07-28'
  and departure_airport_code = 'MNK'
  and arrival_airport_code = 'LDN'
  and s.seat_no = 'B1';

-- 3.2 Какие 2 перелета были самые длительные за все время?
select (arrival_date - departure_date)::time "Продолжительность полета", *
from flight
order by (arrival_date - departure_date)::time desc;

-- 3.3 Какая максимальная и минимальная продолжительность перелетов между Минском и Лондоном и сколько было всего таких перелетов?
select max(arrival_date - departure_date)::time "max полет",
       min(arrival_date - departure_date)::time "min полет",
       count(*)                                 "Количество полетов"
from flight
where departure_airport_code = 'MNK'
  And arrival_airport_code = 'LDN';

-- 3.4 Сколько мест осталось незанятыми 2020-06-14 на рейсе MN3002 (flight_no)?
select count(*) - count(fast.passenger_name)
from (select count(*) - count(passenger_name),
             f.id,
             f.departure_airport_code,
             f.arrival_airport_code,
             a.model,
             s.seat_no,
             t.passenger_name
      from flight f
               join aircraft a on f.aircraft_id = a.id
               join public.seat s on a.id = s.aircraft_id
               left join public.ticket t on f.id = t.flight_id and t.seat_no = s.seat_no
      where flight_no = 'MN3002'
        and departure_date::date = '2020-06-14') as fast;

-- 3.5 Какие имена встречаются чаще всего и какую долю от числа всех пассажиров они составляют?
select "Имя",
       count("Имя")                                                                                 as "Количество",
       (select count(*) from ticket)                                                                as "Всего",
       (count("Имя")::numeric / (select count(*) from ticket)::numeric * 100)::numeric(4, 2) || '%' as "Доля"
from ticket t
         join lower(left(t.passenger_name, (position(' ' in t.passenger_name)))) as "Имя" on true
group by "Имя"
order by "Количество" desc;

-- 3.5 Тоже-самое, но не дублируем запросы
select name, cnt, total, (cnt / total * 100)::numeric(4, 2) || '%' as "Доля"
from (select lower(split_part(passenger_name, ' ', 1)) as name,
             count(*)                                  as cnt,
             sum(count(*)) over ()                     as total
      from ticket
      group by name) t
order by cnt desc;

-- 3.6* Вывести имена пассажиров и сколько билетов пассажир купил за все время?
select t.passport_no,
       t.passenger_name,
       sum(count(*)) over (partition by t.passport_no order by t.passenger_name) as "Количество"
from ticket t
group by t.passenger_name, t.passport_no
order by "Количество" desc;

-- 3.7* Вывести стоимость всех маршрутов по убыванию.
-- А также отобразить для каждой строки разницу текущим и следующим рейсом в отсортированном списке?

with flight_cost as (select flight_id, sum(ticket.cost) as "total"
                     from ticket
                              join public.flight f on ticket.flight_id = f.id
                     group by flight_id)
select id, flight_no, flight_cost.total, coalesce(lead(total) over (order by "id") - total, 0) as "difference"
from flight
         join flight_cost on flight_cost.flight_id = flight.id;