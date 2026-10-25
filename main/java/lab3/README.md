# Лабораторна робота №3

Точка входу: `lab3.Run`. Запустіть цей клас в IntelliJ IDEA.
Потрібні Java 8 або новіша та кодування UTF-8. Додаткові бібліотеки не потрібні.

Після компіляції Maven можна запустити вікно командою:

```powershell
mvn compile
java -cp target/classes lab3.Run
```

Пакет `model` містить дані університету. `Human` зберігає ПІБ і стать;
`Student` успадковує `Human`. Абстрактний `NamedUnit` зберігає назву
та керівника. `University`, `Faculty`, `Department` і `Group`
зберігають відповідні колекції нижчого рівня на основі `ArrayList`.
`Sex` визначає значення статі.

Інтерфейс `Creator<T>` задає метод `create()` для створення типового об'єкта.
Класи `StudentCreator`, `GroupCreator`, `DepartmentCreator`,
`FacultyCreator` та `UniversityCreator` створюють відповідні об'єкти
і використовують нижчий рівень для побудови ієрархії.
Перевантажені методи дають змогу створювати об'єкти з власними даними.

`Run.createTypycalUniversity()` викликає `UniversityCreator`.
`UniversityGUI` показує результат у дереві та відображає відомості
про вибраний об'єкт. Кнопка створення заново викликає цей метод.
Swing-вікно та його обробники працюють у потоці обробки подій EDT.

Перевірка моделі та Swing-елементів без сторонніх залежностей:

```powershell
mvn test-compile
java -cp "target/classes;target/test-classes" lab3.UniversityVerification
```
