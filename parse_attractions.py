import pandas as pd
import sys
import json
import openpyxl


# получаем пути к файлам из командной строки
file1 = sys.argv[1]


# загружаем данные из файлов в объекты DataFrame
df = pd.read_excel(file1)

# Преобразуйте значения в столбце description в строки
df['description'] = df['description'].astype(str)

# Удалите строки в столбце description, содержащие "Отели на Ostrovok.ru"
df = df[~df['description'].str.contains('Отели на Ostrovok.ru')]

# Удалите строки в столбце description, содержащие "Отели на Ostrovok.ru"
# Удалите только строки с NaN в столбце description
df = df.dropna(subset=['description'])


df.drop("web-scraper-order", axis=1, inplace=True)
df.drop("web-scraper-start-url", axis=1, inplace=True)
# смещаем все столбцы на 1 вправо
df.columns = df.columns[1:].insert(0, '')
df.columns = df.columns[1:].insert(0, '')


# Сохраните измененный DataFrame обратно в файл Excel
df.to_excel('example.xlsx', index=False)

# добавляем значение в ячейку A1
wb = openpyxl.load_workbook('example.xlsx')
sheet = wb.active
sheet['A1'] = 'name'
wb.save('example.xlsx')
