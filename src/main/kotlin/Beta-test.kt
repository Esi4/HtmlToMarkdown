fun main() {
    val tagsFont = mutableListOf<Int>()
    val line = """pokupki <s>goose</s> and ytka
        |A vot tut xuinya esli chestno
        |<li><b>Pam paraaaaam</b></li>
        |<li>
        |<p>
        |</p>
        |Da da
    """.trimMargin()
    val vile = "<li><b>Kupchaga-top</b> za svoi <s>stradania</s></li>"
    val map = mapOf("<s>" to "~~", "</s>" to "~~", "<i>" to "*", "</i>" to "*", "<b>" to "**", "</b>" to "**" )
    val gag = "<b>Kupchaga-top</b>"
    for(elem in tagsFont) {
        tagsFont.add(1)
        println(elem)
    }
}
