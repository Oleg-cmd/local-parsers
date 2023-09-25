import pandas as pd
import sys
import json
import openpyxl


def load_data(files):
    loaded_dfs = []
    failed_files = []
    
    for file in files:
        try:
            df = pd.read_excel(file)
            loaded_dfs.append(df)
        except Exception as e:
            print(f"Error in loading file '{file}': {e}")
            failed_files.append(file)
    
    if len(loaded_dfs) == 0:
        print("No files were loaded successfully.")
        return None
    else:
        if len(failed_files) > 0:
            print(f"Some files were not loaded successfully: {failed_files}")
        return pd.concat(loaded_dfs, ignore_index=True)


def preprocess_data(df):
    try:
        # удаляем столбцы "web-scraper-order", "web-scraper-start-url", "stars-class"
        df.drop(["web-scraper-order", "web-scraper-start-url", "stars-class"], axis=1, inplace=True)
        # смещаем все столбцы на 1 вправо
        df.columns = df.columns[1:].insert(0, '')
        df.columns = df.columns[1:].insert(0, '')

        # удаляем строки, содержащие фразу "Мы рекомендуем!" в столбце "description"
        for i, row in df.iterrows():
            description = row['description']
            index = description.find('Мы рекомендуем!')
            if index != -1:
                lines = description.split('\n')
                new_lines = []
                for j in range(len(lines)):
                    if 'Мы рекомендуем!' in lines[j]:
                        continue
                    if lines[j].strip() == '' and (j == 0 or j == len(lines) - 1 or lines[j-1].strip() == '' or lines[j+1].strip() == ''):
                        new_lines.append(lines[j])
                    elif lines[j].strip() != '':
                        new_lines.append(lines[j])
                row['description'] = '\n'.join(new_lines)

        # удаляем лишние пробелы
        for col in df.columns:
            if df[col].dtype == 'O':  # если тип столбца - объект
                df[col] = df[col].str.strip()  # удаляем лишние пробелы

        return df

    except Exception as e:
        print(f"Error in preprocess_data: {e}")
        return None



def process_stars(df):
    try:
        for i, row in df.iterrows():
            stars = row['stars']
            stars_list = json.loads(stars)
            count = sum(1 for star in stars_list if star.get("stars-class") == "hotel_header-stars__item hotelicon hotelicon-star star-filled")
            df.at[i, 'stars'] = count
    except ValueError as e:
        print(f"Error: {e}")
    return df



def sort_data(df):
    try:
        df = df.sort_values(by='stars', ascending=False)
    except KeyError:
        print("Error: the 'stars' column is missing in the dataframe.")
        return None
    except Exception as e:
        print(f"Error during sorting: {e}")
        return None
    return df


def add_header(filepath):
    try:
        wb = openpyxl.load_workbook(filepath)
        sheet = wb.active
        sheet['A1'] = 'name'
        wb.save(filepath)
    except FileNotFoundError:
        print(f"Error: File '{filepath}' not found.")
    except PermissionError:
        print(f"Error: Permission denied for file '{filepath}'.")
    except Exception as e:
        print(f"Error while adding header: {e}")

def main():
    try:
        # получаем пути к файлам из командной строки
        files = sys.argv[1:]
        
        if not files:
            raise ValueError("No input files provided.")
        
        # загружаем данные из файлов в объекты DataFrame
        df = load_data(files)

        # предобработка данных
        df = preprocess_data(df)

        # обработка столбца "stars"
        df = process_stars(df)

        # сортировка данных
        df = sort_data(df)

        # сохранение данных в новый файл
        df.to_excel("parsed.xlsx", index=False)

        # добавляем значение в ячейку A1
        add_header('parsed.xlsx')
        
        print("Data processing completed successfully.")


    except Exception as e:
        print(f"Error: {e}")
    
    
if __name__ == '__main__':
    main()
