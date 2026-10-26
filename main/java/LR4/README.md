# LR4 — JUnit. JSON

У гілці `lb4` цей інтерфейс доповнено JSON-збереженням, завантаженням і порівнянням.
Актуальні команди збірки, запуску з Gson й тестування наведено в кореневому
[README](../../../README.md).

Точка входу: `LR4.Run`. Запустіть цей клас в IntelliJ IDEA.
Потрібні JDK 8 або новіша та кодування UTF-8. Gson і JUnit налаштовані у Maven.

Після компіляції Maven можна запустити вікно командою:

```powershell
.\mvnw.cmd '-DforkCount=0' clean verify
java -jar target/java-labs-0-4-1.0-SNAPSHOT.jar
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

Окремий запуск перевірки моделі та Swing-елементів після збірки:

```powershell
java -cp "target/java-labs-0-4-1.0-SNAPSHOT.jar;target/test-classes" LR4.UniversityVerification
```
