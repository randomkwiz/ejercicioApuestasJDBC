
--crear un procidimeinto para ver los partidos que se puede apostar
select * from Partidos
where DATEDIFF ( DAY , CURRENT_TIMESTAMP , fechaInicio )<=2 and DATEDIFF ( MINUTE ,fechaFin, CURRENT_TIMESTAMP  )>=10
