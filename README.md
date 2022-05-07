# Software Design HW#1, 2

![Coverage](.github/badges/jacoco.svg)

В этом репозитории находится реализация интерпретатора командной строки.

### Запуск
* Вариант 1: `./run.sh` из корня репозитория
* Вариант 2: `java -jar uber-shell-*.jar`. uber-jar файл можно найти в артефактах CI.

### Требования
* Java 11+

### Структура:
* [Исполнитель](src/ru/itmo/sd/shell/processor/CommandProcessor.kt)

  Точка входа, цикл чтения команд, их исполнения и вывода результата.
  Параметризуется потоками ввода-вывода. По умолчанию - stdin, stdout.
* [Парсер](src/ru/itmo/sd/shell/parser/CommandParser.kt)

  Парсер распознает команды, проходит по входным данным с помощью [лексера](src/ru/itmo/sd/shell/parser/CommandLexer.kt).
  * Поддерживает двойные и одинарные кавычки. Внутри одинарных кавычек подстановка переменных не осуществляется.
  * Все открытые кавычки должны быть закрыты, иначе - произойдет ошибка и интерпретатор попросит ввести новую команду.
  * Поддерживаются многострочные пайплайны. Если строка закончилась символом `|`, то пользователю
    будет предложено продолжить ввод команды на следующей строке.
* [Реализованные команды](src/ru/itmo/sd/shell/cli/command)
  * cat - конкатенация файлов или потока ввода, в случае отсутствия аргументов для файлов.
    * Usage:  
      `cat [FILE]...`
  * echo - вывод входных данных
    * Usage:  
    `echo [STRING]...`
  * pwd - вывод названия текущей рабочей директории
    * Usage:  
    `pwd`
  * wc - вывод количества переносов строки, слов и байт в каждом файле или в потоке ввода
    * Usage:  
    `wc [FILE]...`
  * grep - вывод строк, соответствующих шаблонам
    * Usage:  
    `grep [OPTION]... [PATTERN] [FILE]`
    * Options:  
    `-i` - искать совпадения вне зависимости от регистра
    `-w` - искать совпадения с целыми целыми словами
    `-A NUM` - вывести NUM строк после каждой строки, содержащей совпадение с шаблоном
  * external command (вызов внешнего процесса) - вызов команды из реальной командной оболочки операционной системы,
    используется для команд, не поддержанных в рамках этого проекта.
    * Usage:  
    `<some unsupported command`
  * exit (останавливает shell) - остановить интерпретатор
    * Usage:  
    exit
* Пайплайны из команд
  * Например, `echo 123 | cat | cat | cat | wc`.
  * Если строка оканчивается на `|`, то потребуется продолжить команду на следующей строке.
* Объявления переменных в виде `let x = abc`.  
  Переменные могут быть использованы в командах следующим образом: `$x`.
  Они будут автоматически заменены на их значения за исключением случаев, когда они встречаются внутри одинарных кавычек.
