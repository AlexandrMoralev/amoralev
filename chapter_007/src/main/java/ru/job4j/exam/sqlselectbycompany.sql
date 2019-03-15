-- tables
CREATE TABLE company
(
  id   INTEGER NOT NULL,
  name character varying,
  CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
  id         INTEGER NOT NULL,
  name       character varying,
  company_id integer,
  CONSTRAINT person_pkey PRIMARY KEY (id)
);

--// TASK #1) Retrieve in a single query:
--// - names of all persons that are NOT in the company with id = 5
--// - company name for each person

SELECT person.name, company.name
FROM person,
     company
WHERE person.company_id = company.id
  AND NOT company.id = 5;

-- or --

SELECT person.name, company.name
FROM person
       INNER JOIN company ON person.company_id = company.id
WHERE NOT company.id = 5;


--// TASK #2) Select the name of the company with the maximum number of persons
--     + number of persons in this company

SELECT company.name, count(person.id)
FROM company
       INNER JOIN person ON company.id = person.company_id
GROUP BY company.name
LIMIT 1;
