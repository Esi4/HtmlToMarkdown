import java.io.File

fun converter(inputName: String, outputName: String) {
    val result = File(outputName).bufferedWriter()
    val draft = mutableListOf<String>()
    val tags = mutableListOf<String>()
    val numList = mutableListOf<Int>()
    val map = mapOf("<s>" to "~~", "</s>" to "~~", "<i>" to "*", "</i>" to "*", "<b>" to "**", "</b>" to "**" )

    fun plusMarker() {
        numList.add(1)
        tags.add("<ol>")
    }

    fun marker() { //метод для нумерации или обозначения списков разных типов
        if (tags[tags.size - 1] == "<ul>") {
            result.write("* ")
        }
        else if(tags[tags.size - 1] == "<ol>"){
            val x = numList[numList.size - 1]
            result.write("$x. ")
            numList[numList.size - 1]++
        }
    }

    fun error() {
        throw  Exception("Invalid input file")
    }

    //***метод для конвертации текста***\\\
    fun detected(line: String) {
        val sp = if(line.trim().matches(Regex("""^<li>(.*)""")) && line.matches(Regex("""(.*)</li>$"""))) { //распознаем тег <li> в конце и/или вначале строки
            line.trim().drop(4).dropLast(5).split(" ") as MutableList<String> //разделяем строку на слова
        } else if(line.trim().matches(Regex("""^<li>(.*)"""))) { //и обрезаем с одного из краёв/с обоих краёв тег <li>
            line.trim().drop(4).split(" ") as MutableList<String>
        } else line.trim().dropLast(5).split(" ") as MutableList<String>
        if(tags.size != 0) { marker() }
        for(element in sp) {
            var g = 0
            for (elem in map.keys) {
                if (element.startsWith(elem) || element.endsWith(elem) && g == 0) { //сверяем начало и/или конец слова с тегом шрифтов
                    g++
                    if (element.matches(Regex("""^<(.*)>(.*)""")) && element.matches(Regex("""(.*)</(.*)>$"""))) { //распознаём в какой части слова стоят теги
                        result.write(map.getValue(elem).toString()) //записываем из значение, равное ключу в мапе
                        result.write(element.drop(3).dropLast(4))
                        result.write(map.getValue(elem).toString())
                    } else if (element.matches(Regex("""^<(.*)>(.*)"""))) {
                        result.write(map.getValue(elem).toString())
                        result.write(element.drop(3))
                    } else if (element.matches(Regex("""(.*)</(.*)>$"""))) {
                        result.write(element.dropLast(4))
                        result.write(map.getValue(elem).toString())
                    }
                }
            }
            if (g == 0) { //записываем слова без тегов на шрифт
                result.write("$element ")
            }
        }
        result.newLine()
    }

    //***метод перебора и распознавания тегов: тип списка, абзац***\\
    fun trunk(number: Int) {
        if(!(draft[0].trim() == "<html>" && draft[1].trim() == "<body>" && draft[draft.size - 1].trim() == "</html>" && draft[draft.size - 2].trim() == "</body>")) {
            error()
        } //проверка на корректность начала и конца входного файла
        for(i in number until draft.size) {
            when(draft[i].trim()) {
                "<p>" -> result.newLine()
                "<ul>" -> tags.add("<ul>")
                "<ol>" -> plusMarker()
                "</ul>" -> tags.removeAt(tags.size - 1)
                "</ol>" -> tags.removeAt(tags.size - 1)

            }
            if (!draft[i].trim().matches(Regex("""^<[/*\w]*>$"""))) { //распознавание строки с текстом
                detected(draft[i])
            }
        }
        if(tags.size != 0) { //если список в конце не равен нулю, значит какой-то тег не был закрыт
            error()
        }
        result.close()
    }

    //***Весь текст переписываем в лист, построчно***\\
    for(line in File(inputName).readLines()) {
        draft.add(line)
    }

    trunk(0)
}