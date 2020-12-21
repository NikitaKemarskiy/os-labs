# Оптимізація роботи з памяттю

Нехай розмір трьомірного масиву дорівнує 200 елементів на вимір.

Час роботи програми до профілювання та оптимізацій:
`213 ms`

Час роботи програми після профілювання та оптимізацій:
`71 ms`

При ручному профілюванні було виявлено, що багато часу займає погана локалізованість даних у просторі.
Локалізація була покращена завдяки локалізованим зверненням до елементів масиву.