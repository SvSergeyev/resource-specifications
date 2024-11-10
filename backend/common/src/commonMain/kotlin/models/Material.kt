package models

enum class Material(val description: String, val unit: String) {
    STEEL_PLATE_3("Лист металлический сталь 09Г2С 3,0 мм ГОСТ 19903-90", "м2"),
    ALUMINUM_PLATE_8_80("Алюминиевая прямоугольная шина АД31Т 8х80 мм ГОСТ 15176-89", "м2"),
    COPPER_ROD_10("Пруток медный М1 10,0 мм ГОСТ 1535-2006", "м");

    companion object {
        fun fromDescription(description: String): Material? {
            return entries.find { it.description == description }
        }

        fun fromName(name: String): Material? {
            return entries.find { it.name == name }
        }
    }
}