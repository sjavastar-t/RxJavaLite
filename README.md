# RxJavaLite

Этот проект представляет собой пользовательскую реализацию основных концепций реактивного программирования на Java (аналог RxJava).
Это легковесная, простая для понимания библиотека реактивного программирования с основными функциями для работы с асинхронными потоками данных.


## Описание

В проекте реализована система реактивных потоков с возможностью управления потоками выполнения и обработки событий, построенная на паттерне «Наблюдатель» (Observer). Реализованы базовые компоненты, операторы преобразования данных, планировщики потоков и механизмы отмены подписки.


## Функциональность

* **Основные функции** (в пакете `ru.mail.kievsan.rx.core`):
    * `Observer` — интерфейс с методами:
        * `onNext()`,
        * `onError()`,
        * `onComplete()`;
    * `Observable` — источник данных, фабрики: 
      * `create(OnSubscribe<T> source)`, 
      * `just(T item)`, 
      * `just(T... items)`, 
      * `range(int start, int count)`;
    * `CompositeDisposable` — групповая отмена подписок;
    * `Disposable` — отмена одной подписки;

* **Операторы** (в пакете `ru.mail.kievsan.rx.operators`):
    * `MapOperator` (`map`),
    * `FilterOperator` (`filter`),
    * `FlatMapOperator` (`flatMap`),
    * `MergeOperator` (`merge`),
    * `ReduceOperator` (`reduce`);

* **Schedulers** (в пакете `ru.mail.kievsan.rx.schedulers`):
    * `ComputationScheduler` (fixed thread pool),
    * `IOScheduler` (cached thread pool),
    * `SingleScheduler` (single-thread executor).
    * Принципы работы Schedulers:
      * `subscribeOn()` определяет поток подписки.
      * `observeOn()` переключает поток обработки событий.

## Технологии

* Java 17+
* Maven
* SLF4J API + Log4j
* JUnit 5


## Архитектура системы

1. **Паттерн Observer**:

    * Источник (`Observable`) делегирует эмиссию элементов через `OnSubscribe`.
    * Потребитель реализует `Observer` или передаёт лямбды в `subscribe()`.
    * `Disposable` контролирует отмену, `CompositeDisposable` — групповую отмену.

2. **Структура пакетов**:

    * `core` — базовые компоненты и фабрики.
    * `operators` — классы-операторы для модульности.
    * `schedulers` — управление планировщиками потоков.

3. **Flow**:

    * Построение цепочки: `Observable.create(...)` → операторы → `subscribeOn()`/`observeOn()` → `subscribe()`.
    * Все переходы потоков выполняются через `Scheduler.schedule(...)`.


## Тестирование

В проекте написаны юнит-тесты JUnit 5 для ключевых сценариев:

1. **Базовая работа**
    * `create()` + `subscribe(onNext, onError, onComplete)`
    * `just()`, проверка эмиссии и завершения.
   
2. **Операторы**
    * `map`, `filter`
    * `flatMap`, `merge`, `scan`
   
3. **Планировщики**
    * `subscribeOn`/`observeOn` проверка переключения потоков.
   
4. **Обработка ошибок**
    * Эмит `onError`, проверка прекращения `onNext`.
   
5. **Отмена подписки**
    * `Disposable.dispose()`, `CompositeDisposable.dispose()`.

