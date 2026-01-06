-- Тренируемся с оконными функциями
-- Создание таблицы и наполнение тестовыми данными
CREATE TABLE employees
(
    id         INT,
    name       VARCHAR(50),
    department VARCHAR(50),
    hire_date  DATE,
    salary     DECIMAL(10, 2),
    sales      DECIMAL(10, 2)
);

INSERT INTO employees (id, name, department, hire_date, salary, sales)
VALUES (1, 'Иван Петров', 'Продажи', '2022-01-15', 80000, 150000),
       (2, 'Анна Сидорова', 'ИТ', '2021-03-20', 95000, 0),
       (3, 'Петр Иванов', 'Продажи', '2023-02-10', 75000, 90000),
       (4, 'Мария Кузнецова', 'Продажи', '2020-11-05', 82000, 210000),
       (5, 'Алексей Смирнов', 'ИТ', '2022-07-30', 100000, 0),
       (6, 'Ольга Васильева', 'Маркетинг', '2021-09-12', 88000, 0),
       (7, 'Дмитрий Попов', 'Продажи', '2023-04-22', 78000, 120000),
       (8, 'Елена Новикова', 'Маркетинг', '2020-08-18', 92000, 0),
       (9, 'Сергей Козлов', 'Продажи', '2022-12-01', 76000, 180000),
       (10, 'Наталья Морозова', 'Продажи', '2021-05-14', 85000, 190000),
       (11, 'Андрей Волков', 'ИТ', '2023-06-25', 97000, 0),
       (12, 'Ирина Зайцева', 'Продажи', '2020-12-30', 83000, 220000);

-- Уровень 1: Базовые функции
-- 1. Вывести всех сотрудников с их зарплатой и средней зарплатой по всем сотрудникам в отдельной колонке.
select id, name, salary, (avg(salary) over ())::numeric(10, 2)
from employees;

-- 2. Для каждого сотрудника вывести его имя, отдел, зарплату и среднюю зарплату по его отделу.
select id,
       name,
       department,
       salary,
       (avg(salary) over (partition by department))::numeric(10, 2)
from employees;

-- 3. Вывести сотрудников с их зарплатой и ранжировать их по зарплате по убыванию (с помощью RANK()).
select id, name, salary, rank() over (order by salary desc)
from employees;

-- 4. Вывести сотрудников с их зарплатой и пронумеровать их в порядке убывания зарплаты (с помощью ROW_NUMBER()).
-- Учтите, что у сотрудников с одинаковой зарплатой должны быть разные номера.
select id, name, salary, row_number() over (order by salary)
from employees;

-- 5. Вывести двух сотрудников с самой высокой зарплатой в каждом отделе (используйте DENSE_RANK()).
-- Не очень логично, зачем я беру выборку и соединяю ее саму с собой
select employees.id,
       name,
       salary,
       department,
       rank
from employees
         join
     (select id, dense_rank() over (partition by department order by salary desc) "rank" from employees) as rid
     on rid.id = employees.id
where "rank" <= 2
order by department;

with rid as (select id,
                    name,
                    salary,
                    department,
                    dense_rank() over (partition by department order by salary desc) "Номер"
             from employees)
select id,
       name,
       salary,
       department,
       "Номер"
from rid
where "Номер" <= 2;

-- Уровень 2: Функции смещения и агрегация
-- 1. Для каждого сотрудника отдела "Продажи" вывести его имя,
-- дату найма и дату найма следующего принятого сотрудника в этом отделе (функция LEAD).
select id, name, hire_date, department, lead(hire_date) over (partition by department order by hire_date)
from employees
where department = 'Продажи';

-- 2. Для каждого сотрудника отдела "Продажи" вывести его имя,
-- сумму продаж (sales) и сумму продаж предыдущего сотрудника при сортировке по убыванию продаж (функция LAG).
select id, name, sales, department, lag(sales) over (partition by department order by sales) "Сумма предыдущего"
from employees
where department = 'Продажи';

-- 3. Рассчитать накопительную (кумулятивную) сумму продаж по отделу "Продажи" при порядке сортировки по дате найма.
select id,
       name,
       sales,
       hire_date,
       sum(sales) over (order by hire_date rows between unbounded preceding and current row)
           "Сумма предыдущего"
from employees
where department = 'Продажи';

-- 4. Вывести для каждого сотрудника его зарплату и зарплату самого высокооплачиваемого сотрудника во всей компании.
select id,
       name,
       salary,
       max(salary) over ()
from employees;

-- 5. Вывести для каждого сотрудника его имя, отдел, зарплату и разницу между его зарплатой и средней зарплатой по его отделу.
-- По кайфу сделал
with ads as (select id,
                    name,
                    department,
                    salary,
                    (avg(salary) over (partition by department))::numeric(10, 2) as "avg"
             from employees)
select *, (salary / avg * 100)::numeric(10, 2) || '%' as "Доля"
from ads;

-- Дальше там уже что-то ближе к аналитике данных, а не к запросам для backend

