package com.example.model.entities

enum class Color(
    val lightColor: String,
    val darkColor: String
) {
    PINK("#FF7F7F", "#B35A5A"),
    ROSE("#FF80BF", "#B35A8F"),
    BLUSH("#FFD9E1", "#B38F99"),
    ORANGE("#FFBF80", "#B36A4D"),
    YELLOW("#FFD580", "#B38F4D"),
    PALE_YELLOW("#FFF999", "#D4A200"),
    GREEN("#BFFF80", "#8FB35A"),
    PALE_GREEN("#B3FF99", "#8FB36A"),
    MINT_GREEN("#80FFBF", "#4DB38F"),
    SKY_BLUE("#80DFFF", "#4DA6B3"),
    BLUE("#9999FF", "#4D4DB3"),
    LAVENDER("#BF80FF", "#7A4DB3"),
    BROWN("#D5BF9F", "#8F7A66"),
    GRAY_GREEN("#C0E0C0", "#667A66"),
    GRAY_BLUE("#CBD5E0", "#667A8F"),
    BASIC("#EEECEC", "#424242")
}
