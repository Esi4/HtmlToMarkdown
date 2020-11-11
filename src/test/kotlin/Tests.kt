import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class Tests {

    @Test
    fun marker() { //тест нумерованного и маркированного списков
        converter("input/input.html", "input/output.md")
        val result = File("input/output.md").readText().replace(Regex("[\\t\\s\\n]"), "") //переводим результат и выходной файл в одну строку, без пробелов, т.к тест реагирует на пустые символы и пробелы
        val expected =
                """

* Список ~~покупок~~
* Курица 
* Картошка 
* Приправа 
* Список товаров 
1. Швабра 
2. Хлеб 
* Багет 
3. Колбаса 
* Фрукты 
1. Сливы 
2. Бананы 
1. Зеленые 
2. Спелые
                    """.trimIndent().replace(Regex("[\\s\\n\\t]"), "")
        assertEquals(expected, result)
        File("input/output.md").delete()
    }

    @Test
    fun error() { //тест ошибки
        converter("input/input1.html", "input/output.md")
        val result = File("input/output.md").readText().replace(Regex("[\\t\\s\\n]"), "")
        val expected = """"Invalid input file""""
        assertEquals(expected, result)
        File("input/output.md").delete()
    }

    @Test
    fun other() { //тест шрифтов, списков и абзацев. Т.е всего вместе
        converter("input/input3.html", "input/output.md")
        val result = File("input/output.md").readText().replace(Regex("[\\t\\s\\n]"), "")
        val expected =
                """
                Мрак и холод, ветер стонет
                
                Тяжело *сдавать* долги
                Учись и вера тьму *разгонит*
                **Долг сдадим,** спадут хвосты
                 
                Список дел
                 * сделать салат
                    1.Цезарь
                    2.Оливье
                 * ~~убраться~~ в *коридоре*
                    """.trimIndent().replace(Regex("[\\t\\s\\n]"), "")
        assertEquals(expected, result)
        File("input/output.md").delete()
    }
}