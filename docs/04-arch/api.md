### API

#### Описание используемых сущностей

1. **Material (Материал)**

* Основные параметры (поля)
   1. id
   2. name
   3. unit: единицы измерения


* Функции (эндпоинты)
  1. CRUDS (create, read, update, delete, search)
   

2. **Processing (Обработка)**

* Основные параметры (поля)
  1. id
  2. type: тип обработки (например, `покраска`)
  3. description (например, `RAL 5003`)


* Функции (эндпоинты)
  1. CRUDS (create, read, update, delete, search)


3. **StandardProduct (Стандартное изделие)**

* Основные параметры (поля)
  1. id
  2. name: наименование изделия (например, `Болт М8 х 35`)
  3. standard: стандарт изделия (например, `DIN 933`)
  4. unit: единицы измерения (например, `шт.`)


* Функции (эндпоинты)
  1. CRUDS (create, read, update, delete, search)


4. **Assembly (Сборка)**

* Основные параметры (поля)
    1. id
    2. name
    3. description
    4. subAssemblies
    5. parts
    6. standardProducts


* Функции (эндпоинты)
    1. CRUDS (create, read, update, delete, search)
    2. getMaterialsUsage: рекурсивно собирает количество материалов, учитывая все вложенные сборки, детали и стандартные
       изделия

5. **Part (Деталь)**

* Основные параметры (поля)
    1. id
    2. name
    3. materials: материалы, необходимые для изготовления детали
    4. subParts
    5. processingList: список операций по обработке


* Функции (эндпоинты)
    1. CRUDS (create, read, update, delete, search)

